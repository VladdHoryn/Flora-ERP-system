package org.example.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.order.Order;
import org.example.domain.order.OrderStatus;
import org.example.domain.order.PlantOrder;
import org.example.domain.user.User;
import org.example.domain.user.UserRole;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalesApplicationService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // =========================
    // USER LOGIC
    // =========================

    @Transactional
    public User createUser(String name, String password, UserRole role) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setRole(role);

        userRepository.save(user);

        log.info("User created: {}", user.getId());
        return user;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userId);
                    return new RuntimeException("User not found");
                });
    }

    // =========================
    // ORDER LOGIC
    // =========================

    @Transactional
    public Order createOrder(Long userId) {
        User user = getUser(userId);

        Order order = new Order();
        order.setUser(user);

        orderRepository.save(order);

        log.info("Order created: {} for user {}", order.getId(), userId);
        return order;
    }

    @Transactional
    public Order updateOrder(Long orderId, List<PlantOrder> newPlantOrders) {
        Order order = getOrder(orderId);

        if (order.getStatus() != OrderStatus.CREATED) {
            log.error("Order {} cannot be updated. Status: {}", orderId, order.getStatus());
            throw new RuntimeException("Only CREATED orders can be updated");
        }

        order.getPlantOrders().clear();

        for (PlantOrder plantOrder : newPlantOrders) {
            order.addPlantOrder(plantOrder);
        }

        orderRepository.save(order);

        log.info("Order updated: {}", orderId);
        return order;
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found: {}", orderId);
                    return new RuntimeException("Order not found");
                });
    }

    @Transactional
    public void addPlantToOrder(Long orderId, PlantOrder plantOrder) {
        Order order = getOrder(orderId);

        order.addPlantOrder(plantOrder);

        orderRepository.save(order);

        log.info("Plant added to order {}", orderId);
    }

    @Transactional
    public void confirmOrder(Long orderId) {
        Order order = getOrder(orderId);

        validateOrderForConfirmation(order);

        order.confirm();
        orderRepository.save(order);

        log.info("Order confirmed: {}", orderId);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrder(orderId);

        order.cancel();
        orderRepository.save(order);

        log.info("Order cancelled: {}", orderId);
    }

    public List<Order> getUserOrders(Long userId) {
        User user = getUser(userId);
        return user.getOrders();
    }

    // =========================
    // BUSINESS VALIDATION
    // =========================

    @Transactional
    protected void validateOrderForConfirmation(Order order) {
        if (order.getPlantOrders().isEmpty()) {
            log.error("Order {} has no items", order.getId());
            throw new RuntimeException("Order must contain at least one item");
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            log.error("Order {} cannot be confirmed. Status: {}", order.getId(), order.getStatus());
            throw new RuntimeException("Invalid order status");
        }
    }
}
