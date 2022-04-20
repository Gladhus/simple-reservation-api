package com.gladhus.volcanocampingapi.service;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.exception.InvalidDatesException;
import com.gladhus.volcanocampingapi.exception.ReservationNotFoundException;
import com.gladhus.volcanocampingapi.repository.ReservationRepository;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Reservation createReservation(Reservation reservation) throws GenericException {

        // We only care about the date, not the time. So we truncate the Instant to Days.
        reservation.setCheckin(reservation.getCheckin().truncatedTo(ChronoUnit.DAYS));
        reservation.setCheckout(reservation.getCheckout().truncatedTo(ChronoUnit.DAYS));

        // Check if checkin date is before checkout date
        if (reservation.getCheckin().isAfter(reservation.getCheckout())) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The checkout date should be after the checkin date.");
        }

        // Check if reservation is < 3 days
        if (ChronoUnit.DAYS.between(reservation.getCheckin(), reservation.getCheckout()) > 3) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The checkout date cannot be more than 3 days after the checkin date.");
        }

        // Check if checkout is at least one day after checkin
        if (ChronoUnit.DAYS.between(reservation.getCheckin(), reservation.getCheckout()) < 1) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The checkout date should be at least a day after the checkin date.");
        }

        // TODO : Check if reservation is at least 1 day ahead of arrival
        // TODO : Check if reservation is less than a month in advance
        // TODO : Check if dates are available

        reservation.setStatus(ReservationStatus.ACTIVE);
        return reservationRepository.save(reservation);

    }

    public Reservation getReservation(String id) throws GenericException{
        return reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
    }
}
