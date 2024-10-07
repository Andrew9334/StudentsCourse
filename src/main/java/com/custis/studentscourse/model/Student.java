package com.custis.studentscourse.model;

import jakarta.persistence.*;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private int id;
    @Column(name = "student_name")
    @NotEmpty(message = "Student name cannot be empty")
    private String name;

//    @ManyToMany
//    @JoinTable(
//            name = "enrollments",
//            joinColumns = @JoinColumn(name = "student_id"),
//            inverseJoinColumns = @JoinColumn(name = "course_id")
//    )
//    private Set<Course> courses = new HashSet<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public Set<Course> getCourses() {
//        return courses;
//    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setCourses(Set<Course> courses) {
//        this.courses = courses;
//    }
}
