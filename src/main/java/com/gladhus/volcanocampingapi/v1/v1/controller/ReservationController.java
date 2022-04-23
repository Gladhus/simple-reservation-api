package com.gladhus.volcanocampingapi.v1.v1.controller;

import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.v1.v1.adapter.ReservationAdapter;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Reservation", description = "All operations related to reservations.")
public class ReservationController {

    private final ReservationAdapter reservationAdapter;

    @Autowired
    public ReservationController(ReservationAdapter reservationAdapter) {
        this.reservationAdapter = reservationAdapter;
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Error(s) related to validating the information provided.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Reservation could not be found.", content = @Content)
    })
    @Operation(summary = "Returns the details of a reservation for the id.")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable String id) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.getReservation(id), HttpStatus.OK);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Error(s) related to validating the information provided.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Reservation could not be done for provided dates.", content = @Content)
    })
    @Operation(summary = "Creates a new reservation with the information provided.")
    public ResponseEntity<ReservationDto> createReservation(@RequestBody CreateReservationDto createReservationDto) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.createReservation(createReservationDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Error(s) related to validating the information provided.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Reservation could not be done for provided dates.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Reservation could not be found.", content = @Content)
    })
    @Operation(summary = "Updates an existing reservation with the information provided.")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable String id, @RequestBody CreateReservationDto createReservationDto) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.updateReservation(id, createReservationDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Error(s) related to validating the information provided.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Reservation could not be found.", content = @Content)
    })
    @Operation(summary = "Cancels an existing reservation with the id provided.")
    public ResponseEntity<ReservationDto> cancelReservation(@PathVariable String id) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.cancelReservation(id), HttpStatus.OK);
    }

    @GetMapping("/availabilities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LocalDate.class)))),
            @ApiResponse(responseCode = "400", description = "Error(s) related to validating the information provided.", content = @Content)
    })
    @Operation(summary = "Provides a list dates that are available for reserving.")
    public ResponseEntity<List<LocalDate>> getAvailabilities(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) throws GenericException {
        return new ResponseEntity<>(new ArrayList<>(reservationAdapter.getAvailabilities(fromDate, toDate)), HttpStatus.OK);
    }

}
