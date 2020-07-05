package com.kt.ibs.validations;

import com.kt.ibs.controllers.vo.CustomerRegInput;

import lombok.extern.slf4j.Slf4j;

import static com.kt.ibs.constants.Constants.*;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class RegisterCustomerValidator extends InputOutputResults {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public RegisterCustomerValidator() {
        super();
    }

    public Notifications[] validateCustomerDetails(final CustomerRegInput input) {
        Set<Notifications> responseList = new HashSet<>();
        log.info("*** validating request input on registration ***");
        if (checkEmpty.test(input.getUsername())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    USERNAME,
                    CODEERROR));
            log.debug("Username is empty");
        }
        if (checkEmpty.test(input.getEmailAddress())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    EMAIL_ADDRESS,
                    CODEERROR));
            log.debug("Email Address is empty");
        }
        if (checkEmpty.test(input.getFullname())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    PREFERRED_NAME,
                    CODEERROR));
            log.debug("Preferred name is empty");
        }
        if (checkEmpty.test(input.getCustomerNumber())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    CUSTOMER_ID,
                    CODEERROR));
            log.debug("Customer ID is empty");
        }
        if (!validPassword(input)) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    PASSWORD,
                    CODEERROR));
            log.debug("Password is invalid");
        }
        log.info("Validation messages : " + responseList.size());
        Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
        return ((responseNotificationMessages == null) || (responseNotificationMessages.length == 0)) ? new Notifications[]{} : responseNotificationMessages;
    }

    private Boolean validPassword(final CustomerRegInput input) {
        Boolean retVal = true;
        if (checkEmpty.test(input.getPassword()) || checkEmpty.test(input.getRepeatPassword())) {
            retVal = false;
        } else if (!input.getPassword().equals(input.getRepeatPassword())) {
            retVal = false;
        } else if (input.getPassword().length() < 6) {
            /*Pattern pattern = Pattern.compile(PASSWORD_REGEX);
            Matcher matcher = pattern.matcher(input.getPassword());*/
            retVal = false;
        }
        return retVal;

    }

}
