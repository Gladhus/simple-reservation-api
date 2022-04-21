package com.gladhus.volcanocampingapi.v1.service;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.exception.InvalidDatesException;
import com.gladhus.volcanocampingapi.exception.ReservationNotFoundException;
import com.gladhus.volcanocampingapi.repository.ReservationRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Reservation createReservation(Reservation reservation) throws GenericException {

        validateDates(reservation);

        reservation.setStatus(ReservationStatus.ACTIVE);
        return reservationRepository.save(reservation);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {GenericException.class})
    public Reservation updateReservation(Reservation newReservation) throws GenericException {

        Reservation oldReservation = reservationRepository.findById(newReservation.getId()).orElseThrow(ReservationNotFoundException::new);

        if (newReservation.getCheckin() == null) {
            newReservation.setCheckin(oldReservation.getCheckin());
        }

        if (newReservation.getCheckout() == null) {
            newReservation.setCheckout(oldReservation.getCheckout());
        }

        if (!StringUtils.hasText(newReservation.getFullName())) {
            newReservation.setFullName(oldReservation.getFullName());
        }

        if (!StringUtils.hasText(newReservation.getEmail())) {
            newReservation.setEmail(oldReservation.getEmail());
        }

        validateDates(newReservation, true);

        newReservation.setStatus(ReservationStatus.ACTIVE);
        return reservationRepository.save(newReservation);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Reservation getReservation(String id) throws GenericException{
        return reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Reservation cancelReservation(String id) throws GenericException {
        Reservation reservation = reservationRepository.findByIdAndStatus(id, ReservationStatus.ACTIVE).orElseThrow(ReservationNotFoundException::new);

        reservation.setStatus(ReservationStatus.CANCELLED);

        return reservation;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Set<LocalDate> getAvailabilities(LocalDate fromDate, LocalDate toDate) throws GenericException {

        // Check that the toDate is after fromDate
        if (!toDate.isAfter(fromDate)) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The toDate should be after the fromDate.");
        }

        // Check that the toDate is no more than one month in the future
        if (!toDate.isBefore(LocalDate.now().plusMonths(1).plusDays(1))) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The toDate cannot be more than a month in the future.");
        }

        List<Reservation> reservations = reservationRepository.findByCheckinIsBetweenOrCheckoutIsBetweenAndStatus(fromDate, toDate, fromDate, toDate, ReservationStatus.ACTIVE)
                .orElse(new ArrayList<>());

        return getAvailableDatesFromReservations(fromDate, toDate, reservations);
    }

    private Set<LocalDate> getAvailableDatesFromReservations(LocalDate fromDate, LocalDate toDate, List<Reservation> reservations) {
        Set<LocalDate> alreadyReservedDates = reservations.stream().flatMap(reservation -> reservation.getCheckin().datesUntil(reservation.getCheckout())).collect(Collectors.toSet());

        List<LocalDate> localDateStream = fromDate.datesUntil(toDate.plusDays(1)).toList();

        return fromDate.datesUntil(toDate.plusDays(1))
                .filter(date -> !alreadyReservedDates.contains(date))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private void validateDates(Reservation reservation) throws InvalidDatesException {
        validateDates(reservation, false);
    }

    private void validateDates(Reservation reservation, boolean excludeCurrentReservation) throws InvalidDatesException {
        // Check if checkin date is before checkout date
        if (reservation.getCheckin().isAfter(reservation.getCheckout())) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The checkout date should be after the checkin date.");
        }

        // Check if reservation is < 3 days
        if (ChronoUnit.DAYS.between(reservation.getCheckin(), reservation.getCheckout()) > 3) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The length of the stay cannot be longer than 3 days.");
        }

        // Check if checkout is at least one day after checkin
        if (reservation.getCheckout().isEqual(reservation.getCheckin())) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The checkout date should be at least a day after the checkin date.");
        }

        // Check if the checkin is at least one day in the future
        if (!reservation.getCheckin().isAfter(LocalDate.now())) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The checkin date needs to be at least one day in the future.");
        }

        // Check that the checkout date is no more than one month in the future
        if (!reservation.getCheckout().isBefore(LocalDate.now().plusMonths(1))) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The checkout date cannot be more than a month in the future.");
        }

        List<Reservation> reservationsWithinDateRange =
                reservationRepository.findByCheckoutIsBetweenOrCheckinIsBetweenAndStatus(
                                reservation.getCheckin(), reservation.getCheckout(),
                                reservation.getCheckin(), reservation.getCheckout(),
                                ReservationStatus.ACTIVE).orElse(new ArrayList<>())
                        .stream()
                        .filter(res -> !res.getId().equals(reservation.getId()))
                        .toList();

        // Check that the dates selected are available
        if (!getAvailableDatesFromReservations(reservation.getCheckin(), reservation.getCheckout(), reservationsWithinDateRange)
                .containsAll(reservation.getCheckin().datesUntil(reservation.getCheckout()).toList())) {
            throw new InvalidDatesException(HttpStatus.BAD_REQUEST, "The dates selected are not available.");
        }

    }
}