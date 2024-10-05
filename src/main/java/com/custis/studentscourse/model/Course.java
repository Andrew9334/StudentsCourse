package com.custis.studentscourse.model;

import jakarta.persistence.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;
    @Column(name = "course_name")
    @NotEmpty(message = "Course name cannot be empty")
    private String name;
    @Column(name = "total_seats")
    @Positive(message = "Total seats must be positive")
    private int totalSeats;
    @Column(name = "occupied_seats")
    private int occupiedSeats;
    @Column(name = "enrollment_start")
    private ZonedDateTime enrollmentStart;
    @Column(name = "enrollment_end")
    private ZonedDateTime enrollmentEnd;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getOccupiedSeats() {
        return occupiedSeats;
    }

    public ZonedDateTime getEnrollmentStart() {
        return enrollmentStart;
    }

    public ZonedDateTime getEnrollmentEnd() {
        return enrollmentEnd;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setOccupiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    public void setEnrollmentStart(ZonedDateTime enrollmentStart) {
        this.enrollmentStart = enrollmentStart;
    }

    public void setEnrollmentEnd(ZonedDateTime enrollmentEnd) {
        this.enrollmentEnd = enrollmentEnd;
    }
}
