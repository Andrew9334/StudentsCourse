package com.custis.studentscourse.service;

import com.custis.studentscourse.exception.course.EnrollmentIsNotOpenException;
import com.custis.studentscourse.exception.course.CourseNotFoundException;
import com.custis.studentscourse.exception.course.OccupiedSeatsException;
import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.repository.CourseRepository;
import com.custis.studentscourse.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course enrollStudent(int courseId, Student student) throws Exception {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not Found"));

        if (ZonedDateTime.now().isBefore(course.getEnrollmentStart()) ||
                ZonedDateTime.now().isAfter(course.getEnrollmentEnd())) {
            throw new EnrollmentIsNotOpenException("Enrollment is not open for this course");
        }

        if (course.getOccupiedSeats() >= course.getTotalSeats()) {
            throw new OccupiedSeatsException("No available seats");
        }

        course.setOccupiedSeats(course.getOccupiedSeats() + 1);
        return courseRepository.save(course);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
}
