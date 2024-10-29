package com.custis.university.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String ENROLLMENT_QUEUE_NAME = "enrollmentQueue";

    @Bean
    public Queue enrollmentQueue() {
        return new Queue(ENROLLMENT_QUEUE_NAME, true);
    }
}
