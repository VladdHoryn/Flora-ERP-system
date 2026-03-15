package org.example.domain.plant;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.PlantType;

@Entity
@DiscriminatorValue("FRUITTREE")
@Getter
@NoArgsConstructor
public class FruitTree extends Plant {
    private String plantDescription;
    private double fruitWeight;            // in grams
    private String propagationType;

    private double heightAtMaturity;       // in meters
    private String ripeningPeriod;         // e.g., early, mid, late
    private String varietyAdvantages;
    private String fruitTaste;

    private String harvestTime;            // specific month/period
    private int storageDurationDays;       // shelf life in days

    private String soilRequirements;
    private String frostResistance;
    private String lightRequirements;
    private String recommendedPollinators;

    public FruitTree(Long batchId,
                     String name,
                     PlantType plantType,
                     String plantDescription,
                     double fruitWeight,
                     String propagationType,
                     double heightAtMaturity,
                     String ripeningPeriod,
                     String varietyAdvantages,
                     String fruitTaste,
                     String harvestTime,
                     int storageDurationDays,
                     String soilRequirements,
                     String frostResistance,
                     String lightRequirements,
                     String recommendedPollinators){
        super(batchId, name, plantType);
        this.plantDescription = plantDescription;
        this.fruitWeight = fruitWeight;
        this.propagationType = propagationType;
        this.heightAtMaturity = heightAtMaturity;
        this.ripeningPeriod = ripeningPeriod;
        this.varietyAdvantages = varietyAdvantages;
        this.fruitTaste = fruitTaste;
        this.harvestTime = harvestTime;
        this.storageDurationDays = storageDurationDays;
        this.soilRequirements = soilRequirements;
        this.frostResistance = frostResistance;
        this.lightRequirements = lightRequirements;
        this.recommendedPollinators = recommendedPollinators;
    }

    public void updateFruitCharacteristics(double fruitWeight,
                                           String fruitTaste,
                                           int storageDurationDays) {

        if (fruitWeight <= 0)
            throw new IllegalArgumentException("Fruit weight must be positive");

        if (storageDurationDays < 0)
            throw new IllegalArgumentException("Storage duration cannot be negative");

        this.fruitWeight = fruitWeight;
        this.fruitTaste = fruitTaste;
        this.storageDurationDays = storageDurationDays;
    }

    public void updateRipeningInfo(String ripeningPeriod,
                                   String harvestTime) {

        this.ripeningPeriod = ripeningPeriod;
        this.harvestTime = harvestTime;
    }

    public void updateEnvironmentalRequirements(String soilRequirements,
                                                String frostResistance,
                                                String lightRequirements) {

        this.soilRequirements = soilRequirements;
        this.frostResistance = frostResistance;
        this.lightRequirements = lightRequirements;
    }

    @Override
    public void copy(Plant plant) {

        super.copy(plant);

        if (!(plant instanceof FruitTree fruitTree)) {
            throw new IllegalArgumentException("Cannot copy non-FruitTree into FruitTree");
        }

        this.plantDescription = fruitTree.plantDescription;
        this.fruitWeight = fruitTree.fruitWeight;
        this.propagationType = fruitTree.propagationType;

        this.heightAtMaturity = fruitTree.heightAtMaturity;
        this.ripeningPeriod = fruitTree.ripeningPeriod;
        this.varietyAdvantages = fruitTree.varietyAdvantages;
        this.fruitTaste = fruitTree.fruitTaste;

        this.harvestTime = fruitTree.harvestTime;
        this.storageDurationDays = fruitTree.storageDurationDays;

        this.soilRequirements = fruitTree.soilRequirements;
        this.frostResistance = fruitTree.frostResistance;
        this.lightRequirements = fruitTree.lightRequirements;
        this.recommendedPollinators = fruitTree.recommendedPollinators;
    }
}
