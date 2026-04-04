package org.example.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String INVENTORY_CHANGES_QUEUE = "inventory.changes.queue";
    public static final String INVENTORY_CHANGES_ROUTING_KEY = "inventory.changes";

    @Bean
    public TopicExchange inventoryExchange(){
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Queue inventoryChangesQueue(){
        return new Queue(INVENTORY_CHANGES_QUEUE);
    }

    public Binding inventoryChangesBinding(){
        return BindingBuilder
                .bind(inventoryChangesQueue())
                .to(inventoryExchange())
                .with(INVENTORY_CHANGES_ROUTING_KEY);
    }
}
