package com.kt.ibs.controllers;

import java.util.List;
import java.util.stream.Collectors;

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

import com.kt.ibs.controllers.vo.BeneficiaryActivationRequest;
import com.kt.ibs.controllers.vo.BeneficiaryDetails;
import com.kt.ibs.controllers.vo.SelectData;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.security.JwtTokenUtil;
import com.kt.ibs.service.BeneficiaryService;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BeneficiaryController {


    @Autowired
    private BeneficiaryService beneficiaryService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public BeneficiaryController() {
        super();
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/beneficiaries", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity createBeneficiary(
            @RequestBody @Valid final BeneficiaryDetails input,
            @ApiParam(value = "Authorization token", required = true) @RequestHeader("Authorization") final String authorization,
            @PathVariable("username") final String username) throws IBSException {


        log.info("IBS create new beneficiary : " + input);
        RestResponse restResponse = beneficiaryService.createBeneficiary(input, username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restResponse);
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/beneficiaries/active", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity activateBeneficiary(
            @RequestBody @Valid final BeneficiaryActivationRequest input,
            @PathVariable("username") final String username) throws IBSException {
        log.info("Activate new beneficiary with {}", input);
        RestResponse restResponse = beneficiaryService.activateBeneficiary(username,input);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restResponse);
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/beneficiaries/{uuid}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<RestResponse> deleteBeneficiary(@PathVariable("username") final String username, @PathVariable("uuid") final String uuid) throws IBSException {
       beneficiaryService.removeBeneficiary(username, uuid);
       RestResponse response = new RestResponse();
       response.setData(uuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
    @GetMapping(value = "/ibs/api/customers/{username}/beneficiaries/{uuid}/payments")
    @ResponseBody
    public BeneficiaryDetails fetchPayments(@PathVariable("username") final String username, @PathVariable("uuid") final String uuid) throws IBSException {
       return  beneficiaryService.fetchTransfers(username, uuid);
        
    }
     

    @RequestMapping(value = "/ibs/api/customers/{username}/beneficiaries/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RestResponse> fetchBeneficiaries(@PathVariable("username") final String username) throws IBSException {
             RestResponse restResponse = beneficiaryService.fetchBeneficiaries(username);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restResponse);
    }

    @RequestMapping(value = "/ibs/api/customers/{username}/beneficiaries/select", method = RequestMethod.GET)
    @ResponseBody
    public List<SelectData> fetchBeneficiariesSelect(@PathVariable("username") final String username) {
        List<BeneficiaryDetails> beneficiaryDetails = (List<BeneficiaryDetails>) beneficiaryService.fetchBeneficiaries(username).getData();
        return beneficiaryDetails.stream()
                .map(beneficiary -> new SelectData(beneficiary.getUuid(), beneficiary.getBeneficiaryFullname(), false))
                .collect(Collectors.toList());
    }
}
