package com.example.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByUsernameAndPassword(String username, String password);
}
