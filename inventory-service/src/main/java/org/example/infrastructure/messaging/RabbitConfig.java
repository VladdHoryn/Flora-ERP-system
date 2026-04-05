package org.example.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String PRODUCTION_CHANGES_QUEUE = "production.changes.queue";

    @Bean
    public Queue productionChangesQueue(){
        return new Queue(PRODUCTION_CHANGES_QUEUE);
    }
}
