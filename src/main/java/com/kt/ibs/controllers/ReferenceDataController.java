package com.kt.ibs.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.ibs.controllers.vo.FinancialInstitutionSettings;
import com.kt.ibs.controllers.vo.SelectData;
import com.kt.ibs.entity.BankBranch;
import com.kt.ibs.service.ReferenceDataService;

@RestController
public class ReferenceDataController {

    @Autowired
    private ReferenceDataService referenceDataService;

    @RequestMapping(value = "/ibs/api/{country}/banks", method = RequestMethod.GET)
    @ResponseBody
    public List<SelectData> fetchBanks(@PathVariable("country") final String country) {
        return referenceDataService.fetchBanks(country);
    }

    @PostMapping(value = "/ibs/api/{country}/banks/{bank}/branches")
    @ResponseBody
    public void addBranch(@PathVariable("country") final String country, @PathVariable("bank") final String bank, @RequestBody final BankBranch branch) {
        referenceDataService.addBranchToBank(country, bank, branch);
    }

    @PostMapping(value = "/ibs/api/default-bank/{bank}/settings")
    @ResponseBody
    public void updateSettings(@PathVariable("bank") final String bank, @RequestBody final FinancialInstitutionSettings settings) {
        referenceDataService.financialInstitutionSettings(bank, settings);
    }

    @RequestMapping(value = "/ibs/api/banks/{bankCode}/branches", method = RequestMethod.GET)
    @ResponseBody
    public List<SelectData> fetchBranches(@PathVariable("bankCode") final String bankCode) {
        return referenceDataService.fetchBranches(bankCode);
    }

    @RequestMapping(value = "/ibs/api/currencies", method = RequestMethod.GET)
    @ResponseBody
    public List<SelectData> fetchCurrencies() {
        return referenceDataService.fetchCurrencies();
    }

    @RequestMapping(value = "/ibs/api/countries", method = RequestMethod.GET)
    @ResponseBody
    public List<SelectData> fetchCountries() {
        return referenceDataService.fetchCountries();
    }

}
