package org.example.domain.plant;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.PlantType;

@Entity
@DiscriminatorValue("CONIFER")
@Getter
@NoArgsConstructor
public class Conifer extends Plant{
    private String plantDescription;
    private String propagationType;

    private double heightAtTenYears;        // in meters
    private String needleColor;
    private String crownShape;

    private double annualGrowthRate;        // cm per year
    private double crownWidthAtMaturity;    // in meters

    private String soilRequirements;
    private String frostResistance;
    private String lightRequirements;

    public Conifer(Long batchId,
                   String name,
                   PlantType plantType,
                   String plantDescription,
                   String propagationType,
                   double heightAtTenYears,
                   String needleColor,
                   String crownShape,
                   double annualGrowthRate,
                   double crownWidthAtMaturity,
                   String soilRequirements,
                   String frostResistance,
                   String lightRequirements){
        super(batchId, name, plantType);
        this.plantDescription = plantDescription;
        this.propagationType = propagationType;
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

    @Override
    public void copy(Plant plant) {

        super.copy(plant);

        if (!(plant instanceof Conifer conifer)) {
            throw new IllegalArgumentException("Cannot copy non-Conifer into Conifer");
        }

        this.plantDescription = conifer.plantDescription;
        this.propagationType = conifer.propagationType;

        this.heightAtTenYears = conifer.heightAtTenYears;
        this.needleColor = conifer.needleColor;
        this.crownShape = conifer.crownShape;

        this.annualGrowthRate = conifer.annualGrowthRate;
        this.crownWidthAtMaturity = conifer.crownWidthAtMaturity;

        this.soilRequirements = conifer.soilRequirements;
        this.frostResistance = conifer.frostResistance;
        this.lightRequirements = conifer.lightRequirements;
    }
}
