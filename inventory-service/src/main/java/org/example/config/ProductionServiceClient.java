package org.example.config;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.inventory.PlantType;
import org.example.dto.PlantChangeDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductionServiceClient {
    private final RestClient restClient;

    @Value("${production-service.production-path}")
    private String productionUrl;

    @Retry(name = "productionService", fallbackMethod = "findHealthyFallback")
    @CircuitBreaker(name = "productionService", fallbackMethod = "findHealthyFallback")
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

    @Retry(name = "productionService", fallbackMethod = "getChangesFallback")
    @CircuitBreaker(name = "productionService", fallbackMethod = "getChangesFallback")
    public List<PlantChangeDTO> getChanges(LocalDateTime since){
        log.info("CALLING production-service...");

        return restClient.get()
                .uri(productionUrl + "/production/v1/plants/changes?since=" + since)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PlantChangeDTO>>() {});
    }

    private List<Long> findHealthyFallback(PlantType plantType,
                                     String plantName,
                                     Integer plantAge,
                                     Throwable throwable){
        log.warn("findHealthyFallback triggered for plantType={} plantName={}, plantAge={}, ex={} ",
                plantType, plantName, plantAge, throwable.toString());

//        throw new ResponseStatusException(
//                HttpStatus.SERVICE_UNAVAILABLE,
//                "production-service unavailable (findHealthyPlants failed)",
//                throwable
//        );
        return List.of();
    }

    private List<PlantChangeDTO> getChangesFallback(LocalDateTime since, Throwable throwable){
        log.warn("getChangesFallback triggered for since={}, ex={} ",
                since, throwable.toString());

//        throw new ResponseStatusException(
//                HttpStatus.SERVICE_UNAVAILABLE,
//                "production-service unavailable (getChanges failed)",
//                throwable
//        );
        return List.of();
    }
}
