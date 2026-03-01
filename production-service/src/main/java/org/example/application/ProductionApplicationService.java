package org.example.application;

import lombok.AllArgsConstructor;
import org.example.domain.plant.Plant;
import org.example.domain.plantbatch.PlantBatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.example.repository.PlantBatchRepository;
import org.example.repository.PlantRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductionApplicationService {
    private final PlantRepository plantRepository;
    private final PlantBatchRepository plantBatchRepository;

    public List<Plant> getAllPlants(){
        return plantRepository.findAll();
    }

    public Plant getPlantById(Long id){
        return plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found with id: " + id));
    }

    public Plant createPlant(Plant plant){
        return plantRepository.save(plant);
    }

    public Plant updatePlant(Long id, Plant plant){
        Plant existing = getPlantById(id);
        existing.copy(plant);
        return plantRepository.save(existing);
    }

    public void deletePlant(Long id){
        plantRepository.deleteById(id);
    }

    public List<PlantBatch> getAllPlantBatches(){
        return plantBatchRepository.findAll();
    }

    public PlantBatch createPlantBatch(PlantBatch plantBatch){
        return plantBatchRepository.save(plantBatch);
    }
}
