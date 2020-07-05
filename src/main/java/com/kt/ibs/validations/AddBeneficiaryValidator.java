package com.kt.ibs.validations;

import static com.kt.ibs.constants.Constants.ACCOUNT_NUMBER;
import static com.kt.ibs.constants.Constants.ACCOUNT_TYPE;
import static com.kt.ibs.constants.Constants.BENEFICIARY_FULLNAME;
import static com.kt.ibs.constants.Constants.BENEFICIARY_REFERENCE;
import static com.kt.ibs.constants.Constants.CODEERROR;
import static com.kt.ibs.constants.Constants.DEFAULTREQUIREDERRORMESSAGE;
import static com.kt.ibs.constants.Constants.LEVELERROR;
import static com.kt.ibs.constants.Constants.SENDER_REFERENCE;
import static com.kt.ibs.constants.Constants.checkEmpty;

import java.util.HashSet;
import java.util.Set;

import com.kt.ibs.controllers.vo.BeneficiaryDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddBeneficiaryValidator extends InputOutputResults {

//    private static final Logger LOGGER = Logger.getLogger(AddBeneficiaryValidator.class);

    private static final String PHONE_NUMBER_PATTERN = "^[0]{1}[0-9]{9}$";

    public AddBeneficiaryValidator() {
        super();
    }

    public Notifications[] validateBeneficiary(final BeneficiaryDetails input) {
        Set<Notifications> responseList = new HashSet<>();
        log.info("*** validating request input on registering user ***");

        if (checkEmpty.test(input.getBeneficiaryFullname())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    BENEFICIARY_FULLNAME,
                    CODEERROR));
            log.debug("Beneficiary name is empty");
        }
        if (checkEmpty.test(input.getBeneficiaryReference())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    BENEFICIARY_REFERENCE,
                    CODEERROR));
            log.debug("Beneficiary Reference  is empty");
        }
        if (checkEmpty.test(input.getAccountNumber())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    ACCOUNT_NUMBER,
                    CODEERROR));
            log.debug("Account number is empty");
        }
        if (checkEmpty.test(input.getSenderReference())) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    SENDER_REFERENCE,
                    CODEERROR));
            log.debug("Sender reference is empty");
        }
        if (null == input.getAccountType()) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    ACCOUNT_TYPE,
                    CODEERROR));
            log.debug("Account type is empty");
        }

        log.info("Validation messages : " + responseList.size());
        Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
        return (responseNotificationMessages == null || responseNotificationMessages.length == 0) ? new Notifications[]{} : responseNotificationMessages;
    }

}
