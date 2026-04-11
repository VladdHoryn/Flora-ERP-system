package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order")
@Getter
@Setter
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plant_type", nullable = false)
    @NotNull(message = "Plant type is required")
    private PlantType plantType;

    @Column(name = "plant_name", nullable = false)
    @NotBlank(message = "Plant name is required")
    private String plantName;

    @Column(name = "plant_age", nullable = false)
    @NotNull(message = "Plant age is required")
    @Min(value = 0, message = "Plant age must be non-negative")
    private Long plantAge;

    @Column(name = "quantity", nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    public Order(){
        quantity = 0L;
        status = OrderStatus.CREATED;
    }
    public Order(PlantType plantType, String plantName, Long plantAge, Long quantity){
        this.plantType = plantType;
        this.plantName = plantName;
        this.plantAge = plantAge;
        this.quantity = quantity;
        this.status = OrderStatus.CREATED;
    }

    public void confirm(){
        status = OrderStatus.CONFIRMED;
    }
    public void cancel(){
        status = OrderStatus.CANCELLED;
    }
    public void expire(){
        status = OrderStatus.EXPIRED;
    }
}
