package com.custis.university;

import com.custis.university.controller.CourseController;
import com.custis.university.dto.CourseDTO;
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
        CourseDTO course1 = new CourseDTO();
        course1.setId(1);
        course1.setName("Course 1");

        CourseDTO course2 = new CourseDTO();
        course2.setId(2);
        course2.setName("Course 2");

        List<CourseDTO> courses = Arrays.asList(course1, course2);
        when(courseService.getAllCourses()).thenReturn(courses);

        List<CourseDTO> result = courseController.getAllCourses();

        assertEquals(2, result.size());
        assertEquals("Course 1", result.get(0).getName());
        assertEquals("Course 2", result.get(1).getName());
    }

    @Test
    public void testGetCourseById() {
        CourseDTO course = new CourseDTO();
        course.setId(1);
        course.setName("Course 1");

        when(courseService.getCourseById(1)).thenReturn(course);

        CourseDTO result = courseController.getCourseById(1);

        assertEquals("Course 1", result.getName());
    }

    @Test
    public void testCreateCourse() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(1);
        courseDTO.setName("New Course");

        when(courseService.createCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        ResponseEntity<CourseDTO> response = courseController.createCourse(courseDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("New Course", response.getBody().getName());
    }

    @Test
    public void testUpdateCourse() {
        CourseDTO updatedCourse = new CourseDTO();
        updatedCourse.setId(1);
        updatedCourse.setName("Updated Course");

        when(courseService.updateCourse(eq(1), any(CourseDTO.class))).thenReturn(updatedCourse);

        ResponseEntity<CourseDTO> response = courseController.updateCourse(1, updatedCourse);

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
