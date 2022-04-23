package com.gladhus.volcanocampingapi.v1.v1.mapper;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import org.springframework.stereotype.Component;

/**
 * Mapper class for any mapping between domain and DTO classes.
 */
@Component
public class ReservationMapper {

    /**
     * Maps a CreateReservationDto to a domain's Reservation entity.
     * @param createReservationDto with all information for a reservation.
     * @return {@link Reservation} entity with the same information as the dto provided.
     */
    public Reservation mapToEntity(CreateReservationDto createReservationDto) {
        return Reservation.builder()
                .fullName(createReservationDto.getFullName())
                .email(createReservationDto.getEmail())
                .checkin(createReservationDto.getCheckin())
                .checkout(createReservationDto.getCheckout())
                .build();
    }

    /**
     * Maps a domain's Reservation entity to a ReservationDto.
     * @param reservation entity with all information for a reservation.
     * @return ReservationDto with the same information as the entity provided.
     */
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

    /**
     * Maps a CreateReservationDto to a Reservation entity and adding the id provided to the entity.
     * @param id id of the reservation.
     * @param createReservationDto containing all information about the reservation.
     * @return Reservation entity with all information from the DTO and the id provided.
     */
    public Reservation mapToEntity(String id, CreateReservationDto createReservationDto) {
        return Reservation.builder()
                .id(id)
                .fullName(createReservationDto.getFullName())
                .email(createReservationDto.getEmail())
                .checkin(createReservationDto.getCheckin())
                .checkout(createReservationDto.getCheckout())
                .build();
    }
}