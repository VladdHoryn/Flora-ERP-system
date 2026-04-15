package org.example.application.event;

import org.example.domain.inventory.PlantType;

import java.time.LocalDateTime;

public record InventoryChangedPayload (
    Long id,
    Long plantId,
    Long batchId,

    PlantType plantType,
    String plantsName,
    Long age,
    int quantityChange, // +1 або -1
    LocalDateTime createdAt
) {}
