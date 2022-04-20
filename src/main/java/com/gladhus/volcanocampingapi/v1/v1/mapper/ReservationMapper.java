package com.gladhus.volcanocampingapi.v1.v1.mapper;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public Reservation mapToEntity(CreateReservationDto createReservationDto) {
        return Reservation.builder()
                .fullName(createReservationDto.getFullName())
                .email(createReservationDto.getEmail())
                .checkin(createReservationDto.getCheckin())
                .checkout(createReservationDto.getCheckout())
                .build();
    }

    public ReservationDto mapToDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .fullName(reservation.getFullName())
                .email(reservation.getEmail())
                .status(reservation.getStatus())
                .checkin(reservation.getCheckin())
                .checkout(reservation.getCheckout())
                .build();
    }
}
