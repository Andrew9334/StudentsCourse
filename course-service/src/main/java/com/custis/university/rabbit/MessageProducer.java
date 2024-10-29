package com.custis.university.rabbit;

import com.custis.university.config.RabbitConfig;
import com.custis.university.dto.CourseDTO;
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

    public void sendCourseMessage(CourseDTO courseDTO) {
        rabbitTemplate.convertAndSend(RabbitConfig.COURSE_QUEUE_NAME, courseDTO);
        logger.info("Sent course message: {}", courseDTO);
    }
}
