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

    public void cancel(){
        reservationId = null;
        status = PlantStatus.AVAILABLE;
    }
    public void reserve(Long reservationId){
        this.reservationId = reservationId;
        status = PlantStatus.RESERVED;
    }
}
