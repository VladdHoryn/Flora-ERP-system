package org.example.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
//    @Value("${inventory-service.base-url}")
//    private String inventoryUrl;

    private final InventoryServiceProperties inventoryServiceProperties;

    @Bean
    @RefreshScope
    RestClient restClient(RestClient.Builder builder) {
        var httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(500))
                .build();

        var requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofMillis(800));

        return builder
                .baseUrl(inventoryServiceProperties.getBaseUrl())
                .requestFactory(requestFactory)
                .build();
    }
}
