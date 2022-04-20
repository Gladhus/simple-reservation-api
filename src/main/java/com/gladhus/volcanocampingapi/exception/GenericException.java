package com.gladhus.volcanocampingapi.exception;

import org.springframework.http.HttpStatus;

public class GenericException extends Exception {

    private final HttpStatus status;

    public GenericException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
