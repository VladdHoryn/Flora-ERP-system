package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.application.InventoryApplicationService;
import org.example.domain.PlantAvailability;
import org.example.domain.inventory.PlantInventory;
import org.example.domain.reservation.Reservation;
import org.example.dto.PlantData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory/v1")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryApplicationService inventoryApplicationService;

    // ---------------- INVENTORY ----------------

    @GetMapping("/inventories")
    public List<PlantInventory> getAllInventories() {
        return inventoryApplicationService.getAllInventories();
    }

    @GetMapping("/inventories/{id}")
    public PlantInventory getInventoryById(@PathVariable Long id) {
        return inventoryApplicationService.getInventoryById(id);
    }

    @PostMapping("/inventories")
    public PlantInventory createInventory(@RequestBody PlantInventory inventory) {
        return inventoryApplicationService.createInventory(inventory);
    }

    @PutMapping("/inventories/{id}")
    public PlantInventory updateInventory(
            @PathVariable Long id,
            @RequestBody PlantInventory inventory
    ) {
        return inventoryApplicationService.updateInventory(id, inventory);
    }

    @DeleteMapping("/inventories/{id}")
    public void deleteInventory(@PathVariable Long id) {
        inventoryApplicationService.deleteInventory(id);
    }

    // ---------------- RESERVATIONS ----------------

    @GetMapping("/reservations")
    public List<Reservation> getAllReservations(){
        return inventoryApplicationService.getAllReservations();
    }

    @GetMapping("/reservations/{id}/user")
    public Reservation getReservationByUserId(@PathVariable Long userId){
        return inventoryApplicationService.getReservationByUserId(userId);
    }

    @PostMapping("/reservations")
    @Operation(summary = "Create a new plant reservation", description = "Reserves specified quantity of healthy plants")
    public Reservation createReservation(
            @Valid @RequestBody List<PlantData> plants,
            @RequestParam Long userId
    ) {
        return inventoryApplicationService.createReservation(plants, userId);
    }

    @PostMapping("/reservations/{reservationId}/plants/{plantId}")
    public PlantAvailability addPlantToReservation(
            @PathVariable Long reservationId,
            @PathVariable Long plantId
    ) {
        return inventoryApplicationService.addPlantToReservation(reservationId, plantId);
    }

    @DeleteMapping("/reservations/plants/{plantId}")
    public void removePlantFromReservation(@PathVariable Long plantId) {
        inventoryApplicationService.removePlantFromReservation(plantId);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public void cancelReservation(@PathVariable Long reservationId) {
        inventoryApplicationService.cancelReservation(reservationId);
    }

    @PostMapping("/reservations/{reservationId}/expire")
    public void expireReservation(@PathVariable Long reservationId) {
        inventoryApplicationService.expireReservations(reservationId);
    }
}
