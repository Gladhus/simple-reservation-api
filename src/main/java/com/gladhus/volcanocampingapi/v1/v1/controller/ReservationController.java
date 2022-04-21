package com.gladhus.volcanocampingapi.v1.v1.controller;

import com.gladhus.volcanocampingapi.v1.v1.adapter.ReservationAdapter;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import com.gladhus.volcanocampingapi.exception.GenericException;
import java.time.LocalDate;
import java.util.Set;
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
public class ReservationController {

    private final ReservationAdapter reservationAdapter;

    @Autowired
    public ReservationController(ReservationAdapter reservationAdapter) {
        this.reservationAdapter = reservationAdapter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable String id) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.getReservation(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ReservationDto> createReservation(@RequestBody CreateReservationDto createReservationDto) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.createReservation(createReservationDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable String id, @RequestBody CreateReservationDto createReservationDto) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.updateReservation(id, createReservationDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationDto> cancelReservation(@PathVariable String id) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.cancelReservation(id), HttpStatus.OK);
    }

    @GetMapping("/availabilities")
    public ResponseEntity<Set<LocalDate>> getAvailabilities(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.getAvailabilities(startDate, endDate), HttpStatus.OK);
    }

}
