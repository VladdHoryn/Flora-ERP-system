package org.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlantAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId;
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
