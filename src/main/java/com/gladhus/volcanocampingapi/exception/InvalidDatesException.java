package com.gladhus.volcanocampingapi.exception;

import org.springframework.http.HttpStatus;

public class InvalidDatesException extends GenericAPIException {

    private static final String ERROR_MESSAGE = "One of both of the dates entered are invalid.";

    public InvalidDatesException() {
        super(HttpStatus.FORBIDDEN, ERROR_MESSAGE);
    }

    public InvalidDatesException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
