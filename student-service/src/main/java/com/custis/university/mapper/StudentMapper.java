package com.custis.university.mapper;

import com.custis.university.dto.StudentDTO;
import com.custis.university.model.Student;

public class StudentMapper {
    public static StudentDTO toDTO(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getName()
        );
    }

    public static Student toEntity(StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        return student;
    }
}
