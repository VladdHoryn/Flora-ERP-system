package org.example.inventoryService.domain.plant;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
public class HealthStatus {
    @ElementCollection
    @CollectionTable(name = "plant_diseases", joinColumns = @JoinColumn(name = "plant_id"))
    private List<Disease> diseases = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "plant_treatments", joinColumns = @JoinColumn(name = "plant_id"))
    private List<Treatment> treatments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Condition condition;
}

