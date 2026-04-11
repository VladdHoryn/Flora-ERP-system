package org.example.domain.order;

import org.example.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<PlantOrder> plantOrders = new ArrayList<>();

    public Order() {
        status = OrderStatus.CREATED;
    }

    public void addPlantOrder(PlantOrder plantOrder){
        plantOrders.add(plantOrder);
        plantOrder.setOrder(this);
    }

    public void removePlantOrder(PlantOrder plantOrder){
        plantOrders.remove(plantOrder);
        plantOrder.setOrder(null);
    }

    public Order(PlantType plantType, String plantName, Long plantAge, Long quantity) {
        this.status = OrderStatus.CREATED;
    }

    public void confirm() {
        status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        status = OrderStatus.CANCELLED;
    }

    public void expire() {
        status = OrderStatus.EXPIRED;
    }
}
