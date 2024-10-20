package com.custis.university.controller;

import com.custis.university.dto.CourseDTO;
import com.custis.university.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}")
    public CourseDTO getCourseById(@PathVariable int courseId) {
        return courseService.getCourseById(courseId);
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO course) {
        CourseDTO createdCourse = courseService.createCourse(course);
        logger.info("Course created: {}", createdCourse.getId());
        return ResponseEntity.status(201).body(createdCourse);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable int courseId, @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(courseId, courseDTO);
        logger.info("Course updated: {}", courseId);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int courseId) {
        courseService.deleteCourse(courseId);
        logger.info("Course deleted: {}", courseId);
        return ResponseEntity.noContent().build();
    }
}


////    дан массив цен a = [10, 20, 3, 2, 1]
////    дано k = 4 - число скидочных купонов
////    дано x = 7 - размер скидки
////    result: [3, 0, 3, 2, 1]
////    написать метод считающий минимальную сумму цен после применения купонов
////    купон применяется целиком:
////    1) если к 10 применить купон, то цена станет 10 - 7 = 3
////    2) если к 2 применить купон цена = 0
//
//
//public int minPrice(int[] prices, int k, int x) {
//
//    // do something
//
//}