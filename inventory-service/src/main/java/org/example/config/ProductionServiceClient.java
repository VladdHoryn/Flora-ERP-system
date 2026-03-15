package org.example.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.inventory.PlantType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductionServiceClient {
    private final RestClient restClient;

    @Value("${production-service.production-path}")
    private String productionUrl;

    public List<Long> findHealthyPlantIds(
            PlantType plantType,
            String plantName,
            Integer plantAge
    ) {

        log.info("Requesting healthy plants from production-service: type={}, name={}, age={}",
                plantType, plantName, plantAge);

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(productionUrl + "/plants/healthy")
                        .queryParam("plantType", plantType)
                        .queryParam("plantName", plantName)
                        .queryParam("plantAge", plantAge)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<Long>>() {});
    }
}
