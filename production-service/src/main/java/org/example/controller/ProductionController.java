package org.example.controller;

import org.example.application.ProductionApplicationService;
import org.example.domain.PlantType;
import org.example.domain.plant.Plant;
import lombok.RequiredArgsConstructor;
import org.example.domain.plantbatch.PlantBatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/production/v1")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionApplicationService productionApplicationService;

    // ---------------- PLANTS ----------------

    @GetMapping("/plants")
    public List<Plant> getAllPlants() {
        return productionApplicationService.getAllPlants();
    }

    @GetMapping("/plants/{id}")
    public Plant getPlantById(@PathVariable Long id) {
        return productionApplicationService.getPlantById(id);
    }

    @PostMapping("/plants")
    public Plant createPlant(@RequestBody Plant plant) {
        return productionApplicationService.createPlant(plant);
    }

    @PutMapping("/plants/{id}")
    public Plant updatePlant(@PathVariable Long id,
                             @RequestBody Plant plant) {
        return productionApplicationService.updatePlant(id, plant);
    }

    @DeleteMapping("/plants/{id}")
    public void deletePlant(@PathVariable Long id) {
        productionApplicationService.deletePlant(id);
    }

    // ---------------- PLANT BATCHES ----------------

    @GetMapping("/batches")
    public List<PlantBatch> getAllBatches() {
        return productionApplicationService.getAllPlantBatches();
    }

    @PostMapping("/batches")
    public PlantBatch createBatch(@RequestBody PlantBatch plantBatch) {
        return productionApplicationService.createPlantBatch(plantBatch);
    }

    @PutMapping("/batches/{id}")
    public PlantBatch updateBatch(@PathVariable Long id,
                                  @RequestBody PlantBatch plantBatch) {
        return productionApplicationService.updatePlantBatch(id, plantBatch);
    }

    @DeleteMapping("/batches/{id}")
    public void deleteBatch(@PathVariable Long id) {
        productionApplicationService.deletePlantBatch(id);
    }

    @GetMapping("/batches/{id}/plants/count")
    public Integer getPlantsCount(@PathVariable Long id) {
        return productionApplicationService.findPlantsAmountInBatch(id);
    }

    @PutMapping("/batches/{id}/recount")
    public PlantBatch recountBatch(@PathVariable Long id) {
        return productionApplicationService.updateTotalCountForBatch(id);
    }

    @GetMapping("/plants/healthy")
    public List<Long> findHealthyPlantIds(
            @RequestParam String plantName,
            @RequestParam PlantType plantType,
            @RequestParam Integer plantAge
    ) {
        return productionApplicationService
                .findHealthyPlantIds(plantName, plantType, plantAge);
    }
}