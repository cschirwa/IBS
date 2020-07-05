package com.kt.ibs.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kt.ibs.entity.CustomerAudit;

public interface CustomerAuditRepository extends CrudRepository<CustomerAudit, Long> {

    @Override
    public List<CustomerAudit> findAll();

    public List<CustomerAudit> findByUsername(final String username);

    public List<CustomerAudit> findByUsernameAndEventType(final String username, final String eventType);

    public List<CustomerAudit> findByEventType(final String eventType);

    public List<CustomerAudit> findByEventDateGreaterThan(Date date);
}
