package com.kt.ibs.validations;

import static com.kt.ibs.constants.Constants.*;

import java.util.HashSet;
import java.util.Set;

import com.kt.ibs.controllers.vo.TransferInput;
import com.kt.ibs.entity.Account;
import com.kt.ibs.entity.Beneficiary;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TransferValidator extends InputOutputResults {

    private static final String PHONE_NUMBER_PATTERN = "^[0]{1}[0-9]{9}$";

    public TransferValidator() {
        super();
    }

    public Notifications[] validateTransferDetails(final TransferInput input, final Account account, final Beneficiary beneficiary) {
        Set<Notifications> responseList = new HashSet<>();
        log.info("*** validating request input on transfer ***");
        if (input.getTransferType().equals("Beneficiary") && (null == beneficiary)) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    BENEFICIARY,
                    CODEERROR));
            log.debug("Beneficiary is null");
        }
        String transferDetails = checkAmountValidity(account, input);
        if (!"Valid".equalsIgnoreCase(transferDetails)) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    transferDetails,
                    CODEERROR));
            log.debug(transferDetails);
        }
        log.info("Validation messages : " + responseList.size());
        Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
        return ((responseNotificationMessages == null) || (responseNotificationMessages.length == 0)) ? new Notifications[]{} : responseNotificationMessages;
    }

    public Notifications[] validateIntraTransferDetails(final TransferInput input, final Account sourceAccount, final Account targetAccount) {
        Set<Notifications> responseList = new HashSet<>();
        log.info("*** validating request input on transfer ***");
        if (null == sourceAccount) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    SOURCE_ACCOUNT,
                    CODEERROR));
            log.debug("Source account is null");
        }
        if (null == targetAccount) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    DESTINATION_ACCOUNT,
                    CODEERROR));
            log.debug("Target account is null");
        }
        String transferDetails = checkAmountValidity(sourceAccount, input);
        if (!"Valid".equalsIgnoreCase(transferDetails)) {
            responseList.add(makeValidationErrorPayload(
                    LEVELERROR,
                    DEFAULTREQUIREDERRORMESSAGE,
                    transferDetails,
                    CODEERROR));
            log.debug(transferDetails);
        }
        log.info("Validation messages : " + responseList.size());
        Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
        return ((responseNotificationMessages == null) || (responseNotificationMessages.length == 0)) ? new Notifications[]{} : responseNotificationMessages;
    }

    private String checkAmountValidity(final Account account, final TransferInput transferInput) {

        String valid = VALID;
        if ((null != account) && (null != transferInput)) {
            if (account.getOnlineActualBalance().compareTo(transferInput.getTransferAmount()) < 1) {
                valid = INSUFFICIENT_FUNDS;
            }
        } else {
            valid = TRANSFER_DETAILS;
        }
        return valid;
    }
}
