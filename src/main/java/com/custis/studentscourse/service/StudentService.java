package com.custis.studentscourse.service;

import com.custis.studentscourse.model.Student;
import com.custis.studentscourse.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(String name) {
        Student student = new Student();
        student.setName(name);
        return studentRepository.save(student);
    }
}
