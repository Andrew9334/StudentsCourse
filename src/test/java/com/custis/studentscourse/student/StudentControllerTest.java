package com.custis.studentscourse.student;

import com.custis.studentscourse.exception.student.StudentNotFoundException;
import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.repository.StudentRepository;
import com.custis.studentscourse.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentControllerTest {
    private StudentRepository studentRepository;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepository = Mockito.mock(StudentRepository.class);
        studentService = new StudentService(studentRepository);
    }

    @Test
    void testCreateStudentSuccess() {
        Student student = new Student();
        student.setName("Petr Petrov");

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student createdStudent = studentService.createStudent("Petr Petrov");

        assertNotNull(createdStudent);
        assertEquals("Petr Petrov", createdStudent.getName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testGetStudentCoursesStudentNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentCourses(1);
        });

        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student();
        student1.setName("Petr Petrov");

        Student student2 = new Student();
        student2.setName("Ivan Ivanov");

        when(studentRepository.findAll()).thenReturn(List.of(student1, student2));

        List<Student> students = studentService.getAllStudents();

        assertEquals(2, students.size());
        assertEquals("Petr Petrov", students.get(0).getName());
        assertEquals("Ivan Ivanov", students.get(1).getName());
    }

    @Test
    void testCreateStudentInvalidName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.createStudent(null);
        });

        assertEquals("Student name cannot be null or empty", exception.getMessage());
    }
}
