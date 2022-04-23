package com.gladhus.volcanocampingapi.v1.v1.dto;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import java.time.LocalDate;

/**
 * Util class to create entities or DTOs for other tests.
 * This helps reduce code duplication in tests and also make the tests easier to read.
 */
public class ReservationDataTestUtil {

    public static ReservationDto getReservationDto() {
        return ReservationDto.builder()
                .id("id-test")
                .fullName("fullName-test")
                .email("test@example.com")
                .checkin(LocalDate.now().plusDays(3))
                .checkout(LocalDate.now().plusDays(5))
                .status(ReservationStatus.ACTIVE)
                .build();
    }

    public static CreateReservationDto getCreateReservationDto() {
        return CreateReservationDto.builder()
                .fullName("fullName-test")
                .email("test@example.com")
                .checkin(LocalDate.now().plusDays(3))
                .checkout(LocalDate.now().plusDays(5))
                .build();
    }

    public static Reservation getReservationPreCreate() {
        return Reservation.builder()
                .fullName("fullName-test")
                .email("test@example.com")
                .checkin(LocalDate.now().plusDays(3))
                .checkout(LocalDate.now().plusDays(5))
                .build();
    }

    public static Reservation getReservationPreCreate(LocalDate checkin, LocalDate checkout) {
        return Reservation.builder()
                .fullName("fullName-test")
                .email("test@example.com")
                .checkin(checkin)
                .checkout(checkout)
                .build();
    }

    public static Reservation getReservationEntity() {
        return Reservation.builder()
                .id("id-test")
                .fullName("fullName-test")
                .email("test@example.com")
                .checkin(LocalDate.now().plusDays(3))
                .checkout(LocalDate.now().plusDays(5))
                .status(ReservationStatus.ACTIVE)
                .build();
    }

    public static Reservation getReservationEntity(LocalDate checkin, LocalDate checkout) {
        return Reservation.builder()
                .id("id-test")
                .fullName("fullName-test")
                .email("test@example.com")
                .checkin(checkin)
                .checkout(checkout)
                .status(ReservationStatus.ACTIVE)
                .build();
    }

}