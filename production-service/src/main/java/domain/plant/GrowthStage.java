package domain.plant;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class GrowthStage {
    private int age; // in years
    private double height; // in meters
}
