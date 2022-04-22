package com.gladhus.volcanocampingapi.v1.v1.controller;

import com.gladhus.volcanocampingapi.config.SwaggerConfig;
import com.gladhus.volcanocampingapi.v1.v1.adapter.ReservationAdapter;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import com.gladhus.volcanocampingapi.exception.GenericException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.1/reservation")
@Api(tags = SwaggerConfig.TAG_RESERVATION_API)
public class ReservationController {

    private final ReservationAdapter reservationAdapter;

    @Autowired
    public ReservationController(ReservationAdapter reservationAdapter) {
        this.reservationAdapter = reservationAdapter;
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Error(s) related to validating the information provided."),
            @ApiResponse(code = 404, message = "Reservation could not be found.")
    })
    @ApiOperation(value = "Returns the details of a reservation for the id.", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable String id) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.getReservation(id), HttpStatus.OK);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Error(s) related to validating the information provided.")
    })
    @ApiOperation(value = "Creates a new reservation with the information provided.", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDto> createReservation(@RequestBody CreateReservationDto createReservationDto) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.createReservation(createReservationDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Error(s) related to validating the information provided."),
            @ApiResponse(code = 404, message = "Reservation could not be found.")
    })
    @ApiOperation(value = "Updates an existing reservation with the information provided.", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable String id, @RequestBody CreateReservationDto createReservationDto) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.updateReservation(id, createReservationDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Error(s) related to validating the information provided."),
            @ApiResponse(code = 404, message = "Reservation could not be found.")
    })
    @ApiOperation(value = "Cancels an existing reservation with the id provided.", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDto> cancelReservation(@PathVariable String id) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.cancelReservation(id), HttpStatus.OK);
    }

    @GetMapping("/availabilities")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Error(s) related to validating the information provided.")
    })
    @ApiOperation(value = "Provides a list dates that are available for reserving.", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<LocalDate>> getAvailabilities(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.getAvailabilities(startDate, endDate), HttpStatus.OK);
    }

}
