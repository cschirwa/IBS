package com.kt.ibs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kt.ibs.entity.CustomerAuthorization;

public interface CustomerAuthorizationRepository extends CrudRepository<CustomerAuthorization, Long> {

    @Override
    List<CustomerAuthorization> findAll();

}
