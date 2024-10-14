package com.custis.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final List<String> receivedMessages = new ArrayList<>();

    @KafkaListener(topics = "course_enrollment", groupId = "enrollment_group")
    public void listen(String message) {
        logger.info("Received message: {}", message);
        receivedMessages.add(message);
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }

    public void clearMessages() {
        receivedMessages.clear();
    }
}
