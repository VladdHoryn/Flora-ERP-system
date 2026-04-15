package org.example.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.OutboxEvent;
import org.example.infrastructure.messaging.RabbitConfig;
import org.example.repository.OutboxEventRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryOutboxRelay {
    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void relay(){
        List<OutboxEvent> events = new ArrayList<>();
        events.addAll(outboxEventRepository.findTop20ByStatusOrderByCreatedAtAsc("NEW"));
        events.addAll(outboxEventRepository.findTop20ByStatusOrderByCreatedAtAsc("FAILED"));

        for(OutboxEvent event : events){
            try{
                String routingKey = resolveRoutingKey(event.getEventType());

                rabbitTemplate.convertAndSend(
                        RabbitConfig.PLANT_CHANGES_EXCHANGE,
                        routingKey,
                        event.getPayload()
                );

                event.markSent();
                log.info("Outbox event {} sent successfully", event.getEventId());
            } catch (Exception e){
                event.markFailed();
                log.error("Failed to send outbox event {}: {}", event.getEventId(), e.getMessage(), e);
            }
        }
    }

    private String resolveRoutingKey(String eventType){
        return switch (eventType){
            case "PLANT_CHANGED" -> RabbitConfig.INVENTORY_CHANGED_ROUTING_KEY;
            case "PLANT_CHANGE_FAILED" -> RabbitConfig.INVENTORY_CHANGE_FAILED_ROUTING_KEY;
            default -> throw new IllegalArgumentException("Unsupported event type: " + eventType);
        };
    }
}
