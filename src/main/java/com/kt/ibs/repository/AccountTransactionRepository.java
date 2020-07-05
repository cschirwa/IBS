package com.kt.ibs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kt.ibs.entity.AccountTransaction;

public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, Long> {

    public List<AccountTransaction> findFirst10ByAccountAccountNumberOrderByCreatedTimeDesc(String accountNumer);

    public List<AccountTransaction> findByCustomerUsernameAndAccountAccountNumber(String username, String accountNumer);

    public List<AccountTransaction> findByCustomerUsernameAndAccountAccountNumberAndReference(String username, String accountNumber, String reference);
}
