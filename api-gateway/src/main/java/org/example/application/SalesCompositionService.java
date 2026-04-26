package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.dto.SalesDetailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SalesCompositionService {
    private final WebClient webClient;

    @Value("${services.sales.base-url}")
    private String salesBaseUrl;

    @Value("${services.inventory.base-url}")
    private String inventoryBaseUrl;

    private Mono<SalesDetailsResponse> getSalesDetails(Long salesId){
        return Mono<SalesDetailsResponse>
    }
}
