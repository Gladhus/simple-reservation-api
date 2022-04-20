package com.gladhus.volcanocampingapi.adapter;

import com.gladhus.volcanocampingapi.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.dto.ReservationDto;
import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.mapper.ReservationMapper;
import com.gladhus.volcanocampingapi.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

@Component
public class ReservationAdapter {

    private final ReservationMapper reservationMapper;

    private final ReservationService reservationService;

    @Autowired
    public ReservationAdapter(ReservationMapper reservationMapper, ReservationService reservationService) {
        this.reservationMapper = reservationMapper;
        this.reservationService = reservationService;
    }

    public ReservationDto createReservation(CreateReservationDto createReservationDto) throws GenericException {
        hasText(createReservationDto.getEmail(), "Email is required.");
        hasText(createReservationDto.getFullName(), "Full name is required.");
        notNull(createReservationDto.getCheckin(), "Check-in date is required.");
        notNull(createReservationDto.getCheckout(), "Check-out date is required.");

        return reservationMapper.mapToDto(
                reservationService.createReservation(
                        reservationMapper.mapToEntity(createReservationDto)));
    }

    public ReservationDto getReservation(String id) throws GenericException {
        return reservationMapper.mapToDto(reservationService.getReservation(id));
    }
}
