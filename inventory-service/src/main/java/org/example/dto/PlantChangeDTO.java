package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlantChangeDTO {
    private Long plantId;
    private Long batchId;
    private int quantityChange;
    private String changeType;
    private LocalDateTime createdAt;
}
