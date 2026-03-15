package org.example.repository;

import org.example.domain.inventory.PlantInventory;
import org.example.domain.inventory.PlantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantInventoryRepository extends JpaRepository<PlantInventory, Long> {
    Optional<PlantInventory> findByPlantTypeAndPlantsNameAndAge(
            PlantType plantType,
            String plantsName,
            Long age
    );

    List<PlantInventory> findByPlantType(PlantType plantType);
}
