package com.custis.university.exception;

public class EnrollmentNotFoundException extends RuntimeException {

    public EnrollmentNotFoundException(String message) {
        super(message);
    }
}
