package com.kt.ibs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.kt.ibs.controllers.vo.AccountDetails;
import com.kt.ibs.controllers.vo.BeneficiariesPayment;
import com.kt.ibs.controllers.vo.BeneficiariesPaymentApprovalRequest;
import com.kt.ibs.controllers.vo.BeneficiariesPaymentApprovalResponse;
import com.kt.ibs.controllers.vo.IntraTransferResponse;
import com.kt.ibs.controllers.vo.Transaction;
import com.kt.ibs.controllers.vo.TransferInput;
import com.kt.ibs.controllers.vo.TransferResponseDetails;
import com.kt.ibs.entity.Account;
import com.kt.ibs.entity.AccountTransaction;
import com.kt.ibs.entity.AuditType;
import com.kt.ibs.entity.Beneficiary;
import com.kt.ibs.entity.Currency;
import com.kt.ibs.entity.Customer;
import com.kt.ibs.entity.CustomerAudit;
import com.kt.ibs.entity.CustomerAuthorization;
import com.kt.ibs.entity.OtpType;
import com.kt.ibs.entity.TransactionStatus;
import com.kt.ibs.entity.TransactionType;
import com.kt.ibs.entity.Transfer;
import com.kt.ibs.entity.TransferType;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.repository.AccountRepository;
import com.kt.ibs.repository.AccountTransactionRepository;
import com.kt.ibs.repository.BeneficiaryRepository;
import com.kt.ibs.repository.CurrencyRepository;
import com.kt.ibs.repository.CustomerAuthorizationRepository;
import com.kt.ibs.repository.CustomerRepository;
import com.kt.ibs.repository.TransferRepository;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.validations.InputOutputResults;
import com.kt.ibs.validations.Notifications;
import com.kt.ibs.validations.TransferValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BeneficiariesTransferService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

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
    private AccountTransactionRepository accountTransactionRepository;

    @Autowired
    private CoreBankingService coreBankingService;
    @Autowired
    private AccountService accountService;

    public BeneficiariesPaymentApprovalRequest createTransfer(final BeneficiariesPayment input, final String username) throws IBSException {
        Customer owner = customerRepository.findByUsername(username).get();
        Account sourceAccount = accountRepository.findByUsernameAndAccountNumber(username, input.getSourceAccountNumber());
        String oneTimePin = PinUtil.generatePin();
        CustomerAuthorization authorization = customerAuthorizationRepository.save(new CustomerAuthorization(owner, new Date(), oneTimePin, OtpType.TRANSFER));
        List<String> transfers = new ArrayList<>();
        input.getPayments().stream()
                .filter(payment -> (payment.getAmount() != null) && (payment.getAmount().doubleValue() > 0)).forEach(payment -> {
                    Beneficiary beneficiary = beneficiaryRepository.findByCustomerUsernameAndUuid(username, payment.getBenId());
                    Transfer transfer = new Transfer();
                    transfer.setSourceAccount(sourceAccount);
                    transfer.setBeneficiary(beneficiary);
                    transfer.setTransactionStatus(TransactionStatus.PENDING);
                    transfer.setAuthorization(authorization);
                    transfer.setCreditCurrency(beneficiary.getCurrency());
                    // transfer.setDebitCurrency(currencyRepository.findByCode(input.getDebtCurrency()));
                    // transfer.setDebitAmount(input.getTransferAmount());
                    transfer.setCreditAmount(payment.getAmount());
                    transfer.setCustomer(owner);
                    transfer = transferRepository.save(transfer);
                    transfers.add(transfer.getUuid());
                });

        applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.TRANSFER_INITIATED.name()));
        notificationService.sendOTPNotification(owner, String.format("Enter Pin %s to complete transfer ", oneTimePin));
        return new BeneficiariesPaymentApprovalRequest(transfers, input, null);

    }

    public BeneficiariesPaymentApprovalResponse approveTransfer(final BeneficiariesPaymentApprovalRequest request) {
        boolean hasError = false;
        Customer customer = null;
        Account sourceAccount = null;
        List<Transaction> transactions = new ArrayList<>();
        for (final String uuid : request.getTransferUuids()) {
            Transfer transfer = transferRepository.findByUuid(uuid);
            sourceAccount = transfer.getSourceAccount();
            log.info("Transfer details for {} are {}", uuid, transfer.getCreditAmount());
            final CustomerAuthorization authorization = transfer.getAuthorization();
            customer = authorization.getCustomer();
            if (authorization.getOtp().equals(request.getOneTimePin())) {
                authorization.setDateAuthorized(new Date());
                TransferInput transferInput = new TransferInput();
                transferInput.setCreditCurrency(transfer.getCreditCurrency().getCode());
                transferInput.setTransferAmount(transfer.getCreditAmount());
                // transferInput.setDebtCurrency(transfer.getDebitCurrency().getCode());
                applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.TRANSFER_APPROVAL_SUCCESS.name()));
                TransferResponseDetails transferResponseDetails = coreBankingService.beneficiaryTransfer(transferInput, transfer.getBeneficiary(), transfer.getSourceAccount());

                if ("Success".equalsIgnoreCase(transferResponseDetails.getStatus())) {
                    Currency baseCurrency = currencyRepository.findByCode(transferResponseDetails.getBaseCurrency());
                    // Currency debitCurrency =
                    // currencyRepository.findByCode(transferResponseDetails.getDebitCurrency());
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
                    transfer.setProcessingDate(transferResponseDetails.getCoreProcessedTime());
                    transfer.setTransactionStatus(TransactionStatus.SUCCESS);
                    transfer.setCoreReference(transferResponseDetails.getCoreReference());
                    applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.CORE_BEN_PAYMENT_SUCCESS.name()));
                    transfer = transferRepository.save(transfer);

                    AccountTransaction transaction = new AccountTransaction();
                    transaction.setAccount(transfer.getSourceAccount());
                    transaction.setAmount(transferInput.getTransferAmount());
                    transaction.setBeneficiary(transfer.getBeneficiary());
                    transaction.setBookingDate(transfer.getCreditValueDate());
                    transaction.setCreatedTime(transfer.getProcessingDate());
                    transaction.setCurrency(transfer.getCreditCurrency());
                    transaction.setCustomer(transfer.getCustomer());

                    transaction.setImmediate(false);
                    transaction.setReference(transferResponseDetails.getCoreReference());
                    transaction.setTargetReference(transfer.getBeneficiary().getBeneficiaryReference());
                    transaction.setValueDate(transfer.getCreditValueDate());
                    transaction.setTransfer(transfer);

                    // transaction = accountTransactionRepository.save(transaction);
                    transactions.add(new Transaction(transaction));
                    String message = transaction.getBeneficiary().getBranch().getBank().getBankName() + " "
                            + transaction.getCurrency().getCode() + " " + transaction.getAmount() + " paid to account "
                            + transaction.getBeneficiary().getAccount().getAccountNumber()
                            + " Ref : " + transaction.getBeneficiary().getBeneficiaryReference()
                            + " " + transaction.getBookingDate();

                    notificationService.sendPaymentNotification(transaction, message);

                } else {
                    applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.TRANSFER_FAILED_T24_ERROR.name()));
                    return new BeneficiariesPaymentApprovalResponse(false, "Transfer from  " + transfer.getSourceAccount().getAccountNumber() + " to " + transfer.getBeneficiary().getAccount()
                            .getAccountNumber() + " failed. Contact Admin for help !", transactions);
                }

            } else {
                hasError = true;
                applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.TRANSFER_APPROVAL_FAILED.name()));
                final int authAttempts = authorization.getAuthAttempts() + 1;
                authorization.setAuthAttempts(authAttempts);
            }
            customerAuthorizationRepository.save(authorization);
        }
        if (customer != null) {
            List<AccountDetails> coreAccounts = coreBankingService.fetchAccounts(customer.getCustomerId());
            final Customer cust = customer;
            coreAccounts.stream().forEach(coreAccount -> accountService.updateLocalAccount(coreAccount, cust));
            if (sourceAccount != null) {
                accountService.fetchAccountTransactions(customer.getUsername(), sourceAccount.getAccountNumber());
            }
        }
     
        return new BeneficiariesPaymentApprovalResponse(!hasError, null, transactions);

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
            TransferResponseDetails transferResponseDetails = coreBankingService.intraTransfer(input);

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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.getExceptionResponse());
        }

        return restServiceResponse;
    }
}
