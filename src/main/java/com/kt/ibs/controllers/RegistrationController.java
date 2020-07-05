package com.kt.ibs.controllers;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.controllers.vo.CustomerConfirmationRequest;
import com.kt.ibs.controllers.vo.CustomerRegInput;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.service.CustomerService;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class RegistrationController {

    private static final Logger LOGGER = Logger.getLogger(RegistrationController.class);
    private static final String REQUEST = "REQUEST";
    private static final String RESPONSE = "RESPONSE";

    @Autowired
    private CustomerService customerService;

    public RegistrationController() {
        super();
    }

    @RequestMapping(value = "/ibs/api/customer-registrations", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity registerCustomer(
            @RequestBody @Valid CustomerRegInput input) throws IBSException {
        LOGGER.info("IBS register new customer : " + input);
        RestResponse restResponse = customerService.registerCustomer(input);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restResponse);
    }

    @RequestMapping(value = "/ibs/api/customer-confirmation", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity confirmCustomer(
            @RequestBody @Valid final CustomerConfirmationRequest input) throws IBSException {
        log.info("Confirm customer with {}", input);
        RestResponse restResponse = customerService.confirmCustomer(input);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restResponse);
    }

}
