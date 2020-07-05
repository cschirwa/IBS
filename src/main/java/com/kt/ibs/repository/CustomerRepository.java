package com.kt.ibs.repository;

import com.kt.ibs.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByUuid(String uuid);
}
