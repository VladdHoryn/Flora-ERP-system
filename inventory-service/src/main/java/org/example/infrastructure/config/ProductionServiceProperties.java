package org.example.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "production-service")
public class ProductionServiceProperties {

    private String baseUrl;

    private String productionPath;
}
