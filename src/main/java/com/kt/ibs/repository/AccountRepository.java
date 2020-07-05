package com.kt.ibs.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ibs.entity.Account;
import com.kt.ibs.entity.Currency;
import com.kt.ibs.entity.Customer;

@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {


    public List<Account> findByCustomer(final Customer customer);

    public Account findByCustomerAndCurrency(final Customer customer, final Currency currency);

    public List<Account> findByCustomerUsername(final String username);

    public Account findByUsernameAndAccountNumber(final String username, final String accountNumber);
}
