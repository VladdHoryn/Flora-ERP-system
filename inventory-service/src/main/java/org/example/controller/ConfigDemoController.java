package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.config.InventoryProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfigDemoController {

    private final InventoryProperties properties;

    @GetMapping("/config-demo")
    public String configDemo() {
        return """
                mode=%s
                lowStockThreshold=%d
                """.formatted(
                properties.getMode(),
                properties.getLowStockThreshold()
        );
    }
}
