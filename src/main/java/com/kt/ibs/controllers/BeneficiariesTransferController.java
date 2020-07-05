package com.kt.ibs.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.controllers.vo.BeneficiariesPayment;
import com.kt.ibs.controllers.vo.BeneficiariesPaymentApprovalRequest;
import com.kt.ibs.controllers.vo.BeneficiariesPaymentApprovalResponse;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.service.BeneficiariesTransferService;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BeneficiariesTransferController {

    @Autowired
    private BeneficiariesTransferService beneficiariesTransferService;

    @RequestMapping(value = "/ibs/api/customers/{username}/beneficiaries-payments", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<BeneficiariesPaymentApprovalRequest> createBeneficiariesTransfer(
            @RequestBody @Valid final BeneficiariesPayment input,
            @ApiParam(value = "Authorization token", required = true) @RequestHeader("Authorization") final String authorization,
            @PathVariable("username") final String username) throws IBSException {
        log.info("IBS create new BeneficiariesPayment  : {}", input);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(beneficiariesTransferService.createTransfer(input, username));
    }
    
    
    @RequestMapping(value = "/ibs/api/customers/{username}/beneficiaries-payments/active", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<BeneficiariesPaymentApprovalResponse> approveBeneficiariesTransfer(
            @RequestBody @Valid final BeneficiariesPaymentApprovalRequest input,
            @ApiParam(value = "Authorization token", required = true) @RequestHeader("Authorization") final String authorization,
            @PathVariable("username") final String username) throws IBSException {
        log.info("IBS create new BeneficiariesPayment  : {}", input);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(beneficiariesTransferService.approveTransfer(input));
    }
}
