package org.example.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductionServiceClient {
    private final RestClient restClient;

    @Value("${production-service.production-path}")
    private String productionUrl;


}
