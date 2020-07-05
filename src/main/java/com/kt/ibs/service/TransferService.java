package com.kt.ibs.service;

import com.kt.ibs.controllers.vo.*;
import com.kt.ibs.entity.*;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.repository.*;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.validations.InputOutputResults;
import com.kt.ibs.validations.Notifications;
import com.kt.ibs.validations.TransferValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransferService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private BankBranchRepository bankBranchRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CustomerAuthorizationRepository customerAuthorizationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CoreBankingService coreBankingService;
    
    @Autowired
    private AccountService accountService;

    public RestResponse createLocalTransfer(final TransferInput input, final String username) throws IBSException {
        TransferValidator validator = new TransferValidator();
        RestResponse restServiceResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        Beneficiary beneficiary = null;
        Account sourceAccount = accountRepository.findByUsernameAndAccountNumber(username, input.getSourceAccountNumber());
        Account destinationAccount = null;
        try {
            if ("Beneficiary".equals(input.getTransferType())) {
                beneficiary = beneficiaryRepository.findByCustomerUsernameAndUuid(username, input.getRecipient());
            }

            if ("Transfer".equals(input.getTransferType())) {
                destinationAccount = accountRepository.findByUsernameAndAccountNumber(username, input.getRecipient());
            }

            Notifications[] notifications = validator.validateTransferDetails(input, sourceAccount, beneficiary);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.determineResponse(notifications));

            if (notifications.length > 0) {
                return restServiceResponse;
            }

            Customer owner = customerRepository.findByUsername(username).get();

            String oneTimePin = PinUtil.generatePin();
            CustomerAuthorization authorization = customerAuthorizationRepository.save(new CustomerAuthorization(owner, new Date(), oneTimePin, OtpType.TRANSFER));

            Transfer transfer = new Transfer();
            transfer.setSourceAccount(sourceAccount);
            transfer.setBeneficiary(beneficiary);
            transfer.setTransactionStatus(TransactionStatus.PENDING);
            transfer.setAuthorization(authorization);
            transfer.setCreditCurrency(currencyRepository.findByCode(input.getCreditCurrency()));
            // transfer.setDebitCurrency(currencyRepository.findByCode(input.getDebtCurrency()));
            // transfer.setDebitAmount(input.getTransferAmount());
            transfer.setCreditAmount(input.getTransferAmount());
            transfer.setCustomer(owner);

            Transfer savedInstance = transferRepository.save(transfer);
            log.info("Transfer save with uuid {}", savedInstance.getUuid());

            restServiceResponse.setData(new PaymentApprovalRequest(input.getTransferType(), savedInstance.getUuid(), null));
            restServiceResponse
                    .setMessage("Transfer from  " + savedInstance.getSourceAccount().getAccountNumber() + " actioned successfully");
            applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.TRANSFER_INITIATED.name()));
            notificationService.sendOTPNotification(owner, String.format("Enter Pin %s to complete transfer ", oneTimePin));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.getExceptionResponse());
        }

        return restServiceResponse;
    }

    public RestResponse approveTransfer(final TransferActivationRequest request) {

        TransferResponseDetails transferResponseDetails;
        Transfer transfer = transferRepository.findByUuid(request.getTransferUUID());
        log.info("Transfer details for {} are {}", request.getTransferUUID(), transfer.getCreditAmount());
        final CustomerAuthorization authorization = transfer.getAuthorization();
        OneTimePinActivationResponse response;

        if (authorization.getOtp().equals(request.getOneTimePin())) {
            TransferInput transferInput = new TransferInput();
            transferInput.setCreditCurrency(transfer.getCreditCurrency().getCode());
            transferInput.setTransferAmount(transfer.getCreditAmount());
            // transferInput.setDebtCurrency(transfer.getDebitCurrency().getCode());

            transferResponseDetails = coreBankingService.beneficiaryTransfer(transferInput, transfer.getBeneficiary(), transfer.getSourceAccount());

            Currency baseCurrency = currencyRepository.findByCode(transferResponseDetails.getBaseCurrency());
            // Currency debitCurrency = currencyRepository.findByCode(transferResponseDetails.getDebitCurrency());
            Currency creditCurrency = currencyRepository.findByCode(transferResponseDetails.getCreditCurrency());

            transfer.setCreditAmount(transferResponseDetails.getCreditAmount());
            transfer.setTransactionType(TransactionType.DEBIT);
            transfer.setPaymentMethod(transferResponseDetails.getPaymentMethod());
            transfer.setCurrencymktdr(transferResponseDetails.getCurrencymktdr());
            // transfer.setDebitCurrency(debitCurrency);
            // transfer.setDebitAmount(transferResponseDetails.getDebitAmount());
            transfer.setCreditAmount(transferResponseDetails.getCreditAmount());
            transfer.setDebitValueDate(transferResponseDetails.getDebitValueDate());
            transfer.setCurrencymktcr(transferResponseDetails.getCurrencymktcr());
            transfer.setCreditCurrency(creditCurrency);
            transfer.setCreditValueDate(transferResponseDetails.getCreditValueDate());
            transfer.setTreasuryRate(transferResponseDetails.getTreasuryRate());
            transfer.setProcessingDate(transferResponseDetails.getProcessingDate());
            transfer.setTransactionStatus(TransactionStatus.SUCCESS);

            authorization.setDateAuthorized(new Date());
            response = new OneTimePinActivationResponse(true, 0, null);
            transferRepository.save(transfer);
            applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.TRANSFER_APPROVAL_SUCCESS.name()));

        } else {
            applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.TRANSFER_APPROVAL_FAILED.name()));
            final int authAttempts = authorization.getAuthAttempts() + 1;
            authorization.setAuthAttempts(authAttempts);
            response = new OneTimePinActivationResponse(false, authAttempts, null);
        }
        customerAuthorizationRepository.save(authorization);
        List<AccountDetails> coreAccounts = coreBankingService.fetchAccounts(authorization.getCustomer().getCustomerId());
        coreAccounts.forEach(coreAccount -> accountService.updateLocalAccount(coreAccount, authorization.getCustomer()));
        
        
        final RestResponse restServiceResponse = new RestResponse();
        restServiceResponse.setData(response);
        return restServiceResponse;
    }

    public RestResponse createIntraTransfer(final TransferInput input, final String username) throws IBSException {
        TransferValidator validator = new TransferValidator();
        RestResponse restServiceResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        Account sourceAccount = accountRepository.findByUsernameAndAccountNumber(username, input.getSourceAccountNumber());
        Account destinationAccount = accountRepository.findByUsernameAndAccountNumber(username, input.getBeneficiaryAccountNumber());
        try {
            Notifications[] notifications = validator.validateIntraTransferDetails(input, sourceAccount, destinationAccount);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.determineResponse(notifications));

            if (notifications.length > 0) {
                return restServiceResponse;
            }

            Customer owner = customerRepository.findByUsername(username).get();

            if (sourceAccount.getCurrency().getCode().equalsIgnoreCase(destinationAccount.getCurrency().getCode())) {
                input.setCreditCurrency(sourceAccount.getCurrency().getCode());
            } else if (sourceAccount.getCurrency().getCode().equalsIgnoreCase(input.getCreditCurrency())) {
                input.setDebtCurrency(sourceAccount.getCurrency().getCode());
            }
            TransferResponseDetails transferResponseDetails = coreBankingService.intraTransfer(input);
            if ("SUCCESS".equalsIgnoreCase(transferResponseDetails.getStatus())) {

                Currency currency = currencyRepository.findByCode(transferResponseDetails.getCreditCurrency());

                Transfer transfer = new Transfer(TransactionType.INTRA_TRANSFER, transferResponseDetails.getPaymentMethod(),
                        transferResponseDetails.getCurrencymktdr(), currency, transferResponseDetails.getDebitAmount(),
                        transferResponseDetails.getDebitValueDate(), transferResponseDetails.getIndebitValueDate(),
                        transferResponseDetails.getCurrencymktcr(), currency, transferResponseDetails.getCreditAmount(),
                        transferResponseDetails.getCreditValueDate(), transferResponseDetails.getTreasuryRate(),
                        transferResponseDetails.getProcessingDate(), sourceAccount, destinationAccount, null, owner, TransferType.INTRA_TRANSFER);
                transfer.setCoreReference(transferResponseDetails.getCoreReference());
                transfer.setTransactionStatus(TransactionStatus.SUCCESS);
                transfer.setCustomer(owner);

                Transfer savedInstance = transferRepository.save(transfer);
                log.info("Transfer save with uuid {}", savedInstance.getUuid());

                restServiceResponse.setData(new IntraTransferResponse(true, transferResponseDetails));
                restServiceResponse
                        .setMessage("Transfer from  " + savedInstance.getSourceAccount().getAccountNumber() + " to " + destinationAccount.getAccountNumber() + " actioned successfully");
                applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.TRANSFER_APPROVAL_SUCCESS.name()));
            } else {
                restServiceResponse.setData(new IntraTransferResponse(false, null));
                restServiceResponse
                        .setMessage("Transfer from  " + sourceAccount.getAccountNumber() + " to " + destinationAccount.getAccountNumber() + " failed. Contact Admin for help !");
                applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.TRANSFER_FAILED_T24_ERROR.name()));
            }
            List<AccountDetails> coreAccounts = coreBankingService.fetchAccounts(owner.getCustomerId());
            coreAccounts.forEach(coreAccount -> accountService.updateLocalAccount(coreAccount, owner));
            
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.getExceptionResponse());
        }

        return restServiceResponse;
    }

    public List<TransferResponseDetails> fetchTransfers(final String username) {
        return transferRepository.findByCustomerUsername(username).stream()
                .map(TransferResponseDetails::new)
                .collect(Collectors.toList());
    }
}
