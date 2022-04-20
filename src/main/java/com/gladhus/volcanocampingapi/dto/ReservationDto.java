package com.gladhus.volcanocampingapi.dto;

import com.gladhus.volcanocampingapi.domain.ReservationStatus;
import java.time.Instant;
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
    private Instant checkin;
    private Instant checkout;
}
