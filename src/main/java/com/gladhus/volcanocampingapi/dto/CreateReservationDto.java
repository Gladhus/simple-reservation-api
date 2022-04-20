package com.gladhus.volcanocampingapi.dto;

import java.time.Instant;
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
    private Instant checkin;
    private Instant checkout;
}
