package com.custis.studentscourse.controller;

import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<String> enrollStudent(@PathVariable int courseId, @RequestBody Student student) {
        try {
            courseService.enrollStudent(courseId, student);
            return ResponseEntity.ok("Student enrolled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.status(201).body(createdCourse);
    }
}
