package com.kt.ibs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ibs.entity.Beneficiary;
import com.kt.ibs.entity.Customer;

@Transactional
public interface BeneficiaryRepository extends CrudRepository<Beneficiary, Long> {

    public Optional<Beneficiary> findById(Long id);

    public Beneficiary findByCustomerUsernameAndUuid(final String username, final String uuid);

    public List<Beneficiary> findByCustomer(final Customer customer);

    public List<Beneficiary> findByCustomerAndActive(final Customer customer, final boolean active);
}
