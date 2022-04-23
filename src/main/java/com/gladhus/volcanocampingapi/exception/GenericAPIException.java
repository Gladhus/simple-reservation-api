package com.gladhus.volcanocampingapi.exception;

import org.springframework.http.HttpStatus;

public class GenericAPIException extends Exception {

    private final HttpStatus status;

    public GenericAPIException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
