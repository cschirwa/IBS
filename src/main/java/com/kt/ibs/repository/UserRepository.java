package com.kt.ibs.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kt.ibs.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUuid(String uuid);
}
