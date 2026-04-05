package org.example.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.application.InventoryApplicationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductionPlantChangesConsumer {
    private final ObjectMapper objectMapper;
    private final InventoryApplicationService inventoryApplicationService;

    @RabbitListener(queues = RabbitConfig.PRODUCTION_CHANGES_QUEUE)
    public void consume(String message){
        try{


        } catch (Exception e){

        }
    }
}
