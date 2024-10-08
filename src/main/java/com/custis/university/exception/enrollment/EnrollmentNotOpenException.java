package com.custis.university.exception.enrollment;

public class EnrollmentNotOpenException extends RuntimeException {

    public EnrollmentNotOpenException(String message) {
        super(message);
    }
}
