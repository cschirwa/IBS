package com.kt.ibs.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ibs.entity.Currency;

@Transactional
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    public Currency findByCode(String code);

    @Override
    public List<Currency> findAll();

}
