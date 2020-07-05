package com.kt.ibs.service;

import com.kt.ibs.controllers.vo.*;
import com.kt.ibs.entity.*;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.repository.*;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.validations.CustomerAuditValidator;
import com.kt.ibs.validations.InputOutputResults;
import com.kt.ibs.validations.Notifications;
import com.kt.ibs.validations.RegisterCustomerValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.kt.ibs.constants.Constants.*;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CustomerAuthorizationRepository customerAuthorizationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CustomerAuditRepository customerAuditRepository;

    @Autowired
    private CoreBankingService coreBankingService;

    @Autowired
    private AuthorityRepository authorityRepository;

    public RestResponse registerCustomer(final CustomerRegInput input) throws IBSException {
        RegisterCustomerValidator validator = new RegisterCustomerValidator();
        RestResponse restServiceResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        try {
            Notifications[] notifications = validator.validateCustomerDetails(input);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.determineResponse(notifications));
            if (notifications.length > 0) {
                return restServiceResponse;
            }

            try {
                // Call to T24
                CustomerDetails customerDetails = coreBankingService.fetchCustomerDetails(input.getCustomerNumber());
                if (null == customerDetails) {
                    Notifications notification = new Notifications(LEVELERROR,
                            "Customer not found on Core Banking System!", CUSTOMER_ID, CODEERROR);
                    Set<Notifications> responseList = new HashSet<>();
                    responseList.add(notification);
                    Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
                    restServiceResponse.setServiceResponseHeader(
                            inputOutputResults.determineResponse(responseNotificationMessages));
                    return restServiceResponse;
                }
                if (validateCustomer(input, customerDetails)) {
                    Optional<Customer> user = customerRepository.findByUsername(input.getUsername());

                    if ((user == null) || !user.isPresent()) {
                        String oneTimePin = PinUtil.generatePin();
                        CustomerAuthorization authorization = customerAuthorizationRepository.save(new CustomerAuthorization(null, new Date(), oneTimePin, OtpType.CUSTOMER_REGISTRATION));

                        applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.CUSTOMER_REGISTRATION_INITIATED.name()));

                        Customer customer = new Customer(input.getUsername(), input.getPassword(),  authorityRepository.findByName(AuthorityName.ROLE_RETAIL_CUSTOMER));
                        customer.setNotificationType(NotificationType.EMAIL);
                        customer.setEmail(input.getEmailAddress());
                        customer.setAlternativeEmail(input.getAlternativeEmailAddress());
                        customer.setFullname(input.getFullname());
                        customer.setCustomerId(input.getCustomerNumber());
                        customer.setAuthorization(authorization);
                        customer.setNotificationType(NotificationType.valueOf(input.getNotificationChannel()));
                        customer = customerRepository.save(customer);
                        authorization.setCustomer(customer);
                        customerAuthorizationRepository.save(authorization);

                        notificationService.sendOTPNotification(customer, String.format("Enter Pin %s to activate beneficiary %s", oneTimePin, input.getFullname()));
                        input.setCustomerUuid(customer.getUuid());
                        restServiceResponse.setData(input);
                        restServiceResponse.setMessage("Customer " + input.getFullname() + " registered successfully");

                    } else {
                        Notifications notification = new Notifications(LEVELERROR,
                                "Username already taken, try a different one!", USERNAME, CODEERROR);
                        Set<Notifications> responseList = new HashSet<>();
                        responseList.add(notification);
                        Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
                        restServiceResponse.setServiceResponseHeader(
                                inputOutputResults.determineResponse(responseNotificationMessages));
                        return restServiceResponse;
                    }

                } else {
                    Notifications notification = new Notifications(LEVELERROR,
                            "Customer details mismatch!", CUSTOMER_ID, CODEERROR);
                    Set<Notifications> responseList = new HashSet<>();
                    responseList.add(notification);
                    Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
                    restServiceResponse.setServiceResponseHeader(
                            inputOutputResults.determineResponse(responseNotificationMessages));
                    return restServiceResponse;
                }
            } catch (Exception ex) {
                throw new IBSException(ex.getMessage(), ex);
            }

            log.info("service task is complete");
        } catch (IBSException e) {
            log.error("Exception", e);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.getExceptionResponse());
        }

        return restServiceResponse;
    }

    public RestResponse confirmCustomer(final CustomerConfirmationRequest request) {
        Customer customer = customerRepository.findByUuid(request.getCustomerUuid()).get();
        final CustomerAuthorization authorization = customer.getAuthorization();
        OneTimePinActivationResponse response;

        if (authorization.getOtp().equals(request.getOneTimePin())) {
            customer.setEnabled(true);

            authorization.setDateAuthorized(new Date());
            response = new OneTimePinActivationResponse(true, 0, null);
            customerRepository.save(customer);
            applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.CUSTOMER_REGISTRATION_SUCCESS.name()));

        } else {
            applicationEventPublisher.publishEvent(new CustomerAudit(AuditType.CUSTOMER_REGISTRATION_FAILED.name()));
            final int authAttempts = authorization.getAuthAttempts() + 1;
            authorization.setAuthAttempts(authAttempts);
            response = new OneTimePinActivationResponse(false, authAttempts, null);
        }
        customerAuthorizationRepository.save(authorization);

        final RestResponse restServiceResponse = new RestResponse();
        restServiceResponse.setData(response);
        return restServiceResponse;
    }

    public RestResponse fetchCustomerAudits(final String username, final String eventType) throws IBSException {
        CustomerAuditValidator validator = new CustomerAuditValidator();
        RestResponse restServiceResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        List<CustomerAudit> customerAudits = null;
        try {
            if (checkEmpty.test(eventType)) {
                customerAudits = customerAuditRepository.findByUsername(username);
            } else if (checkEmpty.test(username)) {
                customerAudits = customerAuditRepository.findByEventType(eventType);
            } else {
                customerAudits = customerAuditRepository.findByUsernameAndEventType(username, eventType);
            }
            List<CustomerAuditOutput> customerAuditOutputs = convertToDTO(customerAudits);
            restServiceResponse.setData(customerAuditOutputs);
        } catch (Exception ex) {
            throw new IBSException(ex.getMessage(), ex);
        }
        return restServiceResponse;
    }

    public CustomerSettings initiateProfile(final String username, final CustomerSettings settings) {
        Customer customer = customerRepository.findByUsername(username).get();
        boolean valid = true;

        if (BCrypt.checkpw(settings.getOldPassword(), customer.getPassword())) {
            valid = false;
        }
        if (valid) {
            String oneTimePin = PinUtil.generatePin();
            CustomerAuthorization authorization = new CustomerAuthorization(customer, new Date(), oneTimePin, OtpType.CUSTOMER_SETTINGS);
            customerAuthorizationRepository.save(authorization);
            notificationService.sendOTPNotification(customer, String.format("Your one time pin is %s", oneTimePin));
        }
        return settings;

    }

    public void updateProfile(final String username, final CustomerSettings settings) {
        Customer customer = customerRepository.findByUsername(username).get();
        if (Objects.nonNull(settings.getNotificationType())) {
            customer.setNotificationType(settings.getNotificationType());
        }
        customer.setNotificationDetails(settings.getNotificationDetails());
        if (BCrypt.checkpw(settings.getOldPassword(), customer.getPassword())) {
            customer.updatePassword(settings.getNewPassword());
        }

    }

    public List<CustomerAuditOutput> fetchAllAudits(final String username, final String eventType) throws IBSException {
        return convertToDTO(customerAuditRepository.findAll());
    }

    private List<CustomerAuditOutput> convertToDTO(final List<CustomerAudit> customerAudits) {
        List<CustomerAuditOutput> customerAuditOutputs = new ArrayList<>();
        for (CustomerAudit customerAudit : customerAudits) {
            CustomerAuditOutput customerAuditOutput = new CustomerAuditOutput();
            customerAuditOutput.setCustomerId(customerAudit.getCustomerId());
            customerAuditOutput.setEventDate(customerAudit.getEventDate());
            customerAuditOutput.setEventType(customerAudit.getEventType());
            customerAuditOutput.setUsername(customerAudit.getUsername());
            customerAuditOutput.setToken(customerAudit.getToken());
            customerAuditOutputs.add(customerAuditOutput);
        }
        return customerAuditOutputs;
    }

    private boolean validateCustomer(CustomerRegInput input, CustomerDetails customerDetails) {
        if (input.getEmailAddress().equalsIgnoreCase(customerDetails.getEmail()) &&
                input.getDateOfBirth().equals(customerDetails.getDateOfBirth()) &&
                input.getPhoneNumber().equalsIgnoreCase(customerDetails.getMobileNumber())) {
            return true;
        } else {
            return false;
        }
    }
}
