package org.example.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "inventory-service")
public class InventoryServiceProperties {

    private String baseUrl;

    private String inventoryPath;
}
