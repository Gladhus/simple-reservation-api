package com.gladhus.volcanocampingapi.v1.v1.dto;

import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private String id;
    private String email;
    private String fullName;
    private ReservationStatus status;
    private LocalDate checkin;
    private LocalDate checkout;
}
