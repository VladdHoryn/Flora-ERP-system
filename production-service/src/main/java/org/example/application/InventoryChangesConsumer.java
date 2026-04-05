package org.example.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.messaging.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class InventoryChangesConsumer {
//
//    private final ObjectMapper objectMapper;
//    private final ProductionApplicationService productionApplicationService;
//
////    @RabbitListener(queues = RabbitConfig.INVENTORY_CHANGES_QUEUE)
//    public void consume(String message){
//        try{
//
//
//        } catch (Exception e){
//            log.error("Failed to process message {}", message, e);
//            throw new RuntimeException(e);
//        }
//    }
//}
