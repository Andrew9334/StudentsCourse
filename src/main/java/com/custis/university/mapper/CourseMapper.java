package com.custis.university.mapper;

import com.custis.university.dto.CourseDTO;
import com.custis.university.model.Course;

public class CourseMapper {
    public static CourseDTO toDTO(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getTotalSeats(),
                course.getOccupiedSeats(),
                course.getEnrollmentStart(),
                course.getEnrollmentEnd()
        );
    }

    public static Course toEntity(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setName(courseDTO.getName());
        course.setTotalSeats(courseDTO.getTotalSeats());
        course.setOccupiedSeats(courseDTO.getOccupiedSeats());
        course.setEnrollmentStart(courseDTO.getEnrollmentStart());
        course.setEnrollmentEnd(courseDTO.getEnrollmentEnd());
        return course;
    }
}
