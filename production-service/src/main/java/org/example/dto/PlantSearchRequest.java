package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.PlantType;

@Getter
@Setter
public class PlantSearchRequest {
    @NotNull(message = "Plant type is required")
    private PlantType plantType;
    @NotBlank(message = "Plant name is required")
    private String plantName;
    @NotNull(message = "Plant age is required")
    @Min(value = 0, message = "Plant age must be non-negative")
    private Integer plantAge;
}
