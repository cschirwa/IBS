package com.kt.ibs.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.kt.ibs.controllers.vo.BeneficiaryActivationRequest;
import com.kt.ibs.controllers.vo.BeneficiaryDetails;
import com.kt.ibs.controllers.vo.OneTimePinActivationResponse;
import com.kt.ibs.controllers.vo.TransferResponseDetails;
import com.kt.ibs.entity.Account;
import com.kt.ibs.entity.AuditType;
import com.kt.ibs.entity.BankBranch;
import com.kt.ibs.entity.Beneficiary;
import com.kt.ibs.entity.Customer;
import com.kt.ibs.entity.CustomerAudit;
import com.kt.ibs.entity.CustomerAuthorization;
import com.kt.ibs.entity.OtpType;
import com.kt.ibs.entity.TransactionStatus;
import com.kt.ibs.entity.Transfer;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.repository.AccountRepository;
import com.kt.ibs.repository.BankBranchRepository;
import com.kt.ibs.repository.BeneficiaryRepository;
import com.kt.ibs.repository.CountryRepository;
import com.kt.ibs.repository.CurrencyRepository;
import com.kt.ibs.repository.CustomerAuthorizationRepository;
import com.kt.ibs.repository.CustomerRepository;
import com.kt.ibs.repository.TransferRepository;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.validations.AddBeneficiaryValidator;
import com.kt.ibs.validations.InputOutputResults;
import com.kt.ibs.validations.Notifications;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private TransferRepository transferRepository;

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
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CountryRepository countryRepository;

    public void removeBeneficiary(final String username, final String uuid) {
        final Beneficiary beneficiary = beneficiaryRepository.findByCustomerUsernameAndUuid(username, uuid);
        beneficiary.setActive(false);
        beneficiaryRepository.save(beneficiary);

    }

    public BeneficiaryDetails fetchTransfers(final String username, final String uuid) {
        BeneficiaryDetails beneficiaryDetails = new BeneficiaryDetails(beneficiaryRepository.findByCustomerUsernameAndUuid(username, uuid));
        beneficiaryDetails.setPayments(transferRepository.findByCustomerUsernameAndBeneficiaryUuid(username, uuid).stream()
                .map(TransferResponseDetails::new)
                .collect(Collectors.toList()));
        return beneficiaryDetails;
    }

    public RestResponse createBeneficiary(final BeneficiaryDetails input, final String username) throws IBSException {
        AddBeneficiaryValidator validator = new AddBeneficiaryValidator();
        RestResponse restServiceResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        try {
            Notifications[] notifications = validator.validateBeneficiary(input);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.determineResponse(notifications));

            if (notifications.length > 0) {
                return restServiceResponse;
            }

            Customer owner = customerRepository.findByUsername(username).get();
            Beneficiary beneficiary = new Beneficiary(owner, input.getBeneficiaryFullname(),
                    input.getPaymentCategory(), input.getBeneficiaryReference(), input.getSenderReference());
            final BankBranch bankBranch = bankBranchRepository.findByBankBankCodeAndBranchCode(input.getBankCode(), input.getBranchCode()).get(0);

            Account account = Account.beneficiary(input.getAccountNumber(),
                    currencyRepository.findByCode(input.getCurrency()),
                    bankBranch);
            account.setAccountTitle1(input.getAccountName());
            String oneTimePin = PinUtil.generatePin();
            CustomerAuthorization authorization = customerAuthorizationRepository.save(new CustomerAuthorization(owner, new Date(), oneTimePin, OtpType.ADD_BENEFICIARY));

            beneficiary.setAuthorization(authorization);
            beneficiary.setCountry(countryRepository.findByCode(input.getCountry()));
            beneficiary.setNotificationDetails(input.getNotificationDetails());
            beneficiary.setNotificationType(input.getNotificationType());
            beneficiary.setCurrency(currencyRepository.findByCode(input.getCurrency()));
            beneficiary.setAccount(accountRepository.save(account));
            Beneficiary savedInstance = beneficiaryRepository.save(beneficiary);
            log.info("Beneficiary save with uuid {}", savedInstance.getUuid());
            restServiceResponse.setData(new BeneficiaryDetails(savedInstance));
            restServiceResponse
                    .setMessage("Beneficiary " + savedInstance.getBeneficiaryFullname() + " created successfully");
            applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.BEN_INITIATED.name()));
            notificationService.sendOTPNotification(owner, String.format("Enter Pin %s to activate beneficiary %s", oneTimePin, input.getBeneficiaryFullname()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.getExceptionResponse());
        }

        return restServiceResponse;
    }

    public RestResponse activateBeneficiary(final String username, final BeneficiaryActivationRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findByCustomerUsernameAndUuid(username, request.getBeneficiaryUuid());
        log.info("Beneficiary for {} is {}", request.getBeneficiaryUuid(), beneficiary);
        final CustomerAuthorization authorization = beneficiary.getAuthorization();
        OneTimePinActivationResponse response;

        if (authorization.getOtp().equals(request.getOneTimePin())) {
            beneficiary.setActive(true);
            authorization.setDateAuthorized(new Date());
            beneficiary = beneficiaryRepository.save(beneficiary);
            response = new OneTimePinActivationResponse(true, 0, new BeneficiaryDetails(beneficiary));
            applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.BEN_APPR_SUCCESS.name()));

        } else {
            applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.BEN_APPR_FAIL.name()));
            final int authAttempts = authorization.getAuthAttempts() + 1;
            authorization.setAuthAttempts(authAttempts);
            response = new OneTimePinActivationResponse(false, authAttempts, null);
        }
        customerAuthorizationRepository.save(authorization);

        final RestResponse restServiceResponse = new RestResponse();
        restServiceResponse.setData(response);
        return restServiceResponse;
    }

    public RestResponse fetchBeneficiaries(final String username) {
        RestResponse restResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        restResponse.setMessage("");
        try {
            Customer owner = customerRepository.findByUsername(username).get();
            List<Beneficiary> recipients = beneficiaryRepository.findByCustomerAndActive(owner, true);
            List<BeneficiaryDetails> list = recipients.stream().map(BeneficiaryDetails::new)
                    .map(ben -> {
                        List<Transfer> transfers = transferRepository.findFirstByBeneficiaryUuidAndTransactionStatusOrderByProcessingDateDesc(ben.getUuid(), TransactionStatus.SUCCESS);
                        if ((transfers != null) && !transfers.isEmpty()) {
                            Transfer transfer = transfers.get(0);
                            ben.setLastPayment(transfer.getCreditAmount());
                            ben.setLastPaymentDate(transfer.getProcessingDate());
                        }
                        return ben;

                    })
                    .collect(Collectors.toList());
            restResponse.setData(list);
            restResponse.setServiceResponseHeader(inputOutputResults.determineResponse(null));
            restResponse.setMessage("Success!");
            log.info("service task is complete");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            restResponse.setServiceResponseHeader(inputOutputResults.getExceptionResponse());
        }

        return restResponse;
    }
}
