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

    public void addDisease(Disease disease) {
        if (disease == null) return;

        if (!diseases.contains(disease)) {
            diseases.add(disease);
            updateCondition();
        }
    }

    public void removeDisease(Disease disease) {
        if (disease == null) return;

        if (diseases.remove(disease)) {
            updateCondition();
        }
    }

    public void clearDiseases() {
        diseases.clear();
        updateCondition();
    }

    public void addTreatment(Treatment treatment) {
        if (treatment == null) return;

        if (!treatments.contains(treatment)) {
            treatments.add(treatment);
        }
    }

    public void removeTreatment(Treatment treatment) {
        if (treatment == null) return;

        treatments.remove(treatment);
    }

    public void clearTreatments() {
        treatments.clear();
    }

    public boolean isHealthy() {
        return diseases.isEmpty();
    }

    private void updateCondition() {
        this.condition = diseases.isEmpty()
                ? Condition.HEALTHY
                : Condition.SICK;
    }
}

