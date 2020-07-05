package com.kt.ibs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ibs.entity.BankBranch;

@Transactional
public interface BankBranchRepository extends CrudRepository<BankBranch, Long> {

    public List<BankBranch> findByBankBankCodeAndBranchCode(final String bankCode, final String branchCode);

    public List<BankBranch> findByBankBankCode(String bankCode);

}
