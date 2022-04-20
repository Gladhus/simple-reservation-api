package com.gladhus.volcanocampingapi.v1.v1.dto;

import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import java.time.LocalDate;

/**
 * Util class to create entities or DTOs for other tests.
 * This helps reduce code duplication in tests and also make the tests easier to read.
 */
public class ReservationDataTest {

    public static CreateReservationDto getCreateReservationDto() {
        String fullName = "fullName-test";
        String email = "test@example.com";
        LocalDate checkinDate = LocalDate.now().plusDays(3);
        LocalDate checkoutDate = LocalDate.now().plusDays(5);

        return CreateReservationDto.builder()
                .fullName(fullName)
                .email(email)
                .checkin(checkinDate)
                .checkout(checkoutDate)
                .build();
    }

    public static Reservation getReservationEntity() {
        String id = "id-test";
        String fullName = "fullName-test";
        String email = "test@example.com";
        LocalDate checkinDate = LocalDate.now().plusDays(3);
        LocalDate checkoutDate = LocalDate.now().plusDays(5);

        return Reservation.builder()
                .id(id)
                .fullName(fullName)
                .email(email)
                .checkin(checkinDate)
                .checkout(checkoutDate)
                .status(ReservationStatus.ACTIVE)
                .build();
    }

}