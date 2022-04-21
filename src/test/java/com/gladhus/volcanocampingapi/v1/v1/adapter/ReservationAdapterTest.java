package com.gladhus.volcanocampingapi.v1.v1.adapter;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.exception.GenericException;
import com.gladhus.volcanocampingapi.exception.ReservationNotFoundException;
import com.gladhus.volcanocampingapi.v1.service.ReservationService;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDataTest;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.mapper.ReservationMapper;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
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
class ReservationAdapterTest {

    private ReservationAdapter testee;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private ReservationService reservationService;

    @BeforeEach
    void setup() {
        testee = new ReservationAdapter(reservationMapper, reservationService);
    }

    @Test
    void createReservation() throws GenericException {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();
        Reservation reservationEntity = ReservationDataTest.getReservationEntity();
        ReservationDto reservationDto = ReservationDataTest.getReservationDto();

        when(reservationMapper.mapToEntity(createReservationDto)).thenReturn(reservationEntity);
        when(reservationService.createReservation(reservationEntity)).thenReturn(reservationEntity);
        when(reservationMapper.mapToDto(reservationEntity)).thenReturn(reservationDto);

        ReservationDto result = testee.createReservation(createReservationDto);

        assertThat(result).isEqualTo(reservationDto);
    }

    @Test
    void createReservation_emptyEmail() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();
        createReservationDto.setEmail("");

        assertThatThrownBy(() -> testee.createReservation(createReservationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email is required.");
    }

    @Test
    void createReservation_nullEmail() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();
        createReservationDto.setEmail(null);

        assertThatThrownBy(() -> testee.createReservation(createReservationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email is required.");
    }

    @Test
    void createReservation_emptyFullName() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();
        createReservationDto.setFullName("");

        assertThatThrownBy(() -> testee.createReservation(createReservationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Full name is required.");
    }

    @Test
    void createReservation_nullFullName() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();
        createReservationDto.setFullName(null);

        assertThatThrownBy(() -> testee.createReservation(createReservationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Full name is required.");
    }

    @Test
    void createReservation_nullCheckin() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();
        createReservationDto.setCheckin(null);

        assertThatThrownBy(() -> testee.createReservation(createReservationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Checkin date is required.");
    }

    @Test
    void createReservation_nullCheckout() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();
        createReservationDto.setCheckout(null);

        assertThatThrownBy(() -> testee.createReservation(createReservationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Checkout date is required.");
    }

    @Test
    void updateReservation() throws GenericException {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();
        Reservation reservationEntity = ReservationDataTest.getReservationEntity();
        ReservationDto reservationDto = ReservationDataTest.getReservationDto();

        when(reservationMapper.mapToEntity(reservationEntity.getId(), createReservationDto)).thenReturn(reservationEntity);
        when(reservationService.updateReservation(reservationEntity)).thenReturn(reservationEntity);
        when(reservationMapper.mapToDto(reservationEntity)).thenReturn(reservationDto);

        ReservationDto result = testee.updateReservation(reservationEntity.getId(), createReservationDto);

        assertThat(result).isEqualTo(reservationDto);
    }

    @Test
    void updateReservation_emptyId() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();

        assertThatThrownBy(() -> testee.updateReservation("", createReservationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation id is required.");
    }

    @Test
    void updateReservation_nullId() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();

        assertThatThrownBy(() -> testee.updateReservation(null, createReservationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation id is required.");
    }

    @Test
    void getReservation() throws GenericException {
        Reservation reservationEntity = ReservationDataTest.getReservationEntity();
        ReservationDto reservationDto = ReservationDataTest.getReservationDto();

        when(reservationService.getReservation(reservationEntity.getId())).thenReturn(reservationEntity);
        when(reservationMapper.mapToDto(reservationEntity)).thenReturn(reservationDto);

        ReservationDto result = testee.getReservation(reservationEntity.getId());

        assertThat(result).isEqualTo(reservationDto);
    }

    @Test
    void getReservation_emptyId() {
        assertThatThrownBy(() -> testee.getReservation(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation id is required.");
    }

    @Test
    void getReservation_nullId() {
        assertThatThrownBy(() -> testee.getReservation(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation id is required.");
    }

    @Test
    void cancelReservation() throws GenericException {
        Reservation reservationEntity = ReservationDataTest.getReservationEntity();
        ReservationDto reservationDto = ReservationDataTest.getReservationDto();

        when(reservationService.cancelReservation(reservationEntity.getId())).thenReturn(reservationEntity);
        when(reservationMapper.mapToDto(reservationEntity)).thenReturn(reservationDto);

        ReservationDto result = testee.cancelReservation(reservationEntity.getId());

        assertThat(result).isEqualTo(reservationDto);
    }

    @Test
    void cancelReservation_emptyId() {
        assertThatThrownBy(() -> testee.cancelReservation(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation id is required.");
    }

    @Test
    void cancelReservation_nullId() {
        assertThatThrownBy(() -> testee.cancelReservation(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation id is required.");
    }

    @Test
    void getAvailabilities() throws GenericException {
        LocalDate fromDate = LocalDate.now().plusDays(1);
        LocalDate toDate = LocalDate.now().plusDays(10);

        Set<LocalDate> availabilities = new TreeSet<>(fromDate.datesUntil(toDate).toList());

        when(reservationService.getAvailabilities(fromDate, toDate)).thenReturn(availabilities);

        Set<LocalDate> result = testee.getAvailabilities(fromDate, toDate);

        assertThat(result).isEqualTo(availabilities);
    }

    @Test
    void getAvailabilities_nullFromDate() throws GenericException {
        LocalDate fromDate = null;
        LocalDate toDate = LocalDate.now().plusDays(10);

        Set<LocalDate> availabilities = new TreeSet<>(LocalDate.now().datesUntil(toDate).toList());

        when(reservationService.getAvailabilities(LocalDate.now(), toDate)).thenReturn(availabilities);

        Set<LocalDate> result = testee.getAvailabilities(fromDate, toDate);

        assertThat(result).isEqualTo(availabilities);
    }

    @Test
    void getAvailabilities_nullToDate() throws GenericException {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = null;

        Set<LocalDate> availabilities = new TreeSet<>(fromDate.datesUntil(LocalDate.now().plusDays(10)).toList());

        when(reservationService.getAvailabilities(fromDate, LocalDate.now().plusMonths(1))).thenReturn(availabilities);

        Set<LocalDate> result = testee.getAvailabilities(fromDate, toDate);

        assertThat(result).isEqualTo(availabilities);
    }

    @Test
    void getAvailabilities_nullToDate_nullFromDate() throws GenericException {
        LocalDate fromDate = null;
        LocalDate toDate = null;

        Set<LocalDate> availabilities = new TreeSet<>(LocalDate.now().datesUntil(LocalDate.now().plusDays(10)).toList());

        when(reservationService.getAvailabilities(LocalDate.now(), LocalDate.now().plusMonths(1))).thenReturn(availabilities);

        Set<LocalDate> result = testee.getAvailabilities(fromDate, toDate);

        assertThat(result).isEqualTo(availabilities);
    }
}