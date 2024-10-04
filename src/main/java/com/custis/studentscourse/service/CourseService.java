package com.custis.studentscourse.service;

import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course enrollStudent(int courseId, Student student) throws Exception {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception("Course not Found"));

        if (LocalDateTime.now().isBefore(course.getEnrollmentStart()) ||
                LocalDateTime.now().isAfter(course.getEnrollmentEnd())) {
            throw new Exception("Enrollment is not open for this course");
        }

        if (course.getOccupiedSeats() >= course.getTotalSeats()) {
            throw new Exception("No available seats");
        }

        course.setOccupiedSeats(course.getOccupiedSeats() + 1);
        return courseRepository.save(course);
    }
}
