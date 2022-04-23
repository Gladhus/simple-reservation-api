package com.gladhus.volcanocampingapi.v1.v1.adapter;

import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.v1.service.ReservationService;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.mapper.ReservationMapper;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

/**
 * Adapter class for adapting the DTOs and Entities so that the controller layer and service layer understand each other
 * on reservation operations.
 */
@Component
public class ReservationAdapter {

    private static final String ID_REQUIRED_MESSAGE = "Reservation id is required.";

    private final ReservationMapper reservationMapper;

    private final ReservationService reservationService;

    @Autowired
    public ReservationAdapter(ReservationMapper reservationMapper, ReservationService reservationService) {
        this.reservationMapper = reservationMapper;
        this.reservationService = reservationService;
    }

    /**
     * Creates a new reservation through the {@link ReservationService} using the {@link ReservationMapper}.
     * @param createReservationDto DTO containing the information for the reservation to be created.
     * @return {@link ReservationDto} with all information for the newly created reservation.
     * @throws GenericException if any exception was raised during mapping or creation of the reservation.
     */
    public ReservationDto createReservation(CreateReservationDto createReservationDto) throws GenericException {
        hasText(createReservationDto.getEmail(), "Email is required.");
        hasText(createReservationDto.getFullName(), "Full name is required.");
        notNull(createReservationDto.getCheckin(), "Checkin date is required.");
        notNull(createReservationDto.getCheckout(), "Checkout date is required.");

        return reservationMapper.mapToDto(
                reservationService.createReservation(
                        reservationMapper.mapToEntity(createReservationDto)));
    }

    /**
     * Updates a reservation through the {@link ReservationService} using the {@link ReservationMapper}.
     * @param id if of the reservation to be updated.
     * @param createReservationDto DTO containing the information for the reservation to be created.
     * @return {@link ReservationDto} with updated information of the reservation.
     * @throws GenericException if any exception was raised during mapping or update of the reservation.
     */
    public ReservationDto updateReservation(String id, CreateReservationDto createReservationDto) throws GenericException {
        hasText(id, ID_REQUIRED_MESSAGE);

        return reservationMapper.mapToDto(reservationService.updateReservation(reservationMapper.mapToEntity(id, createReservationDto)));
    }

    /**
     * Gets a reservation through the {@link ReservationService} using the {@link ReservationMapper}.
     * @param id of the reservation to find.
     * @return {@link ReservationDto} with all the information of the reservation.
     * @throws GenericException if any exception was raised getting or mapping the reservation.
     */
    public ReservationDto getReservation(String id) throws GenericException {
        hasText(id, ID_REQUIRED_MESSAGE);

        return reservationMapper.mapToDto(reservationService.getReservation(id));
    }

    /**
     * Cancels a reservation through the {@link ReservationService} using the {@link ReservationMapper}.
     * @param id of the reservation to cancel.
     * @return {@link ReservationDto} with updated status of the reservation.
     * @throws GenericException if any exception was raised cancelling the reservation.
     */
    public ReservationDto cancelReservation(String id) throws GenericException {
        hasText(id, ID_REQUIRED_MESSAGE);

        return reservationMapper.mapToDto(reservationService.cancelReservation(id));
    }

    /**
     * Gets all dates available for reservation through the {@link ReservationService} using the {@link ReservationMapper}.
     * If no dates are provided, will use today and today + 1 month by default.
     * @param fromDate start of the range
     * @param toDate end of the range
     * @return {@link TreeSet} of {@link LocalDate} representing all available dates.
     * @throws GenericException if any exception was raised getting the availabile dates.
     */
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
