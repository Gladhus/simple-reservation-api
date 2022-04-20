package com.gladhus.volcanocampingapi.v1.v1.adapter;

import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.v1.v1.mapper.ReservationMapper;
import com.gladhus.volcanocampingapi.v1.service.ReservationService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        notNull(createReservationDto.getCheckin(), "Check-in date is required.");
        notNull(createReservationDto.getCheckout(), "Check-out date is required.");

        return reservationMapper.mapToDto(
                reservationService.createReservation(
                        reservationMapper.mapToEntity(createReservationDto)));
    }

    public ReservationDto getReservation(String id) throws GenericException {
        return reservationMapper.mapToDto(reservationService.getReservation(id));
    }

    public ReservationDto cancelReservation(String id) throws GenericException {
        hasText(id, "Reservation id is required.");

        return reservationMapper.mapToDto(reservationService.cancelReservation(id));
    }

    public Set<LocalDate> getAvailabilities(LocalDate startDate, LocalDate endDate) throws GenericException {
        if (startDate == null) {
            startDate = LocalDate.now();
        }

        if (endDate == null) {
            endDate = LocalDate.now().plus(30, ChronoUnit.DAYS);
        }

        return reservationService.getAvailabilities(startDate, endDate);
    }
}
