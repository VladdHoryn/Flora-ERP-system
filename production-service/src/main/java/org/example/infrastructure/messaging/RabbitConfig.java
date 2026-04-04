package org.example.infrastructure.messaging;

import org.springframework.amqp.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    public static final String INVENTORY_CHANGES_QUEUE = "inventory.changes.queue";

    @Bean
    public Queue inventoryChangesQueue(){
        return new Queue(INVENTORY_CHANGES_QUEUE);
    }
}
