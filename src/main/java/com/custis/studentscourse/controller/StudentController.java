package com.custis.studentscourse.controller;

import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable int studentId) {
        Student student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.status(201).body(createdStudent);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable int studentId,@Valid @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(studentId, student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}
