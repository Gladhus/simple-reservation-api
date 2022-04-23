package com.gladhus.volcanocampingapi.v1.v1.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationDto {
    private String email;
    private String fullName;
    private LocalDate checkin;
    private LocalDate checkout;
}
