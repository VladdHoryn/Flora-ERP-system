package org.example.controller;

import org.example.application.ProductionApplicationService;
import org.example.domain.plant.Plant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/production/v1")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionApplicationService productionApplicationService;

    @GetMapping("/plants")
    public List<Plant> getPlants(){
        return productionApplicationService.getAllPlants();
    }

    @PostMapping("/plants")
    public Plant createPlant(@RequestBody Plant plant){
        return productionApplicationService.createPlant(plant);
    }
}