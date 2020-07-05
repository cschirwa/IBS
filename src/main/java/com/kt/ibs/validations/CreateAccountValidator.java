package com.kt.ibs.validations;

import com.kt.ibs.controllers.vo.CreateAccountInputDetails;

import lombok.extern.slf4j.Slf4j;

import static com.kt.ibs.constants.Constants.*;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CreateAccountValidator extends InputOutputResults {

    private static final String PHONE_NUMBER_PATTERN = "^[0]{1}[0-9]{9}$";

    public CreateAccountValidator() {
        super();
    }

    public Notifications[] validateAccount(CreateAccountInputDetails input) {
        Set<Notifications> responseList = new HashSet<>();
        log.info("*** validating request input on registering user ***");

        if (checkEmpty.test(input.getCustomerId())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    CUSTOMER_ID,
                    CODEERROR));
            log.debug("Customer Id is empty");
        }
        if (checkEmpty.test(input.getAccountTitle1())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    ACCOUNT_TITLE_1,
                    CODEERROR));
            log.debug("Account Title is empty");
        }
        if (checkEmpty.test(input.getCategory())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    ACCOUNT_CATEGORY,
                    CODEERROR));
            log.debug("Account category is empty");
        }
        if (checkEmpty.test(input.getPositionType())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    POSITION_TYPE,
                    CODEERROR));
            log.debug("Position type is empty");
        }
        if (checkEmpty.test(input.getCurrency())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    CURRENCY,
                    CODEERROR));
            log.debug("Currency is empty");
        }

        log.info("Validation messages : " + responseList.size());
        Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
        return (responseNotificationMessages == null || responseNotificationMessages.length == 0) ? new Notifications[]{} : responseNotificationMessages;
    }

}
