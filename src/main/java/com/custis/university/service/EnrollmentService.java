package com.custis.university.service;

import com.custis.university.dto.CourseDTO;
import com.custis.university.dto.EnrollmentDTO;
import com.custis.university.dto.StudentDTO;
import com.custis.university.exception.course.CourseNotFoundException;
import com.custis.university.exception.course.OccupiedSeatsException;
import com.custis.university.exception.enrollment.DuplicateEnrollmentException;
import com.custis.university.exception.enrollment.EnrollmentNotFoundException;
import com.custis.university.exception.enrollment.EnrollmentNotOpenException;
import com.custis.university.exception.student.StudentNotFoundException;
import com.custis.university.mapper.CourseMapper;
import com.custis.university.mapper.EnrollmentMapper;
import com.custis.university.model.Course;
import com.custis.university.model.Enrollment;
import com.custis.university.model.Student;
import com.custis.university.repository.postgres.CourseRepository;
import com.custis.university.repository.postgres.EnrollmentRepository;
import com.custis.university.repository.postgres.StudentRepository;
import com.custis.university.service.rabbit.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(CourseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        CourseDTO courseDTO = CourseMapper.toDTO(course);
        return courseDTO;
    }

    @Transactional
    public EnrollmentDTO enrollStudent(StudentDTO student, CourseDTO course) {
        Student foundStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        Course foundCourse = courseRepository.findById(course.getId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        if (ZonedDateTime.now().isBefore(foundCourse.getEnrollmentStart()) || ZonedDateTime.now().isAfter(foundCourse.getEnrollmentEnd())) {
            throw new EnrollmentNotOpenException("Enrollment window is closed");
        }

        if (course.getOccupiedSeats() >= foundCourse.getTotalSeats()) {
            throw new OccupiedSeatsException("No available seats");
        }

        if (enrollmentRepository.existsByStudentIdAndCourseId(foundStudent.getId(), foundCourse.getId())) {
            throw new DuplicateEnrollmentException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(foundStudent);
        enrollment.setCourse(foundCourse);
        enrollment.setStudentName(foundStudent.getName());
        enrollment.setCourseName(foundCourse.getName());
        enrollmentRepository.save(enrollment);

        foundCourse.setOccupiedSeats(foundCourse.getOccupiedSeats() + 1);
        courseRepository.save(foundCourse);

        messageProducer.sendEnrollmentMessage(EnrollmentMapper.toDTO(enrollment));
        logger.info("Student {} enrolled in course {}", foundStudent.getName(), foundCourse.getName());

        return EnrollmentMapper.toDTO(enrollment);
    }

    @Transactional
    public void deleteEnrollment(int enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new EnrollmentNotFoundException("Enrollment not found");
        }
        enrollmentRepository.deleteById(enrollmentId);
        logger.info("Enrollment with ID {} deleted", enrollmentId);
    }
}
