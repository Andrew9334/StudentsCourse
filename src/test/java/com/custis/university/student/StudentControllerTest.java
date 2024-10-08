package com.custis.university.student;

import com.custis.university.controller.StudentController;
import com.custis.university.model.Student;
import com.custis.university.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StudentControllerTest {

    private StudentService studentService;
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        studentService = Mockito.mock(StudentService.class);
        studentController = new StudentController(studentService);
    }

    @Test
    public void testGetAllStudents() {
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("Alice");

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("Bob");

        List<Student> students = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(students);

        ResponseEntity<List<Student>> response = studentController.getAllStudents();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Alice", response.getBody().get(0).getName());
        assertEquals("Bob", response.getBody().get(1).getName());
    }

    @Test
    public void testGetStudentById() {
        Student student = new Student();
        student.setId(1);
        student.setName("Alice");

        when(studentService.getStudentById(1)).thenReturn(student);

        ResponseEntity<Student> response = studentController.getStudentById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Alice", response.getBody().getName());
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student();
        student.setId(1);
        student.setName("Alice");

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        ResponseEntity<Student> response = studentController.createStudent(student);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Alice", response.getBody().getName());
    }

    @Test
    public void testUpdateStudent() {
        Student updatedStudent = new Student();
        updatedStudent.setId(1);
        updatedStudent.setName("Updated Alice");

        when(studentService.updateStudent(eq(1), any(Student.class))).thenReturn(updatedStudent);

        ResponseEntity<Student> response = studentController.updateStudent(1, updatedStudent);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Alice", response.getBody().getName());
    }

    @Test
    public void testDeleteStudent() {
        doNothing().when(studentService).deleteStudent(1);

        ResponseEntity<Void> response = studentController.deleteStudent(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(studentService, times(1)).deleteStudent(1);
    }
}
