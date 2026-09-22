package com.example.controller;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AccountDuplicateException;
import com.example.exception.AccountConstraintException;
import com.example.exception.MessageConstraintException;
import com.example.exception.MessageBlankException;
import com.example.exception.AccountAuthenticationException;
import com.example.service.AccountService;
import com.example.service.MessageService;

@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(
            AccountService accountService,
            MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @ExceptionHandler(AccountDuplicateException.class)
    private ResponseEntity<Void> accountDuplicateException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(AccountAuthenticationException.class)
    private ResponseEntity<Void> accountUnauthorizedException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler({
            AccountConstraintException.class,
            MessageBlankException.class,
            MessageConstraintException.class,
            NoSuchElementException.class,
            DataIntegrityViolationException.class
    })
    private ResponseEntity<Void> accountException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    private ResponseEntity<Void> deleteException() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    private ResponseEntity<Account> register(
            @RequestBody Account account)
            throws AccountConstraintException, AccountDuplicateException {
        return ResponseEntity.ok(
                accountService.register(
                        account.getUsername(),
                        account.getPassword()));
    }

    @PostMapping("/login")
    private ResponseEntity<Account> login(
            @RequestBody Account account)
            throws AccountAuthenticationException {
        return ResponseEntity.ok(
                accountService.login(
                        account.getUsername(),
                        account.getPassword()));
    }

    @PostMapping("/messages")
    private ResponseEntity<Message> createMessage(
            @RequestBody Message message)
            throws MessageBlankException, MessageConstraintException {
        return ResponseEntity.ok(
                messageService.create(
                        message.getPostedBy(),
                        message.getMessageText(),
                        message.getTimePostedEpoch()));
    }

    @GetMapping("/messages")
    private ResponseEntity<Iterable<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAll());
    }

    @GetMapping("/messages/{messageId}")
    private ResponseEntity<Message> getAllMessagesById(
            @PathVariable int messageId) {
        return ResponseEntity.ok(
                messageService.getById(messageId).orElse(null));
    }

    @DeleteMapping("/messages/{messageId}")
    private ResponseEntity<Integer> deleteMessage(
            @PathVariable int messageId) {
        messageService.delete(messageId);
        return ResponseEntity.ok(1);
    }

    @PatchMapping("/messages/{messageId}")
    private ResponseEntity<Integer> updateMessage(
            @PathVariable int messageId, @RequestBody Message message)
            throws MessageBlankException {
        messageService.update(messageId, message.getMessageText());
        return ResponseEntity.ok(1);
    }

    @GetMapping("/accounts/{accountId}/messages")
    private ResponseEntity<Iterable<Message>> accountMessages(
            @PathVariable int accountId) {
        return ResponseEntity.ok(messageService.findAllByPostedBy(accountId));
    }
}