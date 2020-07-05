package com.kt.ibs.validations;

import com.kt.ibs.response.ServiceResponseHeader;

import lombok.extern.slf4j.Slf4j;

import com.kt.ibs.constants.Constants;

import static com.kt.ibs.constants.Constants.CODE_SUCCESS;
import static com.kt.ibs.constants.Constants.REPONSE_MESSAGE_SUCCESS;
import static com.kt.ibs.constants.Constants.RESPONSE_MESSAGE_ERROR;
import static com.kt.ibs.constants.Constants.SYSTEM_ERROR;
import static com.kt.ibs.constants.Constants.EXCEPTIONERROR;
import static com.kt.ibs.constants.Constants.LEVELERROR;
//import static com.kt.ibs.constants.Constants.*;

import static com.kt.ibs.constants.ResponseCodes.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class InputOutputResults {
    private static final String num = "[a-zA-Z]+";

    /**
     * @param notifications level, message, code
     * @return ServiceHeaderResponse the default service header response format for AB
     */
    public ServiceResponseHeader determineResponse(Notifications[] notifications) {
        boolean errorFound = false;
        boolean exceptionFound = false;
        Date currentDate = new Date();

        ServiceResponseHeader serviceHeaderResponse = new ServiceResponseHeader();
        serviceHeaderResponse.setResultCode(CODE_SUCCESS);
        serviceHeaderResponse.setResultDescription(REPONSE_MESSAGE_SUCCESS);
        serviceHeaderResponse.setTimestamp(currentDate.toString());
        serviceHeaderResponse.setNotifications(notifications);


        if (null != notifications && notifications.length > 0) {
            forloop:
            for (Notifications notification : notifications) {
                if (notification.getCode() == CODE_ERROR.getResponseLevel()) {
                    errorFound = true;
                    break forloop;
                } else if (notification.getCode() == CODE_EXCEPTION.getResponseLevel()) {
                    exceptionFound = true;
                }
            }

            if (errorFound) {
                serviceHeaderResponse.setResultCode(CODE_ERROR.getResponseLevel());
                serviceHeaderResponse.setResultDescription(RESPONSE_MESSAGE_ERROR);
            } else if (exceptionFound) {
                serviceHeaderResponse.setResultCode(CODE_EXCEPTION.getResponseLevel());
                serviceHeaderResponse.setResultDescription(SYSTEM_ERROR);
            }
        }

        return serviceHeaderResponse;
    }

    public ServiceResponseHeader getExceptionResponse() {
        Notifications notification = new Notifications(LEVELERROR, "Something went wrong!", Constants.SYSTEM_ERROR, EXCEPTIONERROR);
        Set<Notifications> responseList = new HashSet<>();
        responseList.add(notification);
        Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
        return determineResponse(responseNotificationMessages);
    }


    protected Notifications makeValidationErrorPayload(String level, String message, String field, int errorCode) {
        return new Notifications(level, message, field, errorCode);
    }

}
