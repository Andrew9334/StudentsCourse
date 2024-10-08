package com.custis.university.service;

import com.custis.university.exception.course.CourseNotFoundException;
import com.custis.university.exception.course.OccupiedSeatsException;
import com.custis.university.exception.enrollment.EnrollmentNotOpenException;
import com.custis.university.exception.enrollment.EnrollmentNotFoundException;
import com.custis.university.exception.student.StudentNotFoundException;
import com.custis.university.model.Course;
import com.custis.university.model.Enrollment;
import com.custis.university.model.Student;
import com.custis.university.repository.CourseRepository;
import com.custis.university.repository.EnrollmentRepository;
import com.custis.university.repository.StudentRepository;
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
    public void enrollStudent(Student student, Course course) {
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

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(foundStudent);
        enrollment.setCourse(foundCourse);
        enrollment.setStudentName(foundStudent.getName());
        enrollment.setCourseName(foundCourse.getName());
        enrollmentRepository.save(enrollment);
        course.setOccupiedSeats(foundCourse.getOccupiedSeats() + 1);
        courseRepository.save(foundCourse);
    }

    @Transactional
    public void deleteEnrollment(int enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }
}
