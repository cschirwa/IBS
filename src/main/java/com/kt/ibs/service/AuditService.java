package com.kt.ibs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kt.ibs.entity.CustomerAudit;
import com.kt.ibs.repository.CustomerAuditRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditService {

    @Autowired
    private CustomerAuditRepository customerAuditRepository;

    @EventListener
    public void persist(final CustomerAudit audit) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            audit.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        customerAuditRepository.save(audit);
    }
}
