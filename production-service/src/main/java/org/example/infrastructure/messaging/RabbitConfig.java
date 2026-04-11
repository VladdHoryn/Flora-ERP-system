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
}
