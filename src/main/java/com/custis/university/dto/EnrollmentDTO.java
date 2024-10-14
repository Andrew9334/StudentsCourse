package com.custis.university.dto;

import javax.validation.constraints.NotEmpty;

public class EnrollmentDTO {

    private int id;
    @NotEmpty(message = "Student name cannot be empty")
    private String studentName;
    @NotEmpty(message = "Course name cannot be empty")
    private String courseName;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(int id, String studentName, String courseName) {
        this.id = id;
        this.studentName = studentName;
        this.courseName = courseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
