package com.custis.university.service;

import com.custis.university.dto.StudentDTO;
import com.custis.university.exception.StudentNotFoundException;
import com.custis.university.mapper.StudentMapper;
import com.custis.university.model.Student;
import com.custis.university.rabbit.MessageProducer;
import com.custis.university.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .map(StudentMapper::toDTO);
    }

    public Mono<StudentDTO> getStudentById(int studentId) {
        String cacheKey = "student:" + studentId;
        Mono<StudentDTO> cacheStudent = redisService.getCachedValue(cacheKey, StudentDTO.class);

        return cacheStudent.flatMap(studentDTO -> {
                    logger.info("Student fetched from cache: {}", cacheKey);
                    return Mono.just(studentDTO);
                }).switchIfEmpty(studentRepository.findById(studentId)
                        .map(StudentMapper::toDTO))
                .doOnNext(studentDTO -> {
                    redisService.cacheValue(cacheKey, studentDTO);
                    logger.info("Student cached: {}", cacheKey);
                }).onErrorMap(e -> new StudentNotFoundException("Student not found"));
    }

    @Transactional
    public Mono<StudentDTO> createStudent(StudentDTO studentDTO) {
        Student student = StudentMapper.toEntity(studentDTO);
        return studentRepository.save(student)
                .map(StudentMapper::toDTO)
                .doOnNext(savedStudentDTO -> {
                    redisService.cacheValue("student:" + savedStudentDTO.getId(), savedStudentDTO);
                    messageProducer.sendStudentMessage(savedStudentDTO);
                    logger.info("Student created and cached: {}", savedStudentDTO.getId());
                });
    }

    public Mono<StudentDTO> updateStudent(int studentId, StudentDTO studentDetails) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(Mono.error(new StudentNotFoundException("Student not found")))
                .doOnNext(student -> {
                    student.setName(studentDetails.getName());
                })
                .flatMap(studentRepository::save)
                .map(StudentMapper::toDTO)
                .doOnNext(updatedStudentDTO -> {
                    redisService.cacheValue("student:" + studentId, updatedStudentDTO);
                    messageProducer.sendStudentMessage(updatedStudentDTO);
                    logger.info("Student updated and cached: {}", studentId);
                });
    }

    @Transactional
    public Mono<Void> deleteStudent(int studentId) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(Mono.error(new StudentNotFoundException("Student not found")))
                .flatMap(student -> {
                    redisService.deleteCacheValue("student:" + studentId);
                    messageProducer.sendStudentMessage(StudentMapper.toDTO(student));
                    logger.info("Student deleted and removed from cache: {}", studentId);
                    return studentRepository.delete(student);
                });
    }

    public Mono<Void> createOrUpdateStudent(StudentDTO studentDTO) {
        return (studentDTO.getId() == null)
                ? createStudent(studentDTO).then()
                : updateStudent(studentDTO.getId(), studentDTO).then();
    }
}

