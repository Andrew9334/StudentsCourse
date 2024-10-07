package com.custis.studentscourse.service;

import com.custis.studentscourse.exception.course.CourseNotFoundException;
import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.repository.CourseRepository;
import com.custis.studentscourse.repository.StudentRepository;
import org.springframework.stereotype.Service;

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

    public Course getCourseById(int courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
    }

    public Course updateCourse(int courseId, Course courseDetails) {
        Course course = getCourseById(courseId);
        course.setName(courseDetails.getName());
        course.setTotalSeats(courseDetails.getTotalSeats());
        course.setOccupiedSeats(courseDetails.getOccupiedSeats());
        course.setEnrollmentStart(courseDetails.getEnrollmentStart());
        course.setEnrollmentEnd(courseDetails.getEnrollmentEnd());
        return courseRepository.save(course);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(int courseId) {
        Course course = getCourseById(courseId);
        courseRepository.delete(course);
    }
}
