package com.custis.university.rabbit;

import com.custis.university.config.RabbitConfig;
import com.custis.university.dto.EnrollmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEnrollmentMessage(EnrollmentDTO enrollmentDTO) {
        rabbitTemplate.convertAndSend(RabbitConfig.ENROLLMENT_QUEUE_NAME, enrollmentDTO);
        logger.info("Sent enrollment message to RabbitMQ: {}", enrollmentDTO);
    }
}
