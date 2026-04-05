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

import java.util.List;

//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class OutboxRelayService {
//    private final OutboxEventRepository outboxEventRepository;
//    private final RabbitTemplate rabbitTemplate;
//
//    @Scheduled(fixedDelay = 5000)
//    @Transactional
//    public void relay(){
//        log.info("Sending outbox events");
//        List<OutboxEvent> events = outboxEventRepository.findTop20ByStatusOrderByCreatedAtAsc("NEW");
//        events.addAll(outboxEventRepository.findTop20ByStatusOrderByCreatedAtAsc("FAILED"));
//
//        for(OutboxEvent event : events){
//            log.info("Obtained outbox event {}", event.getEventId());
//            try{
//                rabbitTemplate.convertAndSend(
//                        RabbitConfig.INVENTORY_EXCHANGE,
//                        RabbitConfig.INVENTORY_CHANGES_ROUTING_KEY,
//                        event.getPayload()
//                );
//
//                event.markSent();
//                log.info("Outbox event {} sent successfully", event.getEventId());
//            } catch (Exception e) {
//                log.error("Failed to send outbox event {}: {}", event.getEventId(), e.getMessage(), e);
//                event.markFailed();
//            }
//        }
//    }
//}
