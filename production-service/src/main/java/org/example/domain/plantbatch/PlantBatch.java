package org.example.domain.plantbatch;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.PlantType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PlantBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    PlantType plantType;
    String plantsName;
    String location;
    LocalDate plantedAt;
    int totalCount;

    public PlantBatch(PlantType plantType, String plantsName, String location, LocalDate plantedAt, int totalCount) {
        if (plantType == null) {
            throw new IllegalArgumentException("Plant type cannot be null");
        }
        if (plantsName == null || plantsName.isBlank()) {
            throw new IllegalArgumentException("Plant name cannot be null or blank");
        }
        if (location == null || location.isBlank())
            throw new IllegalArgumentException("Location cannot be null or blank");

        if (plantedAt == null)
            throw new IllegalArgumentException("Planted date cannot be null");

        if (totalCount < 0)
            throw new IllegalArgumentException("Total count cannot be negative");

        this.plantsName = plantsName;
        this.plantType = plantType;
        this.location = location;
        this.plantedAt = plantedAt;
        this.totalCount = totalCount;
    }

    public void incrementTotalCount(int delta) {
        if (delta <= 0)
            throw new IllegalArgumentException("Delta must be positive");

        this.totalCount += delta;
    }

    public void decrementTotalCount(int delta) {
        if (delta <= 0)
            throw new IllegalArgumentException("Delta must be positive");

        if (delta > this.totalCount)
            throw new IllegalArgumentException("Cannot remove more than total count");

        this.totalCount -= delta;
    }

    public void copy(PlantBatch plantBatch){
        this.plantType = plantBatch.plantType;
        this.plantsName = plantBatch.plantsName;
        this.location = plantBatch.location;
        this.plantedAt = plantBatch.plantedAt;
    }

    public long getAgeInMonths() {
        if (plantedAt == null) {
            return 0; // або кинути exception
        }

        return ChronoUnit.MONTHS.between(plantedAt, LocalDate.now());
    }
}
