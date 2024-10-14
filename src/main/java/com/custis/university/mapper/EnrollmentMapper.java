package com.custis.university.mapper;

import com.custis.university.dto.EnrollmentDTO;
import com.custis.university.model.Enrollment;
import com.custis.university.model.Student;

public class EnrollmentMapper {
    public static EnrollmentDTO toDTO(Enrollment enrollment) {
        return new EnrollmentDTO(
                enrollment.getId(),
                enrollment.getStudentName(),
                enrollment.getCourseName()
        );
    }

    public static Enrollment toEntity(EnrollmentDTO enrollmentDTO) {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentDTO.getId());
        enrollment.setStudentName(enrollmentDTO.getStudentName());
        enrollment.setCourseName(enrollmentDTO.getCourseName());
        return enrollment;
    }
}
