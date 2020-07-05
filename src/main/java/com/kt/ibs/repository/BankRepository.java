package com.kt.ibs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ibs.entity.Bank;

@Transactional
public interface BankRepository extends CrudRepository<Bank, Long> {

    @Override
    public List<Bank> findAll();

    public List<Bank> findByCountryCode(String countryCode);

    Bank findByCountryCodeAndBankCode(String countryCode, String bankCode);

}
