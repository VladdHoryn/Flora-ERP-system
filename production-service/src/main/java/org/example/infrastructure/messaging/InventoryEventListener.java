package org.example.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.ProductionApplicationService;
import org.example.application.event.InventoryChangeFailedPayload;
import org.example.application.event.InventoryChangedPayload;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventListener {

    private final ProductionApplicationService productionApplicationService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitConfig.INVENTORY_CHANGED_QUEUE)
    private void handleInventoryChanged(String message) throws Exception{
        InventoryChangedPayload payload = objectMapper.readValue(message, InventoryChangedPayload.class);

        log.info("Received INVENTORY_CHANGED for plantId={}, batchId={}",
                payload.plantId(), payload.batchId());

        productionApplicationService.approveInventoryChanged(payload.plantId());
    }

    @RabbitListener(queues = RabbitConfig.INVENTORY_CHANGE_FAILED_QUEUE)
    private void handleInventoryChangeFailed(String message) throws Exception{
        InventoryChangeFailedPayload payload = objectMapper.readValue(message, InventoryChangeFailedPayload.class);

        log.info("Received INVENTORY_CHANGE_FAILED for plantId={}, batchId={}",
                payload.plantId(), payload.batchId());

//        productionApplicationService.approveInventoryChangeFailed(payload.plantId());
    }
}
