package com.custis.university.dto;

import java.time.ZonedDateTime;

public class CourseDTO {

    private int id;
    private String name;
    private int totalSeats;
    private int occupiedSeats;
    private ZonedDateTime enrollmentStart;
    private ZonedDateTime EnrollmentEnd;

    public CourseDTO() {
    }

    public CourseDTO(int id, String name, int totalSeats, int occupiedSeats, ZonedDateTime enrollmentStart, ZonedDateTime EnrollmentEnd) {
        this.id = id;
        this.name = name;
        this.totalSeats = totalSeats;
        this.occupiedSeats = occupiedSeats;
        this.enrollmentStart = enrollmentStart;
        this.EnrollmentEnd = EnrollmentEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getOccupiedSeats() {
        return occupiedSeats;
    }

    public void setOccupiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    public ZonedDateTime getEnrollmentStart() {
        return enrollmentStart;
    }

    public void setEnrollmentStart(ZonedDateTime enrollmentStart) {
        this.enrollmentStart = enrollmentStart;
    }

    public ZonedDateTime getEnrollmentEnd() {
        return EnrollmentEnd;
    }

    public void setEnrollmentEnd(ZonedDateTime enrollmentEnd) {
        this.EnrollmentEnd = enrollmentEnd;
    }
}
