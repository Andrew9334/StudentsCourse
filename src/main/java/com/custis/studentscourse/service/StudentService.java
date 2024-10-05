package com.custis.studentscourse.service;

import com.custis.studentscourse.exception.student.StudentNotFoundException;
import com.custis.studentscourse.model.Course;
import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty");
        }
        Student student = new Student();
        student.setName(name);
        return studentRepository.save(student);
    }

    public Set<Course> getStudentCourses(int studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        return student.getCourses();
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
