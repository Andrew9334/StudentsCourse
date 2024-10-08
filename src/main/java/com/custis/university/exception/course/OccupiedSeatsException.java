package com.custis.university.exception.course;

public class OccupiedSeatsException extends RuntimeException {

    public OccupiedSeatsException(String message) {
        super(message);
    }
}
