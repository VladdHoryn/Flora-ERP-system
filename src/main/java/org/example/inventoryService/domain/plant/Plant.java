package org.example.inventoryService.domain.plant;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "plant_type")
@Getter
@NoArgsConstructor
public abstract class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long batchId;
    @Embedded
    HealthStatus healthStatus;
    @Embedded
    GrowthStage growthStage;

    protected Plant(Long batchId){
        this.batchId = batchId;
        this.healthStatus = new HealthStatus();
        this.growthStage = new GrowthStage();
    }

    public void addDisease(Disease disease) {
        healthStatus.addDisease(disease);
    }

    public void removeDisease(Disease disease) {
        healthStatus.removeDisease(disease);
    }

    public void clearDisease(){
        healthStatus.clearDiseases();
    }

    public void addTreatment(Treatment treatment) {
        healthStatus.addTreatment(treatment);
    }

    public void removeTreatment(Treatment treatment) {
        healthStatus.removeTreatment(treatment);
    }

    public void clearTreatment(){
        healthStatus.clearTreatments();
    }

    public void updateGrowth(int age, double height) {
        this.growthStage.setAge(age);
        this.growthStage.setHeight(height);
    }

    public boolean isHealthy() {
        return healthStatus.isHealthy();
    }
}
