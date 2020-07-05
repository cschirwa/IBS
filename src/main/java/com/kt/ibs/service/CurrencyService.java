package com.kt.ibs.service;

import com.kt.ibs.constants.Constants;
import com.kt.ibs.entity.Currency;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.repository.CurrencyRepository;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.validations.CreateCurrencyValidator;
import com.kt.ibs.validations.InputOutputResults;
import com.kt.ibs.validations.Notifications;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.kt.ibs.constants.Constants.CODEERROR;
import static com.kt.ibs.constants.Constants.LEVELERROR;

@Service
public class CurrencyService {

    private final static Logger LOGGER = Logger.getLogger(CurrencyService.class);
    @Autowired
    private CurrencyRepository currencyRepository;

    public RestResponse createCurrency(final Currency input) throws IBSException {
        CreateCurrencyValidator validator = new CreateCurrencyValidator();
        RestResponse restServiceResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        Notifications[] notifications = validator.validateCurrencyInput(input);
        restServiceResponse.setServiceResponseHeader(inputOutputResults.determineResponse(notifications));

        if (notifications.length > 0) {
            return restServiceResponse;
        }

        if (null == currencyRepository.findByCode(input.getCode())) {
            Currency savedInstance = currencyRepository.save(input);
            restServiceResponse.setMessage("Currency " + savedInstance.getDescription() + " created successfully");

        } else {
            Notifications notification = new Notifications(LEVELERROR, "Currency already exist!", Constants.CURRENCY_CODE, CODEERROR);
            Set<Notifications> responseList = new HashSet<>();
            responseList.add(notification);
            Notifications[] responseNotificationMessages = responseList.toArray(new Notifications[0]);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.determineResponse(responseNotificationMessages));
        }

        LOGGER.info("service task is complete");

        return restServiceResponse;
    }

    public RestResponse fetchCurrencies() throws IBSException {
        RestResponse restServiceResponse = new RestResponse();
        restServiceResponse.setData(currencyRepository.findAll());
        return restServiceResponse;
    }
}
