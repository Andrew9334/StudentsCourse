package com.custis.university.service;

import com.custis.university.exception.course.CourseNotFoundException;
import com.custis.university.model.Course;
import com.custis.university.repository.CourseRepository;
import com.custis.university.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Course updateCourse(int courseId, Course courseDetails) {
        Course course = getCourseById(courseId);
        course.setName(courseDetails.getName());
        course.setTotalSeats(courseDetails.getTotalSeats());
        course.setOccupiedSeats(courseDetails.getOccupiedSeats());
        course.setEnrollmentStart(courseDetails.getEnrollmentStart());
        course.setEnrollmentEnd(courseDetails.getEnrollmentEnd());
        return courseRepository.save(course);
    }

    @Transactional
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(int courseId) {
        Course course = getCourseById(courseId);
        courseRepository.delete(course);
    }
}
