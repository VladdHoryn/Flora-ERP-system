package controller;

import application.InventoryApplicationService;
import domain.plant.Plant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/v1")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryApplicationService inventoryApplicationService;

    @GetMapping("/plants")
    public List<Plant> getPlants(){
        return inventoryApplicationService.getAllPlants();
    }

    @PostMapping("/plants")
    public Plant createPlant(@RequestBody Plant plant){
        return inventoryApplicationService.createPlant(plant);
    }
}