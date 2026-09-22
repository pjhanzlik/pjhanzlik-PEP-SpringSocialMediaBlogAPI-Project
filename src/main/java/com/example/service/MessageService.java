package com.example.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.MessageConstraintException;
import com.example.exception.MessageBlankException;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message create(
            int postedBy, String messageText, long getTimePostedEpoch)
            throws MessageBlankException, MessageConstraintException {
        // These checks should really be done by database constraints...
        if (messageText == null || messageText == "") {
            throw new MessageBlankException();
        }

        try {
            return messageRepository.save(
                    new Message(
                            postedBy,
                            messageText,
                            getTimePostedEpoch));
        } catch (DataIntegrityViolationException e) {
            throw new MessageConstraintException(e);
        }

    }

    public Iterable<Message> getAll() {
        return messageRepository.findAll();
    }

    public Optional<Message> getById(int id) {
        return messageRepository.findById(id);
    }

    public void delete(int id) {
        messageRepository.deleteById(id);
    }

    public void update(int id, String messageText) throws MessageBlankException {
        // These checks should really be done by database constraints...
        if (messageText == null || messageText == "") {
            throw new MessageBlankException();
        }

        messageRepository.save(messageRepository.findById(id).map(m -> {
            m.setMessageText(messageText);
            return m;
        }).get());
    }

    public Iterable<Message> findAllByPostedBy(int postedBy) {
        return messageRepository.findAllByPostedBy(postedBy);
    }
}
