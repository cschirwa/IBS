package com.kt.ibs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.ibs.entity.Authority;
import com.kt.ibs.entity.AuthorityName;


public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(AuthorityName name);

}
