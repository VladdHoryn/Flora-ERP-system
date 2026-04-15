package org.example.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.event.InventoryChangeFailedPayload;
import org.example.application.event.InventoryChangedPayload;
import org.example.application.event.PlantChangeLogPayload;
import org.example.domain.OutboxEvent;
import org.example.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventorySagaService {
    private final InventoryApplicationService inventoryApplicationService;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void handlePlantChanged(PlantChangeLogPayload payload){
        try{
            inventoryApplicationService.fetchPlantChanges(payload);

            InventoryChangedPayload eventPayload = new InventoryChangedPayload(
                    payload.id(),
                    payload.plantId(),
                    payload.batchId(),
                    payload.plantType(),
                    payload.plantsName(),
                    payload.age(),
                    payload.quantityChange(),
                    LocalDateTime.now()
            );

            saveOutboxEvent(
                    "INVENTORY",
                    payload.id().toString(),
                    "INVENTORY_CHANGED",
                    eventPayload
            );

            log.info("Inventory changed successfully for plantId={}, batchID={}",
                    payload.plantId(), payload.batchId());
        } catch (EntityNotFoundException | IllegalStateException e){
            InventoryChangeFailedPayload eventPayload = new InventoryChangeFailedPayload(
                    payload.id(),
                    payload.plantId(),
                    payload.batchId(),
                    payload.plantType(),
                    payload.plantsName(),
                    payload.age(),
                    payload.quantityChange(),
                    e.getMessage(),
                    LocalDateTime.now()
            );

            saveOutboxEvent(
                    "INVENTORY",
                    payload.id().toString(),
                    "INVENTORY_CHANGE_FAILED",
                    eventPayload
            );

            log.warn("Inventory change failed for plantId={}, batchId={}, reason{}",
                    payload.plantId(), payload.batchId(), e.getMessage());
        }
    }

    private void saveOutboxEvent(String aggregateType,
                                 String aggregateId,
                                 String eventType,
                                 Object payload) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);

            OutboxEvent event = new OutboxEvent(
                    aggregateType,
                    aggregateId,
                    eventType,
                    payloadJson
            );

            outboxEventRepository.save(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize outbox payload", e);
        }
    }
}
