package com.custis.studentscourse.controller;

import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return enrollmentService.getAllCourses();
    }

    @GetMapping("/courses/{courseId}")
    public Course getCourseById(@PathVariable int courseId) {
        return enrollmentService.getCourseById(courseId);
    }

    @PostMapping()
    public ResponseEntity<String> enrollStudent(@RequestParam int studentId, @RequestParam int courseId) {
        enrollmentService.enrollStudent(studentId, courseId);
        return ResponseEntity.status(201).body("Enrollment successful");
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable int enrollmentId) {
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.noContent().build();
    }
}
