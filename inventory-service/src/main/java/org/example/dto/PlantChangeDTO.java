package org.example.dto;

import lombok.Data;
import org.example.domain.inventory.PlantType;

import java.time.LocalDateTime;

@Data
public class PlantChangeDTO {
    private Long plantId;
    private PlantType plantType;
    private String plantsName;
    private Long age;
    private int quantityChange;
    private String changeType;
    private LocalDateTime createdAt;
}
