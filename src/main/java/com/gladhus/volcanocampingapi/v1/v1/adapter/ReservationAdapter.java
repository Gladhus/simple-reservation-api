package com.gladhus.volcanocampingapi.v1.v1.adapter;

import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.v1.service.ReservationService;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.mapper.ReservationMapper;
import java.time.LocalDate;
import java.util.Set;
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
        notNull(createReservationDto.getCheckin(), "Checkin date is required.");
        notNull(createReservationDto.getCheckout(), "Checkout date is required.");

        return reservationMapper.mapToDto(
                reservationService.createReservation(
                        reservationMapper.mapToEntity(createReservationDto)));
    }

    public ReservationDto updateReservation(String id, CreateReservationDto createReservationDto) throws GenericException {
        hasText(id, "Reservation id is required.");

        return reservationMapper.mapToDto(reservationService.updateReservation(reservationMapper.mapToEntity(id, createReservationDto)));
    }

    public ReservationDto getReservation(String id) throws GenericException {
        hasText(id, "Reservation id is required.");

        return reservationMapper.mapToDto(reservationService.getReservation(id));
    }

    public ReservationDto cancelReservation(String id) throws GenericException {
        hasText(id, "Reservation id is required.");

        return reservationMapper.mapToDto(reservationService.cancelReservation(id));
    }

    public Set<LocalDate> getAvailabilities(LocalDate fromDate, LocalDate toDate) throws GenericException {
        if (fromDate == null) {
            fromDate = LocalDate.now();
        }

        if (toDate == null) {
            toDate = LocalDate.now().plusMonths(1);
        }

        return reservationService.getAvailabilities(fromDate, toDate);
    }
}
