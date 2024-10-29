package com.custis.university.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String STUDENT_QUEUE_NAME= "studentQueue";

    @Bean
    public Queue studentQueue() {
        return new Queue(STUDENT_QUEUE_NAME,true);
    }
}
