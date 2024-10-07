package com.custis.studentscourse.course;

import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.repository.CourseRepository;
import com.custis.studentscourse.repository.StudentRepository;
import com.custis.studentscourse.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CourseServiceTest {

    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseRepository = Mockito.mock(CourseRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        courseService = new CourseService(courseRepository, studentRepository);
    }

//    @Test
//    void testEnrollStudentSuccess() throws Exception {
//        Course course = new Course();
//        course.setId(1);
//        course.setTotalSeats(2);
//        course.setOccupiedSeats(1);
//        course.setEnrollmentStart(ZonedDateTime.now().minusDays(1));
//        course.setEnrollmentEnd(ZonedDateTime.now().plusDays(1));
//
//        Student student = new Student();
//        student.setId(1);
//        student.setName("Ivanov Ivan");
//
//        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
//        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
//
//        courseService.enrollStudent(1, student);
//
//        assertEquals(2, course.getOccupiedSeats());
//        verify(courseRepository, times(1)).save(course);
//    }

//    @Test
//    void testEnrollStudentNoAvailableSpots() {
//        Course course = new Course();
//        course.setId(1);
//        course.setTotalSeats(2);
//        course.setOccupiedSeats(1);
//        course.setEnrollmentStart(ZonedDateTime.now().plusDays(1));
//        course.setEnrollmentEnd(ZonedDateTime.now().plusDays(2));
//
//        Student student = new Student();
//        student.setId(1);
//        student.setName("Ivanov Ivan");
//
//
//        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
//        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
//
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            courseService.enrollStudent(1, student);
//        });
//
//        assertEquals("Enrollment is not open for this course", exception.getMessage());
//    }

//    @Test
//    void testEnrollStudentOutsideEnrollmentWindow() {
//        Course course = new Course();
//        course.setId(1);
//        course.setTotalSeats(1);
//        course.setOccupiedSeats(2);
//        course.setEnrollmentStart(ZonedDateTime.now().minusDays(1));
//        course.setEnrollmentEnd(ZonedDateTime.now().plusDays(1));
//
//        Student student = new Student();
//        student.setId(1);
//        student.setName("Ivanov Ivan");
//
//        when(courseRepository.findById(1)).thenReturn(java.util.Optional.of(course));
//        when(studentRepository.findById(1)).thenReturn(java.util.Optional.of(student));
//
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            courseService.enrollStudent(1, student);
//        });
//
//        assertEquals("No available seats", exception.getMessage());
//    }
}
