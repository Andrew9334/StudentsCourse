package com.custis.university.service;

import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.errors.RecordTooLargeException;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                if (exception instanceof SerializationException) {
                    logger.error("Serialization error while sending message to topic {}: {}", topic, exception.getMessage());
                } else if (exception instanceof TimeoutException) {
                    logger.error("Timeout while sending message to topic {}: {}", topic, exception.getMessage());
                } else if (exception instanceof ProducerFencedException) {
                    logger.error("Producer fenced error for topic {}: {}", topic, exception.getMessage());
                } else if (exception instanceof AuthorizationException) {
                    logger.error("Authorization error for topic {}: {}", topic, exception.getMessage());
                } else if (exception instanceof RecordTooLargeException) {
                    logger.error("Message too large for topic {}: {}", topic, exception.getMessage());
                } else {
                    logger.error("Failed to send message to topic{}: {}", topic, exception.getMessage());
                }
            } else {
                logger.info("Message sent successfully: {}", result.getProducerRecord().value());
            }
        });
    }
}
