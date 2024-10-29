package com.custis.university.controller;

import com.custis.university.dto.CourseDTO;
import com.custis.university.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public Flux<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}")
    public Mono<ResponseEntity<CourseDTO>> getCourseById(@PathVariable int courseId) {
        return courseService.getCourseById(courseId)
                .map(courseDTO -> {
                    logger.info("Course fetched: {}", courseId);
                    return ResponseEntity.ok(courseDTO);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<CourseDTO>> createCourse(@Valid @RequestBody CourseDTO course) {
        return courseService.createCourse(course)
                .map(createdCourse -> ResponseEntity.status(201).body(createdCourse));
    }

    @PutMapping("/{courseId}")
    public Mono<ResponseEntity<CourseDTO>> updateCourse(@PathVariable int courseId, @Valid @RequestBody CourseDTO courseDTO) {
        return courseService.updateCourse(courseId, courseDTO)
                .doOnNext(updatedCourse -> logger.info("Course updated: {}", courseId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{courseId}")
    public Mono<ResponseEntity<Void>> deleteCourse(@PathVariable int courseId) {
        return courseService.deleteCourse(courseId)
                .doOnSuccess(aVoid -> logger.info("Course deleted: {}", courseId))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}