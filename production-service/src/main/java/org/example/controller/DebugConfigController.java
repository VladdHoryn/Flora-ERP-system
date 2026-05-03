package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/production/v1/debug/config")
@RequiredArgsConstructor
public class DebugConfigController {

    private final ProductionServiceProperties productionServiceProperties;

    @GetMapping
    public Map<String, String> currentConfig() {
        return Map.of(
                "book-service.base-url", productionServiceProperties.getBaseUrl(),
                "book-service.books-path", productionServiceProperties.getBooksPath()
        );
    }
}
