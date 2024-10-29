package com.custis.university.controller;

import com.custis.university.dto.StudentDTO;
import com.custis.university.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Flux<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{studentId}")
    public Mono<ResponseEntity<StudentDTO>> getStudentById(@PathVariable int studentId) {
        return studentService.getStudentById(studentId)
                .map(studentDTO -> {
                    logger.info("Student fetched: {}", studentId);
                    return ResponseEntity.ok(studentDTO);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<StudentDTO>> createStudent(@Valid @RequestBody StudentDTO student) {
        return studentService.createStudent(student)
                .map(createdStudent -> ResponseEntity.status(201).body(createdStudent));
    }

    @PutMapping("/{studentId}")
    public Mono<ResponseEntity<StudentDTO>> updateStudent(@PathVariable int studentId, @Valid @RequestBody StudentDTO student) {
        return studentService.updateStudent(studentId, student)
                .doOnNext(updatedStudent -> logger.info("Student updated: {}", studentId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{studentId}")
    public Mono<ResponseEntity<Void>> deleteStudent(@PathVariable int studentId) {
        return studentService.deleteStudent(studentId)
                .doOnSuccess(aVoid -> logger.info("Student deleted: {}", studentId))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
