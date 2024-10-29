package com.custis.university.rabbit;

import com.custis.university.config.RabbitConfig;
import com.custis.university.dto.CourseDTO;
import com.custis.university.exception.CourseNotFoundException;
import com.custis.university.service.CourseService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CourseConsumer {

    private final CourseService courseService;

    public CourseConsumer(CourseService courseService) {
        this.courseService = courseService;
    }

    @RabbitListener(queues = RabbitConfig.COURSE_QUEUE_NAME)
    public void receiveCourseMessage(CourseDTO courseDTO) {
        try {
            courseService.createOrUpdateCourse(courseDTO);
        } catch (CourseNotFoundException e) {
            throw new CourseNotFoundException("Course not found " + e);
        }
    }
}
