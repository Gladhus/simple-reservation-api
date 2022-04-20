package com.gladhus.volcanocampingapi.repository;

import com.gladhus.volcanocampingapi.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
public interface ReservationRepository extends JpaRepository<Reservation, String> {
}