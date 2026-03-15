package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.application.InventoryApplicationService;
import org.example.domain.PlantAvailability;
import org.example.domain.inventory.PlantInventory;
import org.example.domain.reservation.Reservation;
import org.example.dto.PlantData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
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

    @PostMapping("/reservations")
    public Reservation createReservation(@RequestBody List<PlantData> plants) {
        return inventoryApplicationService.createReservation(plants);
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
