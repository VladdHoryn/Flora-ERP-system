package org.example.domain.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PlantType plantType;
    private String plantsName;
    private Long age;

    private Long totalQuantity;
    private Long reservedQuantity;
    private Long availableQuantity;

    PlantInventory(PlantType plantType, String plantsName, Long age){
        this.plantType = plantType;
        this.plantsName = plantsName;
        this.age = age;
        this.totalQuantity = 0L;
        this.reservedQuantity = 0L;
        this.availableQuantity = 0L;
    }
    PlantInventory(PlantType plantType, String plantsName, Long age,
                   Long totalQuantity, Long reservedQuantity){
        this.plantType = plantType;
        this.plantsName = plantsName;
        this.age = age;
        this.totalQuantity = totalQuantity;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = totalQuantity - reservedQuantity;
    }

    void increaseStock(Long count){
        this.totalQuantity = totalQuantity + count;

        this.availableQuantity = totalQuantity - reservedQuantity;
    }
    void decreaseStock(Long count){
        if(this.totalQuantity < count)
            throw new RuntimeException("Not enough plants to decrease");
        if(this.totalQuantity - this.reservedQuantity < count)
            throw new RuntimeException("Can not decrease plants amount if they are reserved");
        this.totalQuantity = totalQuantity - count;

        this.availableQuantity = this.totalQuantity - this.reservedQuantity;
    }
    void reserve(Long count){
        if(this.availableQuantity < count)
            throw new RuntimeException("Not enough plants to reserve");
        this.reservedQuantity += count;

        this.availableQuantity = totalQuantity - reservedQuantity;
    }
    void releaseReservation(Long count){
        if(this.reservedQuantity < count)
            throw new RuntimeException("Not enough plants to release reservation for");
        this.reservedQuantity -= count;

        this.availableQuantity = totalQuantity - reservedQuantity;
    }
}
