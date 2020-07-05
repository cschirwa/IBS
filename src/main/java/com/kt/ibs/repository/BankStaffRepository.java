package com.kt.ibs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.kt.ibs.entity.BankStaff;

public interface BankStaffRepository extends CrudRepository<BankStaff, Long> {

    Optional<BankStaff> findByUsername(String username);

    Optional<BankStaff> findByUuid(String uuid);

    List<BankStaff> findByFinancialInstitutionNameAndEnabled(String bank, boolean active, Pageable page);

}
