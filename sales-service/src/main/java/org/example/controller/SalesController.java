package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.SalesApplicationService;
import org.example.domain.order.Order;
import org.example.domain.order.PlantOrder;
import org.example.domain.user.User;
import org.example.dto.CreateUserRequest;
import org.example.dto.PlantOrderDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales/v1")
@RequiredArgsConstructor
@Slf4j
public class SalesController {
    private final SalesApplicationService service;

    // =========================
    // USER ENDPOINTS
    // =========================

    @PostMapping("/users")
    public User createUser(@RequestBody CreateUserRequest request) {
        return service.createUser(
                request.getName(),
                request.getPassword(),
                request.getRole()
        );
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return service.getUser(id);
    }

    // =========================
    // ORDER ENDPOINTS
    // =========================

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Long id){
        return service.getOrder(id);
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestParam Long userId,@Validated @RequestBody List<PlantOrderDto> plantsOrder) {
        return service.createOrder(userId, plantsOrder);
    }

    @PutMapping("/orders/{orderId}")
    public Order updateOrder(
            @PathVariable Long orderId,
            @RequestBody List<PlantOrder> plantOrders
    ) {
        return service.updateOrder(orderId, plantOrders);
    }

    @PostMapping("/orders/{orderId}/plants")
    public void addPlantToOrder(
            @PathVariable Long orderId,
            @RequestBody PlantOrder plantOrder
    ) {
        service.addPlantToOrder(orderId, plantOrder);
    }

    @PostMapping("/orders/{orderId}/confirm")
    public void confirmOrder(@PathVariable Long orderId) {
        service.confirmOrder(orderId);
    }

    @PostMapping("/orders/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {
        service.cancelOrder(orderId);
    }

    @GetMapping("/users/{userId}/orders")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return service.getUserOrders(userId);
    }
}
