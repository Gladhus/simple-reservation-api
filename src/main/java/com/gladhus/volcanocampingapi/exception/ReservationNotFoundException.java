package com.gladhus.volcanocampingapi.exception;

import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends GenericException {

    private static final String ERROR_MESSAGE = "No reservation was found for provided reservation id.";
    public ReservationNotFoundException() {
        super(HttpStatus.NOT_FOUND, ERROR_MESSAGE);
    }
}
