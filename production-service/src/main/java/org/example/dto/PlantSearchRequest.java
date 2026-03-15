package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.PlantType;

@Getter
@Setter
public class PlantSearchRequest {

    private PlantType plantType;
    private String plantName;
    private Integer plantAge;
}
