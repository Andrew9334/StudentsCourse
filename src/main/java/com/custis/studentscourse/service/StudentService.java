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

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(int studentId, Student studentDetails) {
        Student student = getStudentById(studentId);
        student.setName(studentDetails.getName());
        return studentRepository.save(student);
    }

    public void deleteStudent(int studentId) {
        Student student = getStudentById(studentId);
        studentRepository.delete(student);
    }
//    public Set<Course> getStudentCourses(int studentId) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
//        return student.getCourses();
//    }


}
