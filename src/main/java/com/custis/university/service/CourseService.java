package com.custis.university.service;

import com.custis.university.dto.CourseDTO;
import com.custis.university.exception.course.CourseNotFoundException;
import com.custis.university.mapper.CourseMapper;
import com.custis.university.model.Course;
import com.custis.university.repository.postgres.CourseRepository;
import com.custis.university.service.rabbit.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;
    private final RedisService redisService;
    private final MessageProducer messageProducer;

    public CourseService(CourseRepository courseRepository, RedisService redisService, MessageProducer messageProducer) {
        this.courseRepository = courseRepository;
        this.redisService = redisService;
        this.messageProducer = messageProducer;
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(int courseId) {
        String cacheKey = "course " + courseId;
        CourseDTO cacheCourse = redisService.getCachedValue(cacheKey, CourseDTO.class);

        if (cacheCourse != null) {
            logger.info("Course fetched from cache {}", cacheKey);
            return cacheCourse;
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        CourseDTO courseDTO = CourseMapper.toDTO(course);
        redisService.cacheValue(cacheKey, course);
        logger.info("Course cached {}", cacheKey);
        return courseDTO;
    }

    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = CourseMapper.toEntity(courseDTO);
        Course savedCourse = courseRepository.save(course);
        CourseDTO savedCourseDTO = CourseMapper.toDTO(savedCourse);
        redisService.cacheValue("course:" + savedCourseDTO.getId(), savedCourseDTO);
        messageProducer.sendCourseMessage(savedCourseDTO);
        logger.info("Course created and cached: {}", savedCourseDTO.getId());
        return savedCourseDTO;
    }

    @Transactional
    public CourseDTO updateCourse(int courseId, CourseDTO courseDetails) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        course.setName(courseDetails.getName());
        course.setTotalSeats(courseDetails.getTotalSeats());
        course.setOccupiedSeats(courseDetails.getOccupiedSeats());
        course.setEnrollmentStart(courseDetails.getEnrollmentStart());
        course.setEnrollmentEnd(courseDetails.getEnrollmentEnd());
        CourseDTO updatedCourseDTO = CourseMapper.toDTO(courseRepository.save(course));
        redisService.cacheValue("course:" + courseId, course);
        messageProducer.sendCourseMessage(updatedCourseDTO);
        logger.info("Course updated and cached: {}", courseId);
        return updatedCourseDTO;
    }

    @Transactional
    public void deleteCourse(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        courseRepository.delete(course);
        redisService.deleteCacheValue("course:" + courseId);
        CourseDTO courseDTO = CourseMapper.toDTO(course);
        courseDTO.setAction("delete");
        messageProducer.sendCourseMessage(CourseMapper.toDTO(course));
        logger.info("Course deleted and removed from cache: {}", courseId);
    }

    public void createOrUpdateCourse(CourseDTO courseDTO) {
        if (courseDTO.getId() == null) {
            createCourse(courseDTO);
        } else {
            updateCourse(courseDTO.getId(), courseDTO);
        }
    }
}
