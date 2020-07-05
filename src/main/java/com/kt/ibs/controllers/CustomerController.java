package com.kt.ibs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.controllers.vo.CustomerSettings;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CustomerController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private CustomerService customerService;

    public CustomerController() {
        super();
    }

    @PostMapping(value = "/ibs/api/customers/{username}/settings/", headers = "Accept=*/*")
    @ResponseBody
    public ResponseEntity<RestResponse> updateSettings(@PathVariable("username") final String username, @RequestBody final CustomerSettings customerSettings) throws IBSException {
        RestResponse restResponse = new RestResponse();
        customerService.updateProfile(username, customerSettings);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restResponse);
    }
}
