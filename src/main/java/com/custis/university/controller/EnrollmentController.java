package com.custis.university.controller;

import com.custis.university.dto.CourseDTO;
import com.custis.university.dto.StudentDTO;
import com.custis.university.mapper.CourseMapper;
import com.custis.university.mapper.StudentMapper;
import com.custis.university.model.Course;
import com.custis.university.model.Student;
import com.custis.university.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final static Logger logger = LoggerFactory.getLogger(EnrollmentController.class);
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/courses")
    public List<CourseDTO> getAllCourses() {
        return enrollmentService.getAllCourses();
    }

    @GetMapping("/courses/{courseId}")
    public CourseDTO getCourseById(@PathVariable int courseId) {
        return enrollmentService.getCourseById(courseId);
    }

    @PostMapping()
    public ResponseEntity<String> enrollStudent(@RequestParam int studentId, @RequestParam int courseId) {
        Student student = new Student();
        Course course = new Course();
        student.setId(studentId);
        course.setId(courseId);
        StudentDTO studentDTO = StudentMapper.toDTO(student);
        CourseDTO courseDTO = CourseMapper.toDTO(course);
        enrollmentService.enrollStudent(studentDTO, courseDTO);
        logger.info("Enrollment created for student: {}, to course: {}", studentId, courseId);
        return ResponseEntity.status(201).body("Enrollment successful");
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable int enrollmentId) {
        enrollmentService.deleteEnrollment(enrollmentId);
        logger.info("Enrollment deleted: {}", enrollmentId);
        return ResponseEntity.noContent().build();
    }
}
