package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Production Service", description = "APIs for managing plants and plant batches")
public class ProductionController {

    private final ProductionApplicationService productionApplicationService;

    // ---------------- PLANTS ----------------

    @Operation(summary = "Get all plants", description = "Returns a list of all plants in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of plants retrieved successfully")
    })
    @GetMapping("/plants")
    public List<Plant> getAllPlants() {
        return productionApplicationService.getAllPlants();
    }

    @Operation(summary = "Get plant by ID", description = "Returns a single plant by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant found"),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @GetMapping("/plants/{id}")
    public Plant getPlantById(
            @Parameter(description = "ID of the plant to retrieve", required = true)
            @PathVariable Long id) {
        return productionApplicationService.getPlantById(id);
    }

    @Operation(summary = "Create a new plant", description = "Creates a plant in the system with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/plants")
    public Plant createPlant(
            @Parameter(description = "Plant object to create", required = true)
            @RequestBody Plant plant) {
        return productionApplicationService.createPlant(plant);
    }

    @Operation(summary = "Update a plant", description = "Updates a plant with the given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant updated successfully"),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @PutMapping("/plants/{id}")
    public Plant updatePlant(
            @Parameter(description = "ID of the plant to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated plant object", required = true)
            @RequestBody Plant plant) {
        return productionApplicationService.updatePlant(id, plant);
    }

    @Operation(summary = "Delete a plant", description = "Deletes a plant by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Plant not found")
    })
    @DeleteMapping("/plants/{id}")
    public void deletePlant(
            @Parameter(description = "ID of the plant to delete", required = true)
            @PathVariable Long id) {
        productionApplicationService.deletePlant(id);
    }

    // ---------------- PLANT BATCHES ----------------

    @Operation(summary = "Get all plant batches", description = "Returns a list of all plant batches")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of plant batches retrieved")
    })
    @GetMapping("/batches")
    public List<PlantBatch> getAllBatches() {
        return productionApplicationService.getAllPlantBatches();
    }

    @Operation(summary = "Create a plant batch", description = "Creates a new plant batch")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant batch created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/batches")
    public PlantBatch createBatch(
            @Parameter(description = "Plant batch object to create", required = true)
            @RequestBody PlantBatch plantBatch) {
        return productionApplicationService.createPlantBatch(plantBatch);
    }

    @Operation(summary = "Update a plant batch", description = "Updates the plant batch with the given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant batch updated successfully"),
            @ApiResponse(responseCode = "404", description = "Plant batch not found")
    })
    @PutMapping("/batches/{id}")
    public PlantBatch updateBatch(
            @Parameter(description = "ID of the batch to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated batch object", required = true)
            @RequestBody PlantBatch plantBatch) {
        return productionApplicationService.updatePlantBatch(id, plantBatch);
    }

    @Operation(summary = "Delete a plant batch", description = "Deletes the batch by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plant batch deleted"),
            @ApiResponse(responseCode = "404", description = "Plant batch not found")
    })
    @DeleteMapping("/batches/{id}")
    public void deleteBatch(
            @Parameter(description = "ID of the batch to delete", required = true)
            @PathVariable Long id) {
        productionApplicationService.deletePlantBatch(id);
    }

    @Operation(summary = "Get count of plants in batch", description = "Returns the total number of plants in a batch")
    @GetMapping("/batches/{id}/plants/count")
    public Integer getPlantsCount(
            @Parameter(description = "Batch ID", required = true)
            @PathVariable Long id) {
        return productionApplicationService.findPlantsAmountInBatch(id);
    }

    @Operation(summary = "Recount total plants in batch", description = "Updates totalCount for the batch")
    @PutMapping("/batches/{id}/recount")
    public PlantBatch recountBatch(
            @Parameter(description = "Batch ID", required = true)
            @PathVariable Long id) {
        return productionApplicationService.updateTotalCountForBatch(id);
    }

    @Operation(summary = "Find healthy plant IDs", description = "Returns IDs of healthy plants matching criteria")
    @GetMapping("/plants/healthy")
    public List<Long> findHealthyPlantIds(
            @Parameter(description = "Plant name to search", required = true)
            @RequestParam String plantName,
            @Parameter(description = "Plant type", required = true)
            @RequestParam PlantType plantType,
            @Parameter(description = "Plant age", required = true)
            @RequestParam Integer plantAge
    ) {
        return productionApplicationService.findHealthyPlantIds(plantName, plantType, plantAge);
    }
}