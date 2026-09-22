package com.example.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class MessageConstraintException extends Exception {

    public MessageConstraintException(DataIntegrityViolationException e) {
        super(e);
    }
}
