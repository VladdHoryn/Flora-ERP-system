package org.example.inventoryService.domain.plantbatch;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class PlantBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String location;
    LocalDate plantedAt;
    int totalCount;

    public PlantBatch(String location,
                      LocalDate plantedAt,
                      int totalCount){
        this.location = location;
        this.plantedAt = plantedAt;
        this.totalCount = 0;
    }
}
