package org.example.inventoryService.repository;

import org.example.inventoryService.domain.plantbatch.PlantBatch;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface PlantBatchRepository extends JpaRepository<PlantBatch, Long> {
    public List<PlantBatch> findByLocation(String location);

    public List<PlantBatch> findByPlantationDate(LocalDate plantedAt);
}
