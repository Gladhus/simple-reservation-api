package com.gladhus.volcanocampingapi.handler;

import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.exception.InvalidDatesException;
import com.gladhus.volcanocampingapi.exception.ReservationNotFoundException;
import com.gladhus.volcanocampingapi.handler.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ReservationNotFoundException.class})
    public ErrorMessage notFoundErrorHandler(GenericException e) {
        return ErrorMessage.builder().status(e.getStatus()).message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {InvalidDatesException.class})
    public ErrorMessage badRequestErrorHandler(GenericException e) {
        return ErrorMessage.builder().status(e.getStatus()).message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ErrorMessage illegalArgumentErrorHandler(IllegalArgumentException e) {
        return ErrorMessage.builder().status(HttpStatus.BAD_REQUEST).message(e.getMessage()).build();
    }

}
