package com.kt.ibs.validations;

import com.kt.ibs.entity.Currency;

import lombok.extern.slf4j.Slf4j;

import static com.kt.ibs.constants.Constants.*;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CreateCurrencyValidator extends InputOutputResults {

    public CreateCurrencyValidator() {
        super();
    }

    public Notifications[] validateCurrencyInput(Currency input) {
        Set<Notifications> responseList = new HashSet<>();
        log.info("*** validating request input on registering user ***");

        if (checkEmpty.test(input.getCode())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    CURRENCY_CODE,
                    CODEERROR));
            log.debug("Currency Code is empty");
        }
        if (checkEmpty.test(input.getDescription())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    CURRENCY_DESCRIPTION,
                    CODEERROR));
            log.debug("Currency description is empty");
        }

        log.info("Validation messages : " + responseList.size());
        Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
        return (responseNotificationMessages == null || responseNotificationMessages.length == 0) ? new Notifications[]{} : responseNotificationMessages;
    }

}
