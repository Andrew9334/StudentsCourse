package com.custis.university.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

//    public static final String COURSE_QUEUE_NAME= "courseQueue";
//    public static final String STUDENT_QUEUE_NAME= "studentQueue";
//    public static final String ENROLLMENT_QUEUE_NAME= "enrollmentQueue";
//
//    @Bean
//    public Queue courseQueue() {
//      return new Queue(COURSE_QUEUE_NAME, true);
//    }
//
//    @Bean
//    public Queue studentQueue() {
//        return new Queue(STUDENT_QUEUE_NAME,true);
//    }
//
//    @Bean
//    public Queue enrollmentQueue() {
//        return new Queue(ENROLLMENT_QUEUE_NAME, true);
//    }
}
