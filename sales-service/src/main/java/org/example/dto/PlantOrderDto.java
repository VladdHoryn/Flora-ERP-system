package org.example.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.domain.order.PlantType;

@Data
public class PlantOrderDto {
    @NotNull(message = "Plant type is required")
    private PlantType plantType;

    @NotBlank(message = "Plant name is required")
    private String plantName;

    @NotNull(message = "Plant age is required")
    @Min(value = 0, message = "Plant age must be non-negative")
    private Long plantAge;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long quantity;
}
