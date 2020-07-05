package com.kt.ibs.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.controllers.vo.TransferActivationRequest;
import com.kt.ibs.controllers.vo.TransferInput;
import com.kt.ibs.controllers.vo.TransferResponseDetails;
import com.kt.ibs.entity.TransferType;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.security.JwtTokenUtil;
import com.kt.ibs.service.TransferService;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public TransferController() {
        super();
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/localTransfer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<RestResponse> createLocalTransfer(
            @RequestBody @Valid final TransferInput input,
            @ApiParam(value = "Authorization token", required = true) @RequestHeader("Authorization") final String authorization,
            @PathVariable("username") final String username) throws IBSException {
        RestResponse restResponse;


        log.info("IBS create new transfer : " + input);
        if (TransferType.INTRA_TRANSFER.toString().equalsIgnoreCase(input.getTransferType())) {
            restResponse = transferService.createIntraTransfer(input, username);
        } else {
            restResponse = transferService.createLocalTransfer(input, username);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restResponse);
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/transfer/approve", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<RestResponse> approveTransfer(
            @RequestBody @Valid final TransferActivationRequest input,
            @PathVariable("username") final String username) throws IBSException {
        log.info("Approve transfer with {}", input);
        RestResponse restResponse = transferService.approveTransfer(input);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restResponse);
    }
    
    @GetMapping(value = "/ibs/api/customers/{username}/payments")
    @ResponseBody
    public List<TransferResponseDetails> fetchPayments(@PathVariable("username") final String username) throws IBSException {
        return transferService.fetchTransfers(username);
    }

}
