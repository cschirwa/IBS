package com.kt.ibs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.ibs.controllers.vo.FinancialInstitutionSettings;
import com.kt.ibs.controllers.vo.SelectData;
import com.kt.ibs.entity.Bank;
import com.kt.ibs.entity.BankBranch;
import com.kt.ibs.entity.FinancialInstitution;
import com.kt.ibs.repository.BankBranchRepository;
import com.kt.ibs.repository.BankRepository;
import com.kt.ibs.repository.CountryRepository;
import com.kt.ibs.repository.CurrencyRepository;
import com.kt.ibs.repository.FinancialInstitutionRepository;

@Service
public class ReferenceDataService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankBranchRepository bankBranchRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private FinancialInstitutionRepository financialInstitutionRepository;

    public void financialInstitutionSettings(final String name, final FinancialInstitutionSettings financialInstitution) {
        FinancialInstitution institution = financialInstitutionRepository.findByName(name);
        institution.setChargeType(financialInstitution.getChargeType());
        financialInstitutionRepository.save(institution);
    }


    public void addBranchToBank(final String countryCode, final String bankCode, final BankBranch branch) {
        Bank bank = bankRepository.findByCountryCodeAndBankCode(countryCode, bankCode);
        branch.setBank(bank);
        bankBranchRepository.save(branch);
    }
    public List<SelectData> fetchBanks(final String countryCode) {
        // FinancialInstitution financialInstitution = financialInstitutionRepository.findAll().get(0);
        return bankRepository.findByCountryCode(countryCode).stream()
                //.map(bank -> new SelectData(bank.getBankCode(), bank.getBankName(), financialInstitution.getName().equals(bank.getBankName())))
                .map(SelectData::new)
                .sorted((a, b) -> a.getText().compareTo(b.getText()))
                .collect(Collectors.toList());

    }

    public List<SelectData> fetchCurrencies() {
        FinancialInstitution financialInstitution = financialInstitutionRepository.findAll().get(0);
        return currencyRepository.findAll().stream()
                .map(currency -> new SelectData(currency.getCode(), currency.getDescription(), financialInstitution.getDefaultCurrency().getCode().equals(currency.getCode())))
                .collect(Collectors.toList());

    }

    public List<SelectData> fetchCountries() {
        FinancialInstitution financialInstitution = financialInstitutionRepository.findAll().get(0);
        return countryRepository.findAll().stream()
                .map(country -> new SelectData(country.getCode(), country.getDescription(), financialInstitution.getCountry().getCode().equals(country.getCode())))
                .collect(Collectors.toList());

    }

    public List<SelectData> fetchBranches(final String bankCode) {
        return bankBranchRepository.findByBankBankCode(bankCode).stream()
                .map(SelectData::new)
                .collect(Collectors.toList());

    }

}
