package com.gladhus.volcanocampingapi.v1.service;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import com.gladhus.volcanocampingapi.exception.GenericAPIException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Service class that handles all business logic surrounding reservations.
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Service operation that validates a reservation and saves it through the repository.
     * @param reservation The reservation to be created.
     * @return the {@link Reservation} that was created.
     * @throws InvalidDatesException if any validation fails on the reservation.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Reservation createReservation(Reservation reservation) throws InvalidDatesException {

        validateDatesForCreationOrUpdate(reservation);

        reservation.setStatus(ReservationStatus.ACTIVE);
        return reservationRepository.save(reservation);
    }

    /**
     * Service operation that merges new and old reservation, validates the new data and saves it through the repository.
     * @param newReservation reservation object that contains the values to update on the existing reservation.
     * @return the final {@link Reservation} after update.
     * @throws InvalidDatesException if any validation fails on the reservation.
     * @throws ReservationNotFoundException if no reservation was found for the id provided.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {GenericAPIException.class})
    public Reservation updateReservation(Reservation newReservation) throws InvalidDatesException, ReservationNotFoundException {

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

        validateDatesForCreationOrUpdate(newReservation);

        newReservation.setStatus(ReservationStatus.ACTIVE);
        return reservationRepository.save(newReservation);
    }

    /**
     * Service operation that finds a reservation by its id.
     * @param id id of the reservation to find.
     * @return the {@link Reservation} corresponding to the id provided.
     * @throws ReservationNotFoundException if no reservation was found for the id provided.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Reservation getReservation(String id) throws ReservationNotFoundException {
        return reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
    }

    /**
     * Service operation that sets the status of the reservation corresponding to the id provided
     * to {@link ReservationStatus#CANCELLED}
     * @param id id of the reservation to cancel.
     * @return the {@link Reservation} that was cancelled with the updated status.
     * @throws ReservationNotFoundException if no reservation was found for the id provided.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Reservation cancelReservation(String id) throws ReservationNotFoundException {
        Reservation reservation = reservationRepository.findByIdAndStatus(id, ReservationStatus.ACTIVE).orElseThrow(ReservationNotFoundException::new);

        reservation.setStatus(ReservationStatus.CANCELLED);

        return reservation;
    }

    /**
     * Service operation that returns all available reservation dates in the range provided.
     * This operation checks all active reservations within the date range and makes sure that all dates returned are
     * available at the moment of the call.
     * @param fromDate start of the range
     * @param toDate end of the range
     * @return {@link TreeSet} of {@link LocalDate} representing all available dates.
     * @throws InvalidDatesException if there is an error with the dates provided.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Set<LocalDate> getAvailabilities(LocalDate fromDate, LocalDate toDate) throws InvalidDatesException {

        // Check that the toDate is after fromDate
        if (!toDate.isAfter(fromDate)) {
            throw new InvalidDatesException("The toDate should be after the fromDate.");
        }

        // Check that the toDate is no more than one month in the future
        if (!toDate.isBefore(LocalDate.now().plusMonths(1).plusDays(1))) {
            throw new InvalidDatesException("The toDate cannot be more than a month in the future.");
        }

        List<Reservation> reservations = reservationRepository.findByCheckoutOrCheckinIsBetweenAndStatus(fromDate, toDate, fromDate, toDate, ReservationStatus.ACTIVE)
                .orElse(new ArrayList<>());

        return getAvailableDatesFromReservations(fromDate, toDate, reservations);
    }

    /**
     * Creates a list of all dates between the fromDate and toDate and then filters out the unavailable dates.
     */
    private Set<LocalDate> getAvailableDatesFromReservations(LocalDate fromDate, LocalDate toDate, List<Reservation> reservations) {
        Set<LocalDate> alreadyReservedDates = reservations.stream().flatMap(reservation -> reservation.getCheckin().datesUntil(reservation.getCheckout())).collect(Collectors.toSet());

        return fromDate.datesUntil(toDate.plusDays(1))
                .filter(date -> !alreadyReservedDates.contains(date))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Checks the selected dates for a reservation and validates that they are valid and available.
     */
    private void validateDatesForCreationOrUpdate(Reservation reservation) throws InvalidDatesException {
        // Abort if checkin date is after checkout date
        if (reservation.getCheckin().isAfter(reservation.getCheckout())) {
            throw new InvalidDatesException("The checkout date should be after the checkin date.");
        }

        // Abort if reservation is > 3 days
        if (ChronoUnit.DAYS.between(reservation.getCheckin(), reservation.getCheckout()) > 3) {
            throw new InvalidDatesException("The length of the stay cannot be longer than 3 days.");
        }

        // Abort if checkout is not at least one day after checkin
        if (reservation.getCheckout().isEqual(reservation.getCheckin())) {
            throw new InvalidDatesException("The checkout date should be at least a day after the checkin date.");
        }

        // Abort if the checkin same day as the reservation is made on
        if (!reservation.getCheckin().isAfter(LocalDate.now())) {
            throw new InvalidDatesException("The checkin date needs to be at least one day in the future.");
        }

        // Abort if checkout is more than a month in the future
        // We do not validate checkin date as it cannot be after checkout.
        if (!reservation.getCheckout().isBefore(LocalDate.now().plusMonths(1))) {
            throw new InvalidDatesException("The checkout date cannot be more than a month in the future.");
        }

        // Get all active reservation within date range
        List<Reservation> reservationsWithinDateRange =
                reservationRepository.findByCheckoutOrCheckinIsBetweenAndStatus_Pessimistic(
                                reservation.getCheckin(), reservation.getCheckout(),
                                reservation.getCheckin(), reservation.getCheckout(),
                                ReservationStatus.ACTIVE).orElse(new ArrayList<>())
                        .stream()
                        .filter(res -> !res.getId().equals(reservation.getId()))
                        .toList();

        // Abort that the dates selected are available
        if (!getAvailableDatesFromReservations(reservation.getCheckin(), reservation.getCheckout(), reservationsWithinDateRange)
                .containsAll(reservation.getCheckin().datesUntil(reservation.getCheckout()).toList())) {
            throw new InvalidDatesException("The dates selected are not available.");
        }
    }
}