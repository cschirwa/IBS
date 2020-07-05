package com.kt.ibs.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kt.ibs.entity.AuthorityName;
import com.kt.ibs.entity.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {

    public List<Session> findByUserUsername(final String username);

    public List<Session> findByAuthorityAndLoginTimeGreaterThan(AuthorityName authority, Date start);

}
