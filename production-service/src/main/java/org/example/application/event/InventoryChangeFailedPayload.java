package org.example.application.event;

import org.example.domain.PlantType;
import java.time.LocalDateTime;

public record InventoryChangeFailedPayload(
        Long id,
        Long plantId,
        Long batchId,

        PlantType plantType,
        String plantsName,
        Long age,
        int quantityChange, // +1 або -1
        String reason,
        LocalDateTime createdAt
) {
}
