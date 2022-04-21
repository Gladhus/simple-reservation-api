package com.gladhus.volcanocampingapi.v1.service;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.exception.InvalidDatesException;
import com.gladhus.volcanocampingapi.exception.ReservationNotFoundException;
import com.gladhus.volcanocampingapi.repository.ReservationRepository;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDataTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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
                .hasMessage("No reservation was found for provided reservation id.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
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
                .hasMessage("No reservation was found for provided reservation id.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    void getAvailabilities() throws GenericException {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusDays(10);

        List<Reservation> reservationList = List.of(
                ReservationDataTest.getReservationEntity(LocalDate.now(), LocalDate.now().plusDays(2)),
                ReservationDataTest.getReservationEntity(LocalDate.now().plusDays(4), LocalDate.now().plusDays(5)));

        when(reservationRepository.findByCheckinIsBetweenOrCheckoutIsBetweenAndStatus(fromDate, toDate, fromDate, toDate, ReservationStatus.ACTIVE))
                .thenReturn(Optional.of(reservationList));

        Set<LocalDate> result = testee.getAvailabilities(fromDate, toDate);

        assertThat(result.size()).isEqualTo(8);
        assertThat(result.contains(LocalDate.now())).isFalse();
        assertThat(result.contains(LocalDate.now().plusDays(1))).isFalse();
        assertThat(result.contains(LocalDate.now().plusDays(4))).isFalse();
    }

    @Test
    void getAvailabilities_OverAMonthInTheFuture() throws GenericException {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusDays(32);

        assertThatThrownBy(() -> testee.getAvailabilities(fromDate, toDate))
                .isInstanceOf(InvalidDatesException.class)
                .hasMessage("The toDate cannot be more than a month in the future.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAvailabilities_ToDateBeforeFromDate() throws GenericException {
        LocalDate fromDate = LocalDate.now().plusDays(2);
        LocalDate toDate = LocalDate.now();

        assertThatThrownBy(() -> testee.getAvailabilities(fromDate, toDate))
                .isInstanceOf(InvalidDatesException.class)
                .hasMessage("The toDate should be after the fromDate.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void createReservation() throws GenericException {
        Reservation reservationInput = ReservationDataTest.getReservationPreCreate();
        Reservation reservationOutput = ReservationDataTest.getReservationEntity();
        Reservation reservationForInsert = ReservationDataTest.getReservationPreCreate();
        reservationForInsert.setStatus(ReservationStatus.ACTIVE);

        when(reservationRepository.findByCheckoutIsBetweenOrCheckinIsBetweenAndStatus(
                            reservationInput.getCheckin(), reservationInput.getCheckout(),
                            reservationInput.getCheckin(), reservationInput.getCheckout(),
                            ReservationStatus.ACTIVE))
                .thenReturn(Optional.empty());

        when(reservationRepository.save(reservationForInsert)).thenReturn(reservationOutput);

        Reservation result = testee.createReservation(reservationInput);

        assertThat(result).isEqualTo(reservationOutput);
    }

    @Test
    void createReservation_CheckinAfterCheckout() {
        Reservation reservation = ReservationDataTest
                .getReservationPreCreate(LocalDate.now().plusDays(3), LocalDate.now().plusDays(1));

        assertThatThrownBy(() -> testee.createReservation(reservation))
                .isInstanceOf(InvalidDatesException.class)
                .hasMessage("The checkout date should be after the checkin date.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void createReservation_LongerThan3Days() {
        Reservation reservation = ReservationDataTest
                .getReservationPreCreate(LocalDate.now().plusDays(1), LocalDate.now().plusDays(5));

        assertThatThrownBy(() -> testee.createReservation(reservation))
                .isInstanceOf(InvalidDatesException.class)
                .hasMessage("The length of the stay cannot be longer than 3 days.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void createReservation_CheckoutLaterThanCheckin() {
        Reservation reservation = ReservationDataTest
                .getReservationPreCreate(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1));

        assertThatThrownBy(() -> testee.createReservation(reservation))
                .isInstanceOf(InvalidDatesException.class)
                .hasMessage("The checkout date should be at least a day after the checkin date.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void createReservation_AtLeastOneDayAfterToday() {
        Reservation reservation = ReservationDataTest
                .getReservationPreCreate(LocalDate.now(), LocalDate.now().plusDays(1));

        assertThatThrownBy(() -> testee.createReservation(reservation))
                .isInstanceOf(InvalidDatesException.class)
                .hasMessage("The checkin date needs to be at least one day in the future.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void createReservation_CheckoutIsFurtherThanAMonth() {
        Reservation reservation = ReservationDataTest
                .getReservationPreCreate(LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(1).plusDays(2));

        assertThatThrownBy(() -> testee.createReservation(reservation))
                .isInstanceOf(InvalidDatesException.class)
                .hasMessage("The checkout date cannot be more than a month in the future.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    void createReservation_DatesUnavailable() {
        Reservation reservation = ReservationDataTest
                .getReservationPreCreate(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
        Reservation reservationAlreadyDone = ReservationDataTest.getReservationEntity(LocalDate.now(), LocalDate.now().plusDays(2));

        when(reservationRepository.findByCheckoutIsBetweenOrCheckinIsBetweenAndStatus(
                reservation.getCheckin(), reservation.getCheckout(),
                reservation.getCheckin(), reservation.getCheckout(),
                ReservationStatus.ACTIVE))
                .thenReturn(Optional.of(List.of(reservationAlreadyDone)));

        assertThatThrownBy(() -> testee.createReservation(reservation))
                .isInstanceOf(InvalidDatesException.class)
                .hasMessage("The dates selected are not available.")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }
}