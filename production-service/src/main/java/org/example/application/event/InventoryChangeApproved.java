package org.example.application.event;

import org.example.domain.PlantType;

import java.time.LocalDateTime;

public record InventoryChangeApproved(
        String id,
        Long plantId,
        LocalDateTime createdAt
) {
}
