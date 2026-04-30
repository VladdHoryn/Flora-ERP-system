package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private Long id;
    private Long userId;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
