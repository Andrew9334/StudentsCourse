package com.custis.university.service;

import com.custis.university.dto.StudentDTO;
import com.custis.university.exception.student.StudentNotFoundException;
import com.custis.university.mapper.StudentMapper;
import com.custis.university.model.Student;
import com.custis.university.repository.postgres.StudentRepository;
import com.custis.university.service.rabbit.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final RedisService redisService;
    private final MessageProducer messageProducer;

    public StudentService(StudentRepository studentRepository, RedisService redisService, MessageProducer messageProducer) {
        this.studentRepository = studentRepository;
        this.redisService = redisService;
        this.messageProducer = messageProducer;
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(int studentId) {
        String cacheKey = "student:" + studentId;
        StudentDTO cacheStudent = redisService.getCachedValue(cacheKey, StudentDTO.class);

        if (cacheStudent != null) {
            logger.info("Student fetched from cache: {}", cacheKey);
            return cacheStudent;
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        StudentDTO studentDTO = StudentMapper.toDTO(student);
        redisService.cacheValue(cacheKey, student);
        logger.info("Student cached: {}", cacheKey);
        return studentDTO;
    }

    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = StudentMapper.toEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        StudentDTO savedStudentDTO = StudentMapper.toDTO(savedStudent);
        redisService.cacheValue("student:" + savedStudentDTO.getId(), savedStudentDTO);
        messageProducer.sendStudentMessage(savedStudentDTO);
        logger.info("Student created and cached: {}", savedStudentDTO.getId());
        return savedStudentDTO;
    }

    @Transactional
    public StudentDTO updateStudent(int studentId, StudentDTO studentDetails) {
        Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        student.setName(studentDetails.getName());
        StudentDTO updatedStudentDTO = StudentMapper.toDTO(studentRepository.save(student));
        redisService.cacheValue("student:" + studentId, student);
        messageProducer.sendStudentMessage(updatedStudentDTO);
        logger.info("Student updated and cached: {}", studentId);
        return updatedStudentDTO;
    }

    @Transactional
    public void deleteStudent(int studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        studentRepository.delete(student);
        redisService.deleteCacheValue("student:" + studentId);
        StudentDTO studentDTO = StudentMapper.toDTO(student);
        studentDTO.setAction("delete");
        messageProducer.sendStudentMessage(StudentMapper.toDTO(student));
        logger.info("Student deleted and removed from cache: {}", studentId);
    }

    public void createOrUpdateStudent(StudentDTO studentDTO) {
        if (studentDTO.getId() == null) {
            createStudent(studentDTO);
        } else {
            updateStudent(studentDTO.getId(), studentDTO);
        }
    }
}
