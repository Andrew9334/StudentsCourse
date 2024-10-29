package com.custis.university.exception;

public class EnrollmentNotOpenException extends RuntimeException {

    public EnrollmentNotOpenException(String message) {
        super(message);
    }
}
