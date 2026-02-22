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

    public PlantBatch(String location, LocalDate plantedAt, int totalCount) {
        if (location == null || location.isBlank())
            throw new IllegalArgumentException("Location cannot be null or blank");

        if (plantedAt == null)
            throw new IllegalArgumentException("Planted date cannot be null");

        if (totalCount < 0)
            throw new IllegalArgumentException("Total count cannot be negative");

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
}
