package org.example.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String PLANT_CHANGES_EXCHANGE = "plants.exchange";

    public static final String PRODUCTION_CHANGES_QUEUE = "production.changes.queue";
    public static final String INVENTORY_CHANGED_QUEUE = "production.inventory-changed.queue";
    public static final String INVENTORY_CHANGE_FAILED_QUEUE = "production.inventory-change-failed.queue";

    public static final String PRODUCTION_CHANGES_ROUTING_KEY = "production.changed";
    public static final String INVENTORY_CHANGED_ROUTING_KEY = "inventory.changed";
    public static final String INVENTORY_CHANGE_FAILED_ROUTING_KEY = "inventory.change-failed";
    @Bean
    public TopicExchange plantsExchange(){
        return new TopicExchange(PLANT_CHANGES_EXCHANGE);
    }

    @Bean
    public Queue productionChangesQueue(){
        return new Queue(PRODUCTION_CHANGES_QUEUE);
    }

    @Bean
    public Binding plantChangedBinding(){
        return BindingBuilder
                .bind(productionChangesQueue())
                .to(plantsExchange())
                .with(PRODUCTION_CHANGES_ROUTING_KEY);
    }
}
