package org.example.dto;

import lombok.Data;

@Data
public class ReservationResponse {
    private Long id;
    private Long userId;
    private ReservationStatus status;
}
