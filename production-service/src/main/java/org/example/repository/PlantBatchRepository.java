package org.example.repository;

import org.example.domain.plantbatch.PlantBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlantBatchRepository extends JpaRepository<PlantBatch, Long> {
    public List<PlantBatch> findByLocation(String location);

    public List<PlantBatch> findByPlantationDate(LocalDate plantedAt);
}
