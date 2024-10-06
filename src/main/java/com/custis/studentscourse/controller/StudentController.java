package com.custis.studentscourse.controller;

import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        student = studentService.createStudent(student);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<Set<Course>> getStudentCourses(@PathVariable int id) {
        Set<Course> courses = studentService.getStudentCourses(id);
        return ResponseEntity.ok(courses);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
}
