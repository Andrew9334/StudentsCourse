package com.custis.university.enrollment;

import com.custis.university.exception.course.CourseNotFoundException;
import com.custis.university.exception.course.OccupiedSeatsException;
import com.custis.university.exception.enrollment.EnrollmentNotFoundException;
import com.custis.university.exception.enrollment.EnrollmentNotOpenException;
import com.custis.university.exception.student.StudentNotFoundException;
import com.custis.university.model.Course;
import com.custis.university.model.Enrollment;
import com.custis.university.model.Student;
import com.custis.university.repository.CourseRepository;
import com.custis.university.repository.EnrollmentRepository;
import com.custis.university.repository.StudentRepository;
import com.custis.university.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EnrollmentControllerTest {

    private EnrollmentService enrollmentService;
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        studentRepository = mock(StudentRepository.class);
        enrollmentRepository = mock(EnrollmentRepository.class);
        enrollmentService = new EnrollmentService(courseRepository, studentRepository, enrollmentRepository);
    }

    @Test
    void getAllCourses() {
        Course course = new Course();
        course.setId(1);

        when(courseRepository.findAll()).thenReturn(List.of(course));

        List<Course> courses = enrollmentService.getAllCourses();

        assertEquals(1, courses.size());
        assertEquals(course, courses.get(0));
    }

    @Test
    void getCourseById() {
        Course course = new Course();
        course.setId(1);

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        Course foundCourse = enrollmentService.getCourseById(1);
        assertEquals(course, foundCourse);
    }

    @Test
    void getCourseByIdThrowsCourseNotFoundException() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EnrollmentNotFoundException.class, () -> enrollmentService.getCourseById(1));
    }

    @Test
    void enrollStudentSuccess() {
        Student student = new Student();
        student.setId(1);
        student.setName("Ivan Ivanov");
        Course course = new Course();
        course.setId(1);
        course.setName("Course Name");
        course.setTotalSeats(2);
        course.setOccupiedSeats(1);
        course.setEnrollmentStart(ZonedDateTime.now().minusDays(1));
        course.setEnrollmentEnd(ZonedDateTime.now().plusDays(1));

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        enrollmentService.enrollStudent(student, course);
        Enrollment expectedEnrollment = new Enrollment();
        expectedEnrollment.setStudentName("Ivan Ivanov");
        expectedEnrollment.setCourseName("Course Name");

        verify(enrollmentRepository).save(argThat(enrollment ->
                enrollment.getStudentName().equals(expectedEnrollment.getStudentName()) &&
                        enrollment.getCourseName().equals(expectedEnrollment.getCourseName())
        ));

        assertEquals(2, course.getOccupiedSeats());
        verify(courseRepository).save(course);
    }

    @Test
    void enrollStudentThrowsStudentNotFoundException() {
        Course course = new Course();
        course.setId(1);

        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> enrollmentService.enrollStudent(new Student(), course));
    }

    @Test
    void enrollStudentThrowsCourseNotFoundException() {
        Student student = new Student();
        student.setId(1);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> enrollmentService.enrollStudent(student, new Course()));
    }

    @Test
    void enrollStudentThrowsEnrollmentNotOpenException() {
        Student student = new Student();
        student.setId(1);
        Course course = new Course();
        course.setId(1);
        course.setEnrollmentStart(ZonedDateTime.now().plusDays(1)); // Закрытое окно записи

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        assertThrows(EnrollmentNotOpenException.class, () -> enrollmentService.enrollStudent(student, course));
    }

    @Test
    void enrollStudentThrowsOccupiedSeatsException() {
        Student student = new Student();
        student.setId(1);
        Course course = new Course();
        course.setId(1);
        course.setTotalSeats(1);
        course.setOccupiedSeats(1);
        course.setEnrollmentStart(ZonedDateTime.now().minusDays(1));
        course.setEnrollmentEnd(ZonedDateTime.now().plusDays(1));

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        assertThrows(OccupiedSeatsException.class, () -> enrollmentService.enrollStudent(student, course));
    }

    @Test
    void deleteEnrollment() {
        int enrollmentId = 1;
        enrollmentService.deleteEnrollment(enrollmentId);
        verify(enrollmentRepository).deleteById(enrollmentId);
    }
}
