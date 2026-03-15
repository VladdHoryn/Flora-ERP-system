package org.example.repository;

import org.example.domain.PlantType;
import org.example.domain.plant.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findAllByBatchId(Long id);

    List<Plant> findAllByPlantTypeAndName(PlantType plantType, String name);
}
