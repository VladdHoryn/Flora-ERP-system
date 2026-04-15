package org.example.infrastructure.messaging;

import org.springframework.amqp.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    public static final String  PRODUCTION_EXCHANGE = "production.exchange";
    public static final String PRODUCTION_CHANGES_QUEUE = "production.changes.queue";
    public static final String PRODUCTION_CHANGES_ROUTING_KEY = "production.changes";

    public static final String PLANT_CHANGES_EXCHANGE = "plants.exchange";

    public static final String INVENTORY_CHANGED_QUEUE = "inventory.changes.queue";
    public static final String INVENTORY_CHANGE_FAILED_QUEUE = "inventory.changes-failed.queue";

    public static final String INVENTORY_CHANGED_ROUTING_KEY = "inventory.changed";
    public static final String INVENTORY_CHANGE_FAILED_ROUTING_KEY = "inventory.changed-failed";

    @Bean
    public TopicExchange productionExchange(){
        return new TopicExchange(PRODUCTION_EXCHANGE);
    }

    @Bean
    public Queue productionChangesQueue(){
        return new Queue(PRODUCTION_CHANGES_QUEUE);
    }

    @Bean
    public Binding productionChangesBinding(){
        return BindingBuilder
                .bind(productionChangesQueue())
                .to(productionExchange())
                .with(PRODUCTION_CHANGES_ROUTING_KEY);
    }

    @Bean
    public TopicExchange plantsExchange(){
        return new TopicExchange(PLANT_CHANGES_EXCHANGE);
    }

    @Bean
    public Queue inventoryChangedQueue(){return QueueBuilder.durable(INVENTORY_CHANGED_QUEUE).build();}

    @Bean
    public Queue inventoryChangeFailedQueue(){return QueueBuilder.durable(INVENTORY_CHANGE_FAILED_QUEUE).build();}

    @Bean
    public Binding inventoryChangedBinding(){
        return BindingBuilder.bind(inventoryChangedQueue())
                .to(plantsExchange())
                .with(INVENTORY_CHANGED_ROUTING_KEY);
    }

    @Bean
    public Binding inventoryChangeFailedBinding(){
        return BindingBuilder.bind(inventoryChangeFailedQueue())
                .to(plantsExchange())
                .with(INVENTORY_CHANGE_FAILED_ROUTING_KEY);
    }
}
