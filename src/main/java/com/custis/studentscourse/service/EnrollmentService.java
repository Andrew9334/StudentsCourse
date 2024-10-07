package com.custis.studentscourse.service;

import com.custis.studentscourse.exception.course.CourseNotFoundException;
import com.custis.studentscourse.exception.course.OccupiedSeatsException;
import com.custis.studentscourse.exception.enrollment.EnrollmentNotOpenException;
import com.custis.studentscourse.exception.enrollment.EnrollmentNotFoundException;
import com.custis.studentscourse.exception.student.StudentNotFoundException;
import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.model.Enrollment;
import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.repository.CourseRepository;
import com.custis.studentscourse.repository.EnrollmentRepository;
import com.custis.studentscourse.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(CourseRepository courseRepository, StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(int courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found"));
    }

    @Transactional
    public void enrollStudent(int studentId, int courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        if (ZonedDateTime.now().isBefore(course.getEnrollmentStart()) || ZonedDateTime.now().isAfter(course.getEnrollmentEnd())) {
            throw new EnrollmentNotOpenException("Enrollment window is closed");
        }

        if (course.getOccupiedSeats() >= course.getTotalSeats()) {
            throw new OccupiedSeatsException("No available seats");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollmentRepository.save(enrollment);
        course.setOccupiedSeats(course.getOccupiedSeats() + 1);
        courseRepository.save(course);
    }

    public void deleteEnrollment(int enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }
}
