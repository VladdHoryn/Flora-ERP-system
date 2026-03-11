package org.example.domain.plant;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.PlantType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "dtype"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Conifer.class, name = "CONIFER"),
        @JsonSubTypes.Type(value = FruitTree.class, name = "FRUITTREE")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@NoArgsConstructor
public abstract class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long batchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plant_type")
    private PlantType plantType;

    String name;
    @Embedded
    HealthStatus healthStatus;
    @Embedded
    GrowthStage growthStage;

    protected Plant(Long batchId, String name, PlantType plantType){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Plant name cannot be null or blank");
        }
        if (plantType == null) {
            throw new IllegalArgumentException("Plant type cannot be null");
        }

        this.batchId = batchId;
        this.name = name;
        this.plantType = plantType;
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

    public void copy(Plant plant){
        this.batchId = plant.batchId;
        this.plantType = plant.plantType;
        this.name = plant.name;
        this.healthStatus = plant.healthStatus;
        this.growthStage = plant.growthStage;
    }
}
