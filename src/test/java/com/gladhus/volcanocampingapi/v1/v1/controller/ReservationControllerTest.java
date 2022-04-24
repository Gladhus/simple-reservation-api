package com.gladhus.volcanocampingapi.v1.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladhus.volcanocampingapi.AbstractMySQLContainerBasedTest;
import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import com.gladhus.volcanocampingapi.repository.ReservationRepository;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;


import static com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDataTestUtil.getCreateReservationDto;
import static com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDataTestUtil.getReservationEntity;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class ReservationControllerTest extends AbstractMySQLContainerBasedTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void givenReservation_whenGetReservation_thenReservation() throws Exception {
        Reservation reservationEntity = reservationRepository.save(getReservationEntity());

        ResultActions response = mockMvc.perform(get("/api/v1.1/reservation/" + reservationEntity.getId()));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.id", is(equalTo(reservationEntity.getId()))));
        response.andExpect(jsonPath("$.email", is(equalTo(reservationEntity.getEmail()))));
        response.andExpect(jsonPath("$.fullName", is(equalTo(reservationEntity.getFullName()))));
        response.andExpect(jsonPath("$.status", is(equalTo(reservationEntity.getStatus().toString()))));
        response.andExpect(jsonPath("$.checkin", is(equalTo(reservationEntity.getCheckin().toString()))));
        response.andExpect(jsonPath("$.checkout", is(equalTo(reservationEntity.getCheckout().toString()))));
    }

    @Test
    void givenNoReservation_whenGetReservation_thenNotFound() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1.1/reservation/" + "invalid-id"));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(equalTo("No reservation was found for provided reservation id."))))
                .andExpect(jsonPath("$.status", is(equalTo(HttpStatus.NOT_FOUND.name()))));
    }

    @Test
    @Transactional
    void givenNoReservation_whenCreateReservation_thenReservation() throws Exception {
        CreateReservationDto createReservationDto = getCreateReservationDto();

        ResultActions responseCreate = mockMvc.perform(post("/api/v1.1/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        responseCreate.andExpect(status().isCreated());
        responseCreate.andExpect(jsonPath("$.email", is(equalTo(createReservationDto.getEmail()))));
        responseCreate.andExpect(jsonPath("$.fullName", is(equalTo(createReservationDto.getFullName()))));
        responseCreate.andExpect(jsonPath("$.status", is(equalTo(ReservationStatus.ACTIVE.toString()))));
        responseCreate.andExpect(jsonPath("$.checkin", is(equalTo(createReservationDto.getCheckin().toString()))));
        responseCreate.andExpect(jsonPath("$.checkout", is(equalTo(createReservationDto.getCheckout().toString()))));

        ReservationDto responseDto = objectMapper.readValue(responseCreate.andReturn().getResponse().getContentAsString(), ReservationDto.class);

        ResultActions responseGet = mockMvc.perform(get("/api/v1.1/reservation/" + responseDto.getId()));

        responseGet.andExpect(status().isOk());
        responseGet.andExpect(jsonPath("$.id", is(equalTo(responseDto.getId()))));
        responseGet.andExpect(jsonPath("$.email", is(equalTo(createReservationDto.getEmail()))));
        responseGet.andExpect(jsonPath("$.fullName", is(equalTo(createReservationDto.getFullName()))));
        responseGet.andExpect(jsonPath("$.status", is(equalTo(responseDto.getStatus().toString()))));
        responseGet.andExpect(jsonPath("$.checkin", is(equalTo(createReservationDto.getCheckin().toString()))));
        responseGet.andExpect(jsonPath("$.checkout", is(equalTo(createReservationDto.getCheckout().toString()))));
    }

    @Test
    @Transactional
    void givenReservation_whenCreateReservationAtSameDates_thenForbidden() throws Exception {
        CreateReservationDto createReservationDto = getCreateReservationDto();
        reservationRepository.save(getReservationEntity(createReservationDto.getCheckin(), createReservationDto.getCheckout()));

        ResultActions response = mockMvc.perform(post("/api/v1.1/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        response.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", is(equalTo("The dates selected are not available."))))
                .andExpect(jsonPath("$.status", is(equalTo(HttpStatus.FORBIDDEN.name()))));
    }

    @Test
    @Transactional
    void givenReservation_whenUpdateReservation_thenUpdatedReservation() throws Exception {
        Reservation reservationEntity = reservationRepository.save(getReservationEntity());
        CreateReservationDto createReservationDto = CreateReservationDto.builder()
                .email("newEmail")
                .fullName("newFullName")
                .checkin(reservationEntity.getCheckin().plusDays(1))
                .checkout(reservationEntity.getCheckout().plusDays(1))
                .build();

        ResultActions response = mockMvc.perform(put("/api/v1.1/reservation/" + reservationEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(reservationEntity.getId()))))
                .andExpect(jsonPath("$.email", is(equalTo(createReservationDto.getEmail()))))
                .andExpect(jsonPath("$.fullName", is(equalTo(createReservationDto.getFullName()))))
                .andExpect(jsonPath("$.checkin", is(createReservationDto.getCheckin().toString())))
                .andExpect(jsonPath("$.checkout", is(createReservationDto.getCheckout().toString())))
                .andExpect(jsonPath("$.status", is(equalTo(reservationEntity.getStatus().toString()))));

    }

    @Test
    @Transactional
    void givenNoReservation_whenUpdateReservation_thenNotFound() throws Exception {
        CreateReservationDto createReservationDto = getCreateReservationDto();

        ResultActions response = mockMvc.perform(put("/api/v1.1/reservation/" + "test-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(equalTo("No reservation was found for provided reservation id."))))
                .andExpect(jsonPath("$.status", is(equalTo(HttpStatus.NOT_FOUND.name()))));
    }

    @Test
    @Transactional
    void givenReservation_whenUpdateReservationToUnavailableDates_thenForbidden() throws Exception {
        Reservation reservationEntityExisting = reservationRepository
                .save(getReservationEntity(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));
        Reservation reservationEntityToUpdate = reservationRepository
                .save(getReservationEntity(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));
        CreateReservationDto createReservationDto =
                getCreateReservationDto(reservationEntityExisting.getCheckin(), reservationEntityExisting.getCheckout());

        ResultActions response = mockMvc.perform(put("/api/v1.1/reservation/" + reservationEntityToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        response.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", is(equalTo("The dates selected are not available."))))
                .andExpect(jsonPath("$.status", is(equalTo(HttpStatus.FORBIDDEN.name()))));
    }

    @Test
    @Transactional
    void givenReservation_whenUpdateReservationWithNoBody_thenBadRequest() throws Exception {
        Reservation reservationEntityToUpdate = reservationRepository
                .save(getReservationEntity(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));

        ResultActions response = mockMvc.perform(put("/api/v1.1/reservation/" + reservationEntityToUpdate.getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void givenReservation_whenCancelReservation_thenCancelledReservation() throws Exception {
        Reservation reservationEntity = reservationRepository.save(getReservationEntity());

        ResultActions response = mockMvc.perform(delete("/api/v1.1/reservation/" + reservationEntity.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(equalTo(reservationEntity.getId()))))
                .andExpect(jsonPath("$.email", is(equalTo(reservationEntity.getEmail()))))
                .andExpect(jsonPath("$.fullName", is(equalTo(reservationEntity.getFullName()))))
                .andExpect(jsonPath("$.checkin", is(reservationEntity.getCheckin().toString())))
                .andExpect(jsonPath("$.checkout", is(reservationEntity.getCheckout().toString())))
                .andExpect(jsonPath("$.status", is(equalTo(ReservationStatus.CANCELLED.toString()))));
    }

    @Test
    @Transactional
    void givenNoReservation_whenCancelReservation_thenNotFound() throws Exception {
        ResultActions response = mockMvc.perform(delete("/api/v1.1/reservation/" + "test-id"));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(equalTo("No reservation was found for provided reservation id."))))
                .andExpect(jsonPath("$.status", is(equalTo(HttpStatus.NOT_FOUND.name()))));
    }

    @Test
    @Transactional
    void givenReservation_whenGetAvailabilities_thenNotAvailableOnReservedDates() throws Exception {
        Reservation reservationEntity = reservationRepository.save(getReservationEntity());

        ResultActions response = mockMvc.perform(get("/api/v1.1/reservation/availabilities")
                .param("fromDate", reservationEntity.getCheckin().minusDays(1).toString())
                .param("toDate", reservationEntity.getCheckout().toString()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(equalTo(2))))
                .andExpect(jsonPath("$.[0]", is(equalTo(reservationEntity.getCheckin().minusDays(1).toString()))))
                .andExpect(jsonPath("$.[1]", is(equalTo(reservationEntity.getCheckout().toString()))));
    }

    @Test
    @Transactional
    void givenCancelledReservation_whenGetAvailabilities_thenAvailableOnAllDates() throws Exception {
        Reservation tmpEntity = getReservationEntity();
        tmpEntity.setStatus(ReservationStatus.CANCELLED);
        Reservation reservationEntity = reservationRepository.save(tmpEntity);

        ResultActions response = mockMvc.perform(get("/api/v1.1/reservation/availabilities")
                .param("fromDate", reservationEntity.getCheckin().minusDays(1).toString())
                .param("toDate", reservationEntity.getCheckout().toString()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(equalTo(4))))
                .andExpect(jsonPath("$.[0]", is(equalTo(reservationEntity.getCheckin().minusDays(1).toString()))))
                .andExpect(jsonPath("$.[1]", is(equalTo(reservationEntity.getCheckin().toString()))))
                .andExpect(jsonPath("$.[2]", is(equalTo(reservationEntity.getCheckin().plusDays(1).toString()))))
                .andExpect(jsonPath("$.[3]", is(equalTo(reservationEntity.getCheckout().toString()))));
    }

}