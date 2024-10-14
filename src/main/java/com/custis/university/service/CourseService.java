package com.custis.university.service;

import com.custis.university.exception.course.CourseNotFoundException;
import com.custis.university.exception.course.OccupiedSeatsException;
import com.custis.university.exception.course.TotalSeatsException;
import com.custis.university.model.Course;
import com.custis.university.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;
    private final RedisService redisService;

    public CourseService(CourseRepository courseRepository, RedisService redisService) {
        this.courseRepository = courseRepository;
        this.redisService = redisService;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


    public Course getCourseById(int courseId) {
        String cacheKey = "course " + courseId;
        Course cacheCourse = redisService.getCachedValue(cacheKey, Course.class);

        if (cacheCourse != null) {
            logger.info("Course fetched from cache {}", cacheKey);
            return cacheCourse;
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        redisService.cacheValue(cacheKey, course);
        logger.info("Course cached {}", cacheKey);
        return course;
    }

    @Transactional
    public Course createCourse(Course course) {
        Course savedCourse = courseRepository.save(course);
        redisService.cacheValue("course:" + savedCourse.getId(), savedCourse);
        logger.info("Course created and cached: {}", savedCourse.getId());
        return savedCourse;
    }

    @Transactional
    public Course updateCourse(int courseId, Course courseDetails) {
        Course course = getCourseById(courseId);
        course.setName(courseDetails.getName());
        course.setTotalSeats(courseDetails.getTotalSeats());
        course.setOccupiedSeats(courseDetails.getOccupiedSeats());
        course.setEnrollmentStart(courseDetails.getEnrollmentStart());
        course.setEnrollmentEnd(courseDetails.getEnrollmentEnd());
        redisService.cacheValue("course:" + courseId, course);
        logger.info("Course updated and cached: {}", courseId);
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(int courseId) {
        Course course = getCourseById(courseId);
        courseRepository.delete(course);
        redisService.deleteCacheValue("course:" + courseId);
        logger.info("Course deleted and removed from cache: {}", courseId);
    }
}
