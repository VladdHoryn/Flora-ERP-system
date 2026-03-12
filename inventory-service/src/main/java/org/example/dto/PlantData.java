package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.inventory.PlantType;

public record PlantData(
        PlantType plantType,
        String plantName,
        Long plantAge,
        Long quantity
) {}
