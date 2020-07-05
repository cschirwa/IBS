package com.kt.ibs.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ibs.entity.Account;
import com.kt.ibs.entity.Customer;
import com.kt.ibs.entity.TransactionStatus;
import com.kt.ibs.entity.Transfer;

@Transactional
public interface TransferRepository extends CrudRepository<Transfer, Long> {

    public Transfer findByUuid(final String uuid);

    @Override
    public List<Transfer> findAll();

    public List<Transfer> findByTransactionStatusAndProcessingDateGreaterThan(TransactionStatus status, Date date);

    public List<Transfer> findByCustomer(final Customer customer);

    public List<Transfer> findByCustomerUsername(final String username);

    public List<Transfer> findByCustomerUsernameAndBeneficiaryUuid(final String username, final String uuid);

    public List<Transfer> findByCustomerAndTransactionStatus(final Customer customer, TransactionStatus transactionStatus);

    public List<Transfer> findByCustomerAndSourceAccountAndCoreReference(final Customer customer, final Account account, final String coreReference);

    List<Transfer> findFirstByBeneficiaryUuidAndTransactionStatusOrderByProcessingDateDesc(final String beneficiaryUuid, TransactionStatus transactionStatus);

    List<Transfer> findFirstByCustomerUsernameAndTransactionStatusOrderByProcessingDateDesc(final String username, TransactionStatus transactionStatus);
}
