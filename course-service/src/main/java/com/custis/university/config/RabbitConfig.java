package com.custis.university.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String COURSE_QUEUE_NAME = "courseQueue";

    @Bean
    public Queue courseQueue() {
        return new Queue(COURSE_QUEUE_NAME, true);
    }
}
