package com.gladhus.volcanocampingapi.domain;

import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Reservation {

    /**
     * Using UUID generation strategy in order to have unpredictable ids.
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    private String email;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDate checkin;

    private LocalDate checkout;



}
