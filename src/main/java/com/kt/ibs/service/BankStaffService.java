package com.kt.ibs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.ibs.controllers.vo.BankStaffDetails;
import com.kt.ibs.controllers.vo.BankStaffOnboarding;
import com.kt.ibs.entity.AuthorityName;
import com.kt.ibs.entity.BankStaff;
import com.kt.ibs.repository.AuthorityRepository;
import com.kt.ibs.repository.BankStaffRepository;
import com.kt.ibs.repository.FinancialInstitutionRepository;

@Service
public class BankStaffService {

    @Autowired
    private BankStaffRepository bankStaffRepository;

    @Autowired
    private FinancialInstitutionRepository financialInstitutionRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    public List<BankStaffDetails> fetchBankStaff(final String bank) {
        return bankStaffRepository.findByFinancialInstitutionNameAndEnabled(bank, true, null).stream()
                .map(staff -> new BankStaffDetails(staff.getUsername(), staff.getUuid(), staff.getFullname(), bank, staff.getLastLoginTime()))
                .collect(Collectors.toList());

    }

    public BankStaff addStaffMember(final BankStaffOnboarding details) {
        return bankStaffRepository.save(new BankStaff(details.getUsername(), details.getPassword(), financialInstitutionRepository.findByName(details.getBank()), authorityRepository.findByName(
                AuthorityName.ROLE_ADMIN)));

    }
}
