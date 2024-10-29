package com.custis.university.service;

import com.custis.university.dto.CourseDTO;
import com.custis.university.dto.EnrollmentDTO;
import com.custis.university.dto.StudentDTO;
import com.custis.university.exception.*;
import com.custis.university.mapper.CourseMapper;
import com.custis.university.mapper.EnrollmentMapper;
import com.custis.university.model.Enrollment;
import com.custis.university.rabbit.MessageProducer;
import com.custis.university.repository.CourseRepository;
import com.custis.university.repository.EnrollmentRepository;
import com.custis.university.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Service
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    @Autowired
    private MessageProducer messageProducer;

    public EnrollmentService(CourseRepository courseRepository, StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Flux<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .map(CourseMapper::toDTO);
    }

    public Mono<CourseDTO> getCourseById(int courseId) {
        return courseRepository.findById(courseId)
                .flatMap(course -> Mono.just(CourseMapper.toDTO(course)))
                .switchIfEmpty(Mono.error(new CourseNotFoundException("Course not found")));
    }

    public Mono<EnrollmentDTO> enrollStudent(StudentDTO student, CourseDTO course) {
        return studentRepository.findById(student.getId())
                .switchIfEmpty(Mono.error(new StudentNotFoundException("Student not found")))
                .flatMap(foundStudent ->
                        courseRepository.findById(course.getId())
                                .switchIfEmpty(Mono.error(new CourseNotFoundException("Course not found")))
                                .flatMap(foundCourse -> {
                                    if (ZonedDateTime.now().isBefore(foundCourse.getEnrollmentStart()) ||
                                            ZonedDateTime.now().isAfter(foundCourse.getEnrollmentEnd())) {
                                        return Mono.error(new EnrollmentNotOpenException("Enrollment window is closed"));
                                    }

                                    if (foundCourse.getOccupiedSeats() >= foundCourse.getTotalSeats()) {
                                        return Mono.error(new OccupiedSeatsException("No available seats"));
                                    }

                                    if (enrollmentRepository.existsByStudentIdAndCourseId(foundStudent.getId(), foundCourse.getId())) {
                                        return Mono.error(new DuplicateEnrollmentException("Student is already enrolled in this course"));
                                    }

                                    Enrollment enrollment = new Enrollment();
                                    enrollment.setStudent(foundStudent);
                                    enrollment.setCourse(foundCourse);
                                    enrollment.setStudentName(foundStudent.getName());
                                    enrollment.setCourseName(foundCourse.getName());

                                    return enrollmentRepository.save(enrollment)
                                            .flatMap(savedEnrollment -> {
                                                foundCourse.setOccupiedSeats(foundCourse.getOccupiedSeats() + 1);
                                                return courseRepository.save(foundCourse)
                                                        .doOnSuccess(updatedCourse -> {
                                                            messageProducer.sendEnrollmentMessage(EnrollmentMapper.toDTO(savedEnrollment));
                                                            logger.info("Student {} enrolled in course {}", foundStudent.getName(), foundCourse.getName());
                                                        })
                                                        .then(Mono.just(EnrollmentMapper.toDTO(savedEnrollment)));
                                            });
                                })
                );
    }

    public Mono<Void> deleteEnrollment(int enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .switchIfEmpty(Mono.error(new EnrollmentNotFoundException("Enrollment not found")))
                .flatMap(enrollment -> {
                    logger.info("Enrollment with ID {} deleted", enrollmentId);
                    return studentRepository.deleteById(enrollmentId);
                })
                .doOnSuccess(aVoid -> logger.info("Enrollment with ID {} deleted successfully", enrollmentId));
    }
}
