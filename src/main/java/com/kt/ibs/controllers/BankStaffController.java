package com.kt.ibs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.controllers.vo.Analytics;
import com.kt.ibs.controllers.vo.BankStaffDetails;
import com.kt.ibs.controllers.vo.BankStaffOnboarding;
import com.kt.ibs.entity.BankStaff;
import com.kt.ibs.exceptions.IBSException;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.service.AdminQueryService;
import com.kt.ibs.service.BankStaffService;

@RestController
public class BankStaffController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private BankStaffService bankStaffService;

    @Autowired
    private AdminQueryService adminQueryService;

    @RequestMapping(value = "/ibs/api/{bank}/bank-staff/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RestResponse> fetchBankStaff(@PathVariable("bank") final String bank) {
        RestResponse response = new RestResponse();
        response.setData(bankStaffService.fetchBankStaff(bank));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/ibs/api/{bank}/bank-staff/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<BankStaffDetails> onboardStaffMember(@RequestBody final BankStaffOnboarding input) throws IBSException {
        BankStaff addedStaffMember = bankStaffService.addStaffMember(input);
        return ResponseEntity.status(HttpStatus.OK).body(new BankStaffDetails(addedStaffMember.getUsername(), addedStaffMember.getUuid(), addedStaffMember.getFullname(), input.getBank(),
                addedStaffMember.getLastLoginTime()));
    }

    @RequestMapping(value = "/ibs/api/analytics/today/", method = RequestMethod.GET)
    @ResponseBody
    public Analytics fetchStats() {
        return adminQueryService.fetchAdminStats();
    }

}
