package com.custis.university.controller;

import com.custis.university.dto.CourseDTO;
import com.custis.university.dto.StudentDTO;
import com.custis.university.exception.EnrollmentNotFoundException;
import com.custis.university.mapper.CourseMapper;
import com.custis.university.mapper.StudentMapper;
import com.custis.university.model.Course;
import com.custis.university.model.Student;
import com.custis.university.repository.CourseRepository;
import com.custis.university.repository.StudentRepository;
import com.custis.university.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final static Logger logger = LoggerFactory.getLogger(EnrollmentController.class);
    private final EnrollmentService enrollmentService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentController(EnrollmentService enrollmentService, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.enrollmentService = enrollmentService;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses")
    public Flux<CourseDTO> getAllCourses() {
        return enrollmentService.getAllCourses();
    }

    @GetMapping("/courses/{courseId}")
    public Mono<ResponseEntity<CourseDTO>> getCourseById(@PathVariable int courseId) {
        return enrollmentService.getCourseById(courseId)
                .map(courseDTO -> {
                    logger.info("Course fetched: {}", courseId);
                    return ResponseEntity.ok(courseDTO);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public Mono<ResponseEntity<String>> enrollStudent(@RequestParam int studentId, @RequestParam int courseId) {
        return studentRepository.findById(studentId)
                .zipWith(courseRepository.findById(courseId))
                .flatMap(tuple -> {
                    Student student = tuple.getT1();
                    Course course = tuple.getT2();
                    StudentDTO studentDTO = StudentMapper.toDTO(student);
                    CourseDTO courseDTO = CourseMapper.toDTO(course);
                    return enrollmentService.enrollStudent(studentDTO, courseDTO)
                            .then(Mono.fromCallable(() -> {
                                logger.info("Enrollment created for student: {}, to course: {}", studentId, courseId);
                                return ResponseEntity.status(201).body("Enrollment successful");
                            }));
                })
                .switchIfEmpty(Mono.error(new EnrollmentNotFoundException("Student or Course not found")));
    }

    @DeleteMapping("/{enrollmentId}")
    public Mono<ResponseEntity<Void>> deleteEnrollment(@PathVariable int enrollmentId) {
        return enrollmentService.deleteEnrollment(enrollmentId)
                .doOnSuccess(aVoid -> logger.info("Enrollment deleted: {}", enrollmentId))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
