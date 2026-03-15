package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.inventory.PlantType;

public record PlantData(
        @NotNull(message = "Plant type is required")
        PlantType plantType,
        @NotBlank(message = "Plant name is required")
        String plantName,
        @NotNull(message = "Plant age is required")
        @Min(value = 0, message = "Plant age must be non-negative")
        Long plantAge,
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Long quantity
) {}
