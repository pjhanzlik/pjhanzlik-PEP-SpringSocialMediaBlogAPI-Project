package com.example.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.AccountDuplicateException;
import com.example.exception.AccountConstraintException;
import com.example.exception.AccountAuthenticationException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(String username, String password) throws AccountConstraintException, AccountDuplicateException {
        // The following check should be done by the database schema,
        // but we have to do it in Java per trainer request.
        if (username == null || username == "" ||
                password == null ||
                password.length() < 4) {
            throw new AccountConstraintException();
        }

        try {
            return accountRepository.save(new Account(username, password));
        } catch (DataIntegrityViolationException e) {
            throw new AccountDuplicateException(e);
        }
    }

    public Account login(String username, String password) throws AccountAuthenticationException {
        return accountRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(AccountAuthenticationException::new);
    }
}
