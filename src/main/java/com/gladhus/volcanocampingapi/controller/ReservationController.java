package com.gladhus.volcanocampingapi.controller;

import com.gladhus.volcanocampingapi.adapter.ReservationAdapter;
import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import com.gladhus.volcanocampingapi.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.dto.ReservationDto;
import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.repository.ReservationRepository;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReservationController {

    private final ReservationAdapter reservationAdapter;

    @Autowired
    public ReservationController(ReservationAdapter reservationAdapter) {
        this.reservationAdapter = reservationAdapter;
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable String id) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.getReservation(id), HttpStatus.OK);
    }

    @PostMapping("/reservation")
    public ResponseEntity<ReservationDto> createReservation(@RequestBody CreateReservationDto createReservationDto) throws GenericException {
        return new ResponseEntity<>(reservationAdapter.createReservation(createReservationDto), HttpStatus.CREATED);
    }

}
