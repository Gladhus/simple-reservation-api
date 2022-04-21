package com.gladhus.volcanocampingapi.v1.v1.mapper;


import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDataTest;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.mapper.ReservationMapper;
import java.time.LocalDate;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReservationMapperTest {

    private final ReservationMapper testee = new ReservationMapper();

    @Test
    void mapToEntity() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();

        Reservation result = testee.mapToEntity(createReservationDto);

        assertThat(result.getFullName()).isEqualTo(createReservationDto.getFullName());
        assertThat(result.getEmail()).isEqualTo(createReservationDto.getEmail());
        assertThat(result.getCheckin()).isEqualTo(createReservationDto.getCheckin());
        assertThat(result.getCheckout()).isEqualTo(createReservationDto.getCheckout());
        assertNull(result.getId());
        assertNull(result.getStatus());
    }

    @Test
    void mapToEntity_withId() {
        CreateReservationDto createReservationDto = ReservationDataTest.getCreateReservationDto();

        Reservation result = testee.mapToEntity("id-test", createReservationDto);

        assertThat(result.getFullName()).isEqualTo(createReservationDto.getFullName());
        assertThat(result.getEmail()).isEqualTo(createReservationDto.getEmail());
        assertThat(result.getCheckin()).isEqualTo(createReservationDto.getCheckin());
        assertThat(result.getCheckout()).isEqualTo(createReservationDto.getCheckout());
        assertThat(result.getId()).isEqualTo("id-test");
        assertNull(result.getStatus());
    }

    @Test
    void mapToDto() {
        Reservation reservationEntity = ReservationDataTest.getReservationEntity();

        ReservationDto result = testee.mapToDto(reservationEntity);
        assertThat(result.getId()).isEqualTo(reservationEntity.getId());
        assertThat(result.getFullName()).isEqualTo(reservationEntity.getFullName());
        assertThat(result.getEmail()).isEqualTo(reservationEntity.getEmail());
        assertThat(result.getCheckin()).isEqualTo(reservationEntity.getCheckin());
        assertThat(result.getCheckout()).isEqualTo(reservationEntity.getCheckout());
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.ACTIVE);
    }
}