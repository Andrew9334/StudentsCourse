package com.custis.university.controller;

import com.custis.university.dto.StudentDTO;
import com.custis.university.model.Student;
import com.custis.university.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable int studentId) {
        StudentDTO student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO student) {
        StudentDTO createdStudent = studentService.createStudent(student);
        logger.info("Student created: {}", createdStudent.getId());
        return ResponseEntity.status(201).body(createdStudent);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable int studentId, @Valid @RequestBody StudentDTO student) {
        StudentDTO updatedStudent = studentService.updateStudent(studentId, student);
        logger.info("Student updated: {}", studentId);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int studentId) {
        studentService.deleteStudent(studentId);
        logger.info("Student deleted: {}", studentId);
        return ResponseEntity.noContent().build();
    }
}
