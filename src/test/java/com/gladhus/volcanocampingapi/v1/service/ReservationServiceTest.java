package com.gladhus.volcanocampingapi.v1.service;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.exception.ReservationNotFoundException;
import com.gladhus.volcanocampingapi.repository.ReservationRepository;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDataTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class ReservationServiceTest {

    ReservationService testee;

    @Mock
    ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        testee = new ReservationService(reservationRepository);
    }

    @Test
    void getReservation() throws GenericException {
        Reservation reservationEntity = ReservationDataTest.getReservationEntity();

        when(reservationRepository.findById(reservationEntity.getId())).thenReturn(Optional.of(reservationEntity));

        Reservation result = testee.getReservation(reservationEntity.getId());

        assertThat(result).isEqualTo(reservationEntity);
    }

    @Test
    void getReservation_NotFound() {
        Reservation reservationEntity = ReservationDataTest.getReservationEntity();

        when(reservationRepository.findById(reservationEntity.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testee.getReservation(reservationEntity.getId()))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessage("No reservation was found for provided reservation id.");
    }

    @Test
    void cancelReservation() throws GenericException {
        Reservation reservationEntity = ReservationDataTest.getReservationEntity();

        when(reservationRepository.findByIdAndStatus(reservationEntity.getId(), ReservationStatus.ACTIVE))
                .thenReturn(Optional.of(reservationEntity));

        Reservation result = testee.cancelReservation(reservationEntity.getId());

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(result.getId()).isEqualTo(reservationEntity.getId());
        assertThat(result.getFullName()).isEqualTo(reservationEntity.getFullName());
        assertThat(result.getEmail()).isEqualTo(reservationEntity.getEmail());
        assertThat(result.getCheckin()).isEqualTo(reservationEntity.getCheckin());
        assertThat(result.getCheckout()).isEqualTo(reservationEntity.getCheckout());
    }

    @Test
    void cancelReservation_NotFound() {
        Reservation reservationEntity = ReservationDataTest.getReservationEntity();

        when(reservationRepository.findByIdAndStatus(reservationEntity.getId(), ReservationStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> testee.cancelReservation(reservationEntity.getId()))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessage("No reservation was found for provided reservation id.");
    }
}