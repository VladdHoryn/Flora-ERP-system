package org.example.inventoryService.domain.plant;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public abstract class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long batchId;
    @Embedded
    HealthStatus healthStatus;
    @Embedded
    GrowthStage growthStage;
}
