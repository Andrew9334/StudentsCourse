package com.custis.university.exception;

public class OccupiedSeatsException extends RuntimeException {

    public OccupiedSeatsException(String message) {
        super(message);
    }
}
