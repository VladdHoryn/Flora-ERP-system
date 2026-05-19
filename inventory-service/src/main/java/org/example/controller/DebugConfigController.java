package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.config.ProductionServiceProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/inventory/v1/debug/config")
@RequiredArgsConstructor
public class DebugConfigController {

    private final ProductionServiceProperties productionServiceProperties;

    public Map<String, String> currentConfig() {
        return Map.of(
                "production-service.base-url", productionServiceProperties.getBaseUrl(),
                "production-service.production-path", productionServiceProperties.getProductionPath());
    }
}
