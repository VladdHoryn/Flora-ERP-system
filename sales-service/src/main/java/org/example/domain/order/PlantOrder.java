package org.example.domain.order;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "plant_orders")
@Getter
@Setter
public class PlantOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

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
}
