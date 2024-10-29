package com.custis.university.service;

import com.custis.university.dto.CourseDTO;
import com.custis.university.exception.CourseNotFoundException;
import com.custis.university.mapper.CourseMapper;
import com.custis.university.model.Course;
import com.custis.university.rabbit.MessageProducer;
import com.custis.university.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .map(CourseMapper::toDTO);
    }

    public Mono<CourseDTO> getCourseById(int courseId) {
        String cacheKey = "course " + courseId;
        Mono<CourseDTO> cacheCourse = redisService.getCachedValue(cacheKey, CourseDTO.class);

        return cacheCourse.flatMap(courseDTO -> {
            logger.info("Course fetched from cache {}", cacheKey);
            return Mono.just(courseDTO);
        }).switchIfEmpty(courseRepository.findById(courseId)
                .map(CourseMapper::toDTO)
                .doOnNext(courseDTO -> {
                    redisService.cacheValue(cacheKey, courseDTO);
                    logger.info("Course cached {}", cacheKey);
                }).onErrorMap(e -> new CourseNotFoundException("Course not found")));
    }

    public Mono<CourseDTO> createCourse(CourseDTO courseDTO) {
        Course course = CourseMapper.toEntity(courseDTO);
        return courseRepository.save(course)
                .map(CourseMapper::toDTO)
                .doOnNext(savedCourseDTO -> {
                    redisService.cacheValue("course:" + savedCourseDTO.getId(), savedCourseDTO);
                    messageProducer.sendCourseMessage(savedCourseDTO);
                    logger.info("Course created and cached: {}", savedCourseDTO.getId());
                });
    }

    public Mono<CourseDTO> updateCourse(int courseId, CourseDTO courseDetails) {
        return courseRepository.findById(courseId)
                .switchIfEmpty(Mono.error(new CourseNotFoundException("Course not found")))
                .doOnNext(course -> {
                    course.setName(courseDetails.getName());
                    course.setTotalSeats(courseDetails.getTotalSeats());
                    course.setOccupiedSeats(courseDetails.getOccupiedSeats());
                    course.setEnrollmentStart(courseDetails.getEnrollmentStart());
                    course.setEnrollmentEnd(courseDetails.getEnrollmentEnd());
                })
                .flatMap(courseRepository::save)
                .map(CourseMapper::toDTO)
                .doOnNext(updateCourseDTO -> {
                    redisService.cacheValue("course:" + courseId, updateCourseDTO);
                    messageProducer.sendCourseMessage(updateCourseDTO);
                    logger.info("Course updated and cached: {}", courseId);
                });
    }

    public Mono<Void> deleteCourse(int courseId) {
        return courseRepository.findById(courseId)
                .switchIfEmpty(Mono.error(new CourseNotFoundException("Course not found")))
                .flatMap(course -> {
                    redisService.deleteCacheValue("course:" + courseId);
                    messageProducer.sendCourseMessage(CourseMapper.toDTO(course));
                    logger.info("Course deleted and removed from cache: {}", courseId);
                    return courseRepository.delete(course);
                });
    }

    public Mono<Void> createOrUpdateCourse(CourseDTO courseDTO) {
        return (courseDTO.getId() == null)
                ? createCourse(courseDTO).then()
                : updateCourse(courseDTO.getId(), courseDTO).then();
    }
}
