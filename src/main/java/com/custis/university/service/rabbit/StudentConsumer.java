package com.custis.university.service.rabbit;

import com.custis.university.config.RabbitConfig;
import com.custis.university.dto.StudentDTO;
import com.custis.university.exception.student.StudentNotFoundException;
import com.custis.university.service.StudentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StudentConsumer {

    private final StudentService studentService;

    public StudentConsumer(StudentService studentService) {
        this.studentService = studentService;
    }

    @RabbitListener(queues = RabbitConfig.STUDENT_QUEUE_NAME)
    public void receiveStudentMessage(StudentDTO studentDTO) {
        try {
            studentService.createOrUpdateStudent(studentDTO);
        } catch (StudentNotFoundException e) {
            throw new StudentNotFoundException("Student not found " + e);
        }
    }
}
