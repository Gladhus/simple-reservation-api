package com.gladhus.volcanocampingapi.exception;

import org.springframework.http.HttpStatus;

public class InvalidDatesException extends GenericException {

    private static final String ERROR_MESSAGE = "One of both of the dates entered are invalid.";

    public InvalidDatesException(HttpStatus status) {
        super(status, ERROR_MESSAGE);
    }

    public InvalidDatesException(HttpStatus status, String message) {
        super(status, message);
    }
}
