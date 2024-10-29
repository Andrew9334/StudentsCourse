package com.custis.university;

import com.custis.university.dto.CourseDTO;
import com.custis.university.dto.StudentDTO;
import com.custis.university.exception.*;
import com.custis.university.mapper.CourseMapper;
import com.custis.university.mapper.StudentMapper;
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
        course.setName("Test Course");
        course.setTotalSeats(30);
        course.setOccupiedSeats(10);
        course.setEnrollmentStart(ZonedDateTime.now().minusDays(1));
        course.setEnrollmentEnd(ZonedDateTime.now().plusDays(1));

        when(courseRepository.findAll()).thenReturn(List.of(course));

        List<CourseDTO> courses = enrollmentService.getAllCourses();

        assertEquals(1, courses.size());
        CourseDTO expectedCourseDTO = CourseMapper.toDTO(course);
        CourseDTO actualCourseDTO = courses.get(0);

        assertEquals(expectedCourseDTO.getId(), actualCourseDTO.getId());
        assertEquals(expectedCourseDTO.getName(), actualCourseDTO.getName());
        assertEquals(expectedCourseDTO.getTotalSeats(), actualCourseDTO.getTotalSeats());
        assertEquals(expectedCourseDTO.getOccupiedSeats(), actualCourseDTO.getOccupiedSeats());
    }

    @Test
    void getCourseById() {
        Course course = new Course();
        course.setId(1);
        course.setName("Test Course");
        course.setTotalSeats(30);
        course.setOccupiedSeats(10);
        course.setEnrollmentStart(ZonedDateTime.now().minusDays(1));
        course.setEnrollmentEnd(ZonedDateTime.now().plusDays(1));

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        CourseDTO foundCourse = enrollmentService.getCourseById(1);

        assertEquals(course.getId(), foundCourse.getId());
        assertEquals(course.getName(), foundCourse.getName());
        assertEquals(course.getTotalSeats(), foundCourse.getTotalSeats());
        assertEquals(course.getOccupiedSeats(), foundCourse.getOccupiedSeats());
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

        StudentDTO studentDTO = StudentMapper.toDTO(student);
        CourseDTO courseDTO = CourseMapper.toDTO(course);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        enrollmentService.enrollStudent(studentDTO, courseDTO);

        Enrollment expectedEnrollment = new Enrollment();
        expectedEnrollment.setStudentName("Ivan Ivanov");
        expectedEnrollment.setCourseName("Course Name");

        verify(enrollmentRepository).save(argThat(enrollment ->
                enrollment.getStudentName().equals(expectedEnrollment.getStudentName()) &&
                        enrollment.getCourseName().equals(expectedEnrollment.getCourseName())
        ));

        course.setOccupiedSeats(course.getOccupiedSeats() + 1);

        assertEquals(2, course.getOccupiedSeats());
        verify(courseRepository).save(course);
    }

    @Test
    void enrollStudentThrowsStudentNotFoundException() {
        Course course = new Course();
        course.setId(1);
        CourseDTO courseDTO = CourseMapper.toDTO(course);

        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> enrollmentService.enrollStudent(new StudentDTO(), courseDTO));
    }

    @Test
    void enrollStudentThrowsCourseNotFoundException() {
        Student student = new Student();
        student.setId(1);
        StudentDTO studentDTO = StudentMapper.toDTO(student);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> enrollmentService.enrollStudent(studentDTO, new CourseDTO()));
    }

    @Test
    void enrollStudentThrowsEnrollmentNotOpenException() {
        Student student = new Student();
        student.setId(1);
        Course course = new Course();
        course.setId(1);
        course.setEnrollmentStart(ZonedDateTime.now().plusDays(1));
        StudentDTO studentDTO = StudentMapper.toDTO(student);
        CourseDTO courseDTO = CourseMapper.toDTO(course);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        assertThrows(EnrollmentNotOpenException.class, () -> enrollmentService.enrollStudent(studentDTO, courseDTO));
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
        StudentDTO studentDTO = StudentMapper.toDTO(student);
        CourseDTO courseDTO = CourseMapper.toDTO(course);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        assertThrows(OccupiedSeatsException.class, () -> enrollmentService.enrollStudent(studentDTO, courseDTO));
    }

    @Test
    void deleteEnrollment() {
        int enrollmentId = 1;
        enrollmentService.deleteEnrollment(enrollmentId);
        verify(enrollmentRepository).deleteById(enrollmentId);
    }
}
