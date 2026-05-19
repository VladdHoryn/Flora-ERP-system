package org.example;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableRabbit
@ConfigurationPropertiesScan
public class ProductionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductionServiceApplication.class, args);
    }
}
