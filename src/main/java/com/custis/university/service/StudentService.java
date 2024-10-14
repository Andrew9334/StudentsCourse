package com.custis.university.service;

import com.custis.university.exception.student.StudentNotFoundException;
import com.custis.university.model.Student;
import com.custis.university.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final RedisService redisService;

    public StudentService(StudentRepository studentRepository, RedisService redisService) {
        this.studentRepository = studentRepository;
        this.redisService = redisService;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int studentId) {
        String cacheKey = "student:" + studentId;
        Student cacheStudent = redisService.getCachedValue(cacheKey, Student.class);

        if (cacheStudent != null) {
            logger.info("Student fetched from cache: {}", cacheKey);
            return cacheStudent;
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        redisService.cacheValue(cacheKey, student);
        logger.info("Student cached: {}", cacheKey);
        return student;
    }

    @Transactional
    public Student createStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        redisService.cacheValue("student:" + savedStudent.getId(), savedStudent);
        logger.info("Student created and cached: {}", savedStudent.getId());
        return savedStudent;
    }

    @Transactional
    public Student updateStudent(int studentId, Student studentDetails) {
        Student student = getStudentById(studentId);
        student.setName(studentDetails.getName());
        redisService.cacheValue("student:" + studentId, student);
        logger.info("Student updated and cached: {}", studentId);
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(int studentId) {
        Student student = getStudentById(studentId);
        studentRepository.delete(student);
        redisService.deleteCacheValue("student:" + studentId);
        logger.info("Student deleted and removed from cache: {}", studentId);
    }
}
