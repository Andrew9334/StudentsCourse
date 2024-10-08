package com.custis.university.course;

import com.custis.university.controller.CourseController;
import com.custis.university.model.Course;
import com.custis.university.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CourseServiceTest {

    private CourseService courseService;
    private CourseController courseController;

    @BeforeEach
    public void setUp() {
        courseService = Mockito.mock(CourseService.class);
        courseController = new CourseController(courseService);
    }

    @Test
    public void testGetAllCourses() {
        Course course1 = new Course();
        course1.setId(1);
        course1.setName("Course 1");

        Course course2 = new Course();
        course2.setId(2);
        course2.setName("Course 2");

        List<Course> courses = Arrays.asList(course1, course2);
        when(courseService.getAllCourses()).thenReturn(courses);

        List<Course> result = courseController.getAllCourses();

        assertEquals(2, result.size());
        assertEquals("Course 1", result.get(0).getName());
        assertEquals("Course 2", result.get(1).getName());
    }

    @Test
    public void testGetCourseById() {
        Course course = new Course();
        course.setId(1);
        course.setName("Course 1");

        when(courseService.getCourseById(1)).thenReturn(course);

        Course result = courseController.getCourseById(1);

        assertEquals("Course 1", result.getName());
    }


    @Test
    public void testCreateCourse() {
        Course course = new Course();
        course.setId(1);
        course.setName("New Course");

        when(courseService.createCourse(any(Course.class))).thenReturn(course);

        ResponseEntity<Course> response = courseController.createCourse(course);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("New Course", response.getBody().getName());
    }


    @Test
    public void testUpdateCourse() {
        Course updatedCourse = new Course();
        updatedCourse.setId(1);
        updatedCourse.setName("Updated Course");

        when(courseService.updateCourse(eq(1), any(Course.class))).thenReturn(updatedCourse);

        ResponseEntity<Course> response = courseController.updateCourse(1, updatedCourse);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Course", response.getBody().getName());
    }

    @Test
    public void testDeleteCourse() {
        doNothing().when(courseService).deleteCourse(1);

        ResponseEntity<Void> response = courseController.deleteCourse(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(courseService, times(1)).deleteCourse(1);
    }
}
