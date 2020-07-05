package com.kt.ibs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.security.JwtTokenUtil;
import com.kt.ibs.service.AccountService;

import io.swagger.annotations.ApiParam;

@RestController
public class AccountsController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccountService accountService;


    @RequestMapping(value = "/ibs/api/customers/{username}/accounts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RestResponse> fetchAccounts(@PathVariable("username") final String username, @RequestParam(value = "category", required = false) final String category) throws IBSException {
        RestResponse response = accountService.fetchAccounts(username, ((category == null) || category.equals("undefined")) ? "1" : category);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/loan-accounts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RestResponse> fetchLoanAccounts(@PathVariable("username") final String username, @RequestParam(value = "category", required = false) final String category)
            throws IBSException {
        RestResponse response = accountService.fetchAccounts(username, ((category == null) || category.equals("undefined")) ? "3001-3999" : category);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/savings-accounts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RestResponse> fetchSavingsAccounts(@PathVariable("username") final String username, @RequestParam(value = "category", required = false) final String category)
            throws IBSException {
        RestResponse response = accountService.fetchAccounts(username, ((category == null) || category.equals("undefined")) ? "6001-6999" : category);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/accounts/{accountNumber}/transactions", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RestResponse> fetchAccountTransactions(@PathVariable("username") final String username, @PathVariable("accountNumber") final String accountNumber,
            @ApiParam(value = "Authorization token", required = true) @RequestHeader("Authorization") final String authorization) throws IBSException {
        RestResponse response = accountService.fetchAccountTransactions(username, accountNumber);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @RequestMapping(value = "/ibs/api/customers/{username}/accounts/{accountNumber}/transactions/{reference}", headers = "Accept=*/*", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<InputStreamResource> fetchAccountTransactionDocument(@PathVariable("username") final String username, @PathVariable("accountNumber") final String accountNumber,
//            @PathVariable("reference") final String reference) throws IBSException {
//        return accountTransactionService.generatePaymentReport(username, accountNumber, reference);
//    }

//    @RequestMapping(value = "/ibs/api/customers/{username}/accounts/{accountNumber}/statements/", headers = "Accept=*/*", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<InputStreamResource> fetchAccountStatement(@PathVariable("username") final String username, @PathVariable("accountNumber") final String accountNumber,
//            @RequestParam(value = "startDate", required = false) final String startDate, @RequestParam(value = "endDate", required = false) final String endDate) throws Exception
//             {
//        return accountTransactionService.generateStatementReport(username, accountNumber);
//    }


}
