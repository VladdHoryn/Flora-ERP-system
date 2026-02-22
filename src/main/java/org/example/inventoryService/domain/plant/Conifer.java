package org.example.inventoryService.domain.plant;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CONIFER")
@Getter
@NoArgsConstructor
public class Conifer extends Plant{
    private String plantDescription;
    private String propagationType;
    private String grownIn;

    private double heightAtTenYears;        // in meters
    private String needleColor;
    private String crownShape;

    private double annualGrowthRate;        // cm per year
    private double crownWidthAtMaturity;    // in meters

    private String soilRequirements;
    private String frostResistance;
    private String lightRequirements;

    public Conifer(Long batchId,
                   String plantDescription,
                   String propagationType,
                   String grownIn,
                   double heightAtTenYears,
                   String needleColor,
                   String crownShape,
                   double annualGrowthRate,
                   double crownWidthAtMaturity,
                   String soilRequirements,
                   String frostResistance,
                   String lightRequirements){
        super(batchId);
        this.plantDescription = plantDescription;
        this.propagationType = propagationType;
        this.grownIn = grownIn;
        this.heightAtTenYears = heightAtTenYears;
        this.needleColor = needleColor;
        this.crownShape = crownShape;
        this.annualGrowthRate = annualGrowthRate;
        this.crownWidthAtMaturity = crownWidthAtMaturity;
        this.soilRequirements = soilRequirements;
        this.frostResistance = frostResistance;
        this.lightRequirements = lightRequirements;
    }

    public void updateGrowthCharacteristics(double annualGrowthRate,
                                            double heightAtTenYears,
                                            double crownWidthAtMaturity) {

        this.annualGrowthRate = annualGrowthRate;
        this.heightAtTenYears = heightAtTenYears;
        this.crownWidthAtMaturity = crownWidthAtMaturity;
    }

    public void updateEnvironmentalRequirements(String soilRequirements,
                                                String frostResistance,
                                                String lightRequirements) {

        this.soilRequirements = soilRequirements;
        this.frostResistance = frostResistance;
        this.lightRequirements = lightRequirements;
    }
}
