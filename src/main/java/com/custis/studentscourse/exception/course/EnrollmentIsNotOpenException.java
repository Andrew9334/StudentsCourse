package com.custis.studentscourse.exception.course;

public class EnrollmentIsNotOpenException extends RuntimeException {

    public EnrollmentIsNotOpenException(String message) {
        super(message);
    }
}
