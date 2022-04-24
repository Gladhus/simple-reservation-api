package com.gladhus.volcanocampingapi.handler.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorMessageDto {

    private HttpStatus status;
    private String message;

}