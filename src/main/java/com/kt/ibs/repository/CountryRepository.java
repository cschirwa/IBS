package com.kt.ibs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ibs.entity.Country;

@Transactional
public interface CountryRepository extends CrudRepository<Country, Long> {

    public Country findByCode(String code);

    @Override
    public List<Country> findAll();

}
