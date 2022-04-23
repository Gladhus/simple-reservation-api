package com.gladhus.volcanocampingapi.v1.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gladhus.volcanocampingapi.AbstractMySQLContainerBasedTest;
import com.gladhus.volcanocampingapi.domain.Reservation;
import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import com.gladhus.volcanocampingapi.repository.ReservationRepository;
import com.gladhus.volcanocampingapi.v1.v1.dto.CreateReservationDto;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDataTestUtil;
import com.gladhus.volcanocampingapi.v1.v1.dto.ReservationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

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
        Reservation reservation = ReservationDataTestUtil.getReservationEntity();
        reservation = reservationRepository.save(reservation);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.1/reservation/" + reservation.getId()));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.id", is(equalTo(reservation.getId()))));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.email", is(equalTo(reservation.getEmail()))));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.fullName", is(equalTo(reservation.getFullName()))));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.status", is(equalTo(reservation.getStatus().toString()))));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.checkin", is(equalTo(reservation.getCheckin().toString()))));
        response.andExpect(MockMvcResultMatchers.jsonPath("$.checkout", is(equalTo(reservation.getCheckout().toString()))));
    }

    @Test
    void givenNoReservation_whenGetReservation_thenNotFound() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.1/reservation/" + "invalid-id"));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(equalTo("No reservation was found for provided reservation id."))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(equalTo(HttpStatus.NOT_FOUND.name()))));
    }

    @Test
    @Transactional
    void givenNoReservation_whenCreateReservation_thenReservation() throws Exception {
        CreateReservationDto createReservationDto = ReservationDataTestUtil.getCreateReservationDto();

        ResultActions responseCreate = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1.1/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        responseCreate.andExpect(MockMvcResultMatchers.status().isCreated());
        responseCreate.andExpect(MockMvcResultMatchers.jsonPath("$.email", is(equalTo(createReservationDto.getEmail()))));
        responseCreate.andExpect(MockMvcResultMatchers.jsonPath("$.fullName", is(equalTo(createReservationDto.getFullName()))));
        responseCreate.andExpect(MockMvcResultMatchers.jsonPath("$.status", is(equalTo(ReservationStatus.ACTIVE.toString()))));
        responseCreate.andExpect(MockMvcResultMatchers.jsonPath("$.checkin", is(equalTo(createReservationDto.getCheckin().toString()))));
        responseCreate.andExpect(MockMvcResultMatchers.jsonPath("$.checkout", is(equalTo(createReservationDto.getCheckout().toString()))));

        ReservationDto responseDto = objectMapper.readValue(responseCreate.andReturn().getResponse().getContentAsString(), ReservationDto.class);

        ResultActions responseGet = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.1/reservation/" + responseDto.getId()));

        responseGet.andExpect(MockMvcResultMatchers.status().isOk());
        responseGet.andExpect(MockMvcResultMatchers.jsonPath("$.id", is(equalTo(responseDto.getId()))));
        responseGet.andExpect(MockMvcResultMatchers.jsonPath("$.email", is(equalTo(createReservationDto.getEmail()))));
        responseGet.andExpect(MockMvcResultMatchers.jsonPath("$.fullName", is(equalTo(createReservationDto.getFullName()))));
        responseGet.andExpect(MockMvcResultMatchers.jsonPath("$.status", is(equalTo(responseDto.getStatus().toString()))));
        responseGet.andExpect(MockMvcResultMatchers.jsonPath("$.checkin", is(equalTo(createReservationDto.getCheckin().toString()))));
        responseGet.andExpect(MockMvcResultMatchers.jsonPath("$.checkout", is(equalTo(createReservationDto.getCheckout().toString()))));
    }

    @Test
    @Transactional
    void givenReservation_whenCreateReservationAtSameDates_thenForbidden() throws Exception {
        CreateReservationDto createReservationDto = ReservationDataTestUtil.getCreateReservationDto();
        Reservation reservationEntity = ReservationDataTestUtil.getReservationEntity(createReservationDto.getCheckin(), createReservationDto.getCheckout());

        reservationRepository.save(reservationEntity);

        ResultActions responseCreate = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1.1/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReservationDto)));

        responseCreate.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is(equalTo("The dates selected are not available."))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is(equalTo(HttpStatus.FORBIDDEN.name()))));
    }

}