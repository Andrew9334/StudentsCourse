package com.custis.university.model;

import jakarta.persistence.*;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private int id;
    @ManyToOne()
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;
    @Column(name = "student_name")
    @NotEmpty
    private String studentName;
    @Column(name = "course_name")
    @NotEmpty
    private String courseName;

    @Version
    @Column(name = "version")
    private Long version;

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
