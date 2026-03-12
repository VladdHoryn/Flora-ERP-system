package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlantAvailability {
    @Id
    private Long plantId;
    private Long reservationId;
    @Enumerated(EnumType.STRING)
    private PlantStatus status;

    public PlantAvailability(){
        reservationId = null;
        status = PlantStatus.AVAILABLE;
    }
    public PlantAvailability(Long reservationId){
        this.reservationId = reservationId;
        status = PlantStatus.RESERVED;
    }

    public void reserve(Long reservationId){
        if (status != PlantStatus.AVAILABLE) {
            throw new IllegalStateException("Plant is not available");
        }

        this.reservationId = reservationId;
        this.status = PlantStatus.RESERVED;
    }

    public void release() {
        if (status != PlantStatus.RESERVED) {
            throw new IllegalStateException("Plant is not reserved");
        }

        this.reservationId = null;
        this.status = PlantStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return status == PlantStatus.AVAILABLE;
    }
}
