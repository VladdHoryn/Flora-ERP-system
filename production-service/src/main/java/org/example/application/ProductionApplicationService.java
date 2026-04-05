package org.example.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.OutboxEvent;
import org.example.infrastructure.config.InventoryServiceClient;
import org.example.domain.PlantType;
import org.example.domain.plant.Plant;
import org.example.domain.plantChangeLog.ChangeType;
import org.example.domain.plantChangeLog.PlantChangeLog;
import org.example.domain.plantbatch.PlantBatch;
import org.example.exception.PlantBatchNotFoundException;
import org.example.exception.PlantNotFoundException;
import org.example.repository.OutboxEventRepository;
import org.example.repository.PlantChangeLogRepository;
import org.springframework.stereotype.Service;
import org.example.repository.PlantBatchRepository;
import org.example.repository.PlantRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ProductionApplicationService {
    private final PlantRepository plantRepository;
    private final PlantBatchRepository plantBatchRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final PlantChangeLogRepository plantChangeLogRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public List<Plant> getAllPlants(){

        return plantRepository.findAll();
    }

    public Plant getPlantById(Long id){
        return plantRepository.findById(id)
                .orElseThrow(() -> new PlantNotFoundException(id));
    }

    @Transactional
    public Plant createPlant(Plant plant){
        PlantBatch plantBatch = plantBatchRepository.findById(plant.getBatchId())
                .orElseThrow(() -> new RuntimeException("Can not create plant with non existent Plant Batch"));

        plantBatch.incrementTotalCount(1);

        Plant saved = plantRepository.save(plant);

        createOutboxEventForChange(PlantChangeLog.builder()
                .plantId(saved.getId())
                .batchId(saved.getBatchId())
                .quantityChange(1)
                .changeType(ChangeType.CREATE)
                .createdAt(LocalDateTime.now())
                .build());
        return saved;
    }

    @Transactional
    public Plant updatePlant(Long id, Plant plant){
        Plant existing = getPlantById(id);

        if(existing.getBatchId() != plant.getBatchId()){
            PlantBatch prevPlantBatch = plantBatchRepository.findById(existing.getBatchId())
                    .orElseThrow(() -> new PlantBatchNotFoundException(existing.getBatchId()));
            PlantBatch newPlantBatch = plantBatchRepository.findById(plant.getBatchId())
                    .orElseThrow(() -> new PlantBatchNotFoundException(existing.getBatchId()));

            prevPlantBatch.decrementTotalCount(1);

            if(existing.isHealthy())
                createOutboxEventForChange(PlantChangeLog.builder()
                                .plantId(existing.getId())
                                .batchId(prevPlantBatch.getId())
                                .quantityChange(-1)
                                .changeType(ChangeType.UPDATE)
                                .createdAt(LocalDateTime.now())
                                .build());

            newPlantBatch.incrementTotalCount(1);

            if(plant.isHealthy())
                createOutboxEventForChange(
                        PlantChangeLog.builder()
                                .plantId(existing.getId())
                                .batchId(newPlantBatch.getId())
                                .quantityChange(1)
                                .changeType(ChangeType.UPDATE)
                                .createdAt(LocalDateTime.now())
                                .build()
                );
        }
        if(existing.isHealthy() && !plant.isHealthy())
            if(plant.isHealthy())
                createOutboxEventForChange(
                        PlantChangeLog.builder()
                                .plantId(existing.getId())
                                .batchId(existing.getBatchId())
                                .quantityChange(-1)
                                .changeType(ChangeType.DISEASE)
                                .createdAt(LocalDateTime.now())
                                .build()
                );
        else if(!existing.isHealthy() && plant.isHealthy())
                createOutboxEventForChange(
                        PlantChangeLog.builder()
                                .plantId(existing.getId())
                                .batchId(existing.getBatchId())
                                .quantityChange(1)
                                .changeType(ChangeType.DISEASE)
                                .createdAt(LocalDateTime.now())
                                .build()
                );

        existing.copy(plant);
        return plantRepository.save(existing);
    }

    @Transactional
    public void deletePlant(Long id){
        Plant plant = getPlantById(id);

        PlantBatch plantBatch = plantBatchRepository.findById(plant.getBatchId())
                .orElseThrow(() -> new PlantBatchNotFoundException());

        plantBatch.decrementTotalCount(1);

        createOutboxEventForChange(
                PlantChangeLog.builder()
                        .plantId(plant.getId())
                        .batchId(plant.getBatchId())
                        .quantityChange(-1)
                        .changeType(ChangeType.DELETE)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

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
                .orElseThrow(() -> new RuntimeException("Plant Batch was not found"));
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
        PlantBatch plantBatch = plantBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Plant Batch was not found"));
        plantBatch.setTotalCount(findPlantsAmountInBatch(batchId));

        return plantBatchRepository.save(plantBatch);
    }

    @Transactional
    public List<Long> findHealthyPlantIds(String plantName,
                                          PlantType plantType,
                                          Integer plantAge) {

        List<Plant> plants =
                plantRepository.findAllByPlantTypeAndName(plantType, plantName);

        return plants.stream()
                .filter(p -> p.getGrowthStage().getAge() == plantAge)
                .filter(Plant::isHealthy)
                .map(Plant::getId)
                .toList();
    }

    @Transactional
    public void createOutboxEventForChange(PlantChangeLog plantChangeLog) {
        try {
            String payloadJson = objectMapper.writeValueAsString(plantChangeLog);

            OutboxEvent event = new OutboxEvent(
                    "PRODUCTION",
                    UUID.randomUUID().toString(),
                    "PLANT_CHANGE",
                    payloadJson
            );
            outboxEventRepository.save(event);

            log.info("Create event for plant change: {}", payloadJson);
        } catch (JsonProcessingException e){
            log.error("Failed to create event for plant change id: {}", plantChangeLog.getPlantId());
        }
    }
}
