package com.kt.ibs.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.controllers.vo.CustomerAuditOutput;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.security.JwtTokenUtil;
import com.kt.ibs.service.CustomerService;


@RestController
public class CustomerAuditController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomerService customerService;

    public CustomerAuditController() {
        super();
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/audits/{eventType}", headers = "Accept=*/*", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RestResponse> fetchCustomerAudits(@PathVariable("username") final String username, @PathVariable("eventType") final String eventType) throws IBSException {
        RestResponse restResponse = customerService.fetchCustomerAudits(username, eventType);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restResponse);
    }

    @RequestMapping(value = "/ibs/api/customer-audits/", headers = "Accept=*/*", method = RequestMethod.GET)
    @ResponseBody
    public List<CustomerAuditOutput> searchCustomerAudits(@RequestParam(value = "username", required = false) final String username,
            @RequestParam(value = "eventType", required = false) final String eventType) throws IBSException {
        return customerService.fetchAllAudits(username, eventType);
    }
}
