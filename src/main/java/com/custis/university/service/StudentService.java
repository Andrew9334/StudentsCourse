package com.custis.university.service;

import com.custis.university.exception.student.StudentNotFoundException;
import com.custis.university.model.Student;
import com.custis.university.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(int studentId, Student studentDetails) {
        Student student = getStudentById(studentId);
        student.setName(studentDetails.getName());
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(int studentId) {
        Student student = getStudentById(studentId);
        studentRepository.delete(student);
    }
}
