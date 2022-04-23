package com.gladhus.volcanocampingapi.handler;

import com.gladhus.volcanocampingapi.exception.GenericAPIException;
import com.gladhus.volcanocampingapi.exception.InvalidDatesException;
import com.gladhus.volcanocampingapi.exception.ReservationNotFoundException;
import com.gladhus.volcanocampingapi.handler.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ReservationNotFoundException.class})
    public ErrorMessageDto notFoundErrorHandler(GenericAPIException e) {
        return ErrorMessageDto.builder().status(e.getStatus()).message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {InvalidDatesException.class})
    public ErrorMessageDto invalidDatesErrorHandler(GenericAPIException e) {
        return ErrorMessageDto.builder().status(e.getStatus()).message(e.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ErrorMessageDto illegalArgumentErrorHandler(IllegalArgumentException e) {
        return ErrorMessageDto.builder().status(HttpStatus.BAD_REQUEST).message(e.getMessage()).build();
    }

}