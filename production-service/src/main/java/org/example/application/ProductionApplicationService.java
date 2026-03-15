package org.example.application;

import jakarta.transaction.Transactional;
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

    @Transactional
    public Plant createPlant(Plant plant){
        PlantBatch plantBatch = plantBatchRepository.findById(plant.getBatchId())
                .orElseThrow(() -> new RuntimeException("Can not create plant with non existent Plant Batch"));

        plantBatch.incrementTotalCount(1);
        return plantRepository.save(plant);
    }

    @Transactional
    public Plant updatePlant(Long id, Plant plant){
        Plant existing = getPlantById(id);

        if(existing.getBatchId() != plant.getBatchId()){
            PlantBatch prevPlantBatch = plantBatchRepository.findById(existing.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Plant Batch was not found"));
            PlantBatch newPlantBatch = plantBatchRepository.findById(plant.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Plant Batch was not found"));

            prevPlantBatch.decrementTotalCount(1);
            newPlantBatch.incrementTotalCount(1);
        }

        existing.copy(plant);
        return plantRepository.save(existing);
    }

    @Transactional
    public void deletePlant(Long id){
        Plant plant = getPlantById(id);

        PlantBatch plantBatch = plantBatchRepository.findById(plant.getBatchId())
                .orElseThrow(() -> new RuntimeException("Plant Batch not found"));

        plantBatch.decrementTotalCount(1);
        plantRepository.deleteById(id);
    }

    public List<PlantBatch> getAllPlantBatches(){
        return plantBatchRepository.findAll();
    }

    @Transactional
    public PlantBatch createPlantBatch(PlantBatch plantBatch){
        return plantBatchRepository.save(plantBatch);
    }

    @Transactional
    public PlantBatch updatePlantBatch(Long batchId, PlantBatch plantBatch){
        PlantBatch existing = plantBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Plant batch with id: " + batchId + "was not found"));

        existing.copy(plantBatch);

        updateTotalCountForBatch(existing.getId());
        return plantBatchRepository.save(existing);
    }

    @Transactional
    public void deletePlantBatch(Long batchId){
        List<Plant> plants = plantRepository.findAllByBatchId(batchId);

        for (var plant : plants){
            deletePlant(plant.getId());
        }

        plantBatchRepository.deleteById(batchId);
    }

    public Integer findPlantsAmountInBatch(Long batchId){
        return plantRepository.findAllByBatchId(batchId).size();
    }

    @Transactional
    public PlantBatch updateTotalCountForBatch(Long batchId){
        PlantBatch plantBatch = plantBatchRepository.findById(batchId).orElseThrow(
                () -> new RuntimeException("Plant batch with id: " + batchId + "was not found"));

        plantBatch.setTotalCount(findPlantsAmountInBatch(batchId));

        return plantBatchRepository.save(plantBatch);
    }
}
