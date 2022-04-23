package com.gladhus.volcanocampingapi.repository;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Reservation> findByIdAndStatus(String id, ReservationStatus status);

    /**
     * This repository query uses a PESSIMISTIC_WRITE lock on the table to ensure repeatable reads
     * and avoid conflicts with concurrent requests.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<List<Reservation>> findByCheckoutIsBetweenOrCheckinIsBetweenAndStatus(LocalDate checkoutStartDate, LocalDate checkoutEndDate,
                                                                                   LocalDate checkinStartDate, LocalDate checkinEndDate,
                                                                                   ReservationStatus status);

    @Transactional(propagation = Propagation.MANDATORY)
    Optional<List<Reservation>> findByCheckinIsBetweenOrCheckoutIsBetweenAndStatus(LocalDate checkinStartDate, LocalDate checkinEndDate,
                                                                                              LocalDate checkoutStartDate, LocalDate checkoutEndDate,
                                                                                              ReservationStatus status);
}