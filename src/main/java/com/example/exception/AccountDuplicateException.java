package com.example.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class AccountDuplicateException extends Exception {
    public AccountDuplicateException(DataIntegrityViolationException e) {
        super(e);
    }
}
