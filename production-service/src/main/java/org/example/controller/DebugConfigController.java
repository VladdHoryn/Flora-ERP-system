package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.config.InventoryServiceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/production/v1/debug/config")
@RequiredArgsConstructor
public class DebugConfigController {

    private final InventoryServiceProperties inventoryServiceProperties;

    @GetMapping
    public Map<String, String> currentConfig() {
        return Map.of(
                "inventory-service.base-url", inventoryServiceProperties.getBaseUrl(),
                "inventory-service.books-path", inventoryServiceProperties.getInventoryPath()
        );
    }
}
