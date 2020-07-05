package com.kt.ibs.validations;

import com.kt.ibs.controllers.vo.CustomerAuditInput;

import lombok.extern.slf4j.Slf4j;

import static com.kt.ibs.constants.Constants.*;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CustomerAuditValidator extends InputOutputResults {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public CustomerAuditValidator() {
        super();
    }

    public Notifications[] validateCustomerAuditInput(final CustomerAuditInput input) {
        Set<Notifications> responseList = new HashSet<>();
        log.info("*** validating request input on registration ***");
        if (checkEmpty.test(input.getUsername()) && checkEmpty.test(input.getEventType())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    CUSTOMER_AUDIT,
                    CODEERROR));
            log.debug("Customer audit details are empty");
        }

        log.info("Validation messages : " + responseList.size());
        Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
        return ((responseNotificationMessages == null) || (responseNotificationMessages.length == 0)) ? new Notifications[]{} : responseNotificationMessages;
    }
}
