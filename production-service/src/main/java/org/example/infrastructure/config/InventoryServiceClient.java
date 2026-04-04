package org.example.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class InventoryServiceClient {
    private final RestClient restClient;

    @Value("${inventory-service.inventory-path}")
    private String inventoryPath;
}
