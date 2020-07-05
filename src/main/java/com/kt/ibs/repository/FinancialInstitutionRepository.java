package com.kt.ibs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kt.ibs.entity.FinancialInstitution;

public interface FinancialInstitutionRepository extends CrudRepository<FinancialInstitution, Long> {

    FinancialInstitution findByName(String name);

    @Override
    List<FinancialInstitution> findAll();

}
