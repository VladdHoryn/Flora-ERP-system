package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesDetailsResponse {
    private Long salesId;
    private Long userId;
    private String status;
    private ReservationResponse reservation;
}
