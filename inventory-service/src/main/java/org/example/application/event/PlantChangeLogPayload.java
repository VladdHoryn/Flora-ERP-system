package org.example.application.event;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.example.domain.inventory.PlantType;

import java.time.LocalDateTime;

public record PlantChangeLogPayload(
        String id,
        Long plantId,
        Long batchId,

        PlantType plantType,
        String plantsName,
        Long age,
        int quantityChange, // +1 або -1
        ChangeType changeType, // "CREATE", "UPDATE", "DELETE", "DISEASE"
        LocalDateTime createdAt
) {}
