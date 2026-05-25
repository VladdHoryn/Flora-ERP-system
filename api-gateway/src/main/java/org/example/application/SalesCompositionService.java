package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.dto.ReservationResponse;
import org.example.dto.SalesDetailsResponse;
import org.example.dto.SalesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SalesCompositionService {
    private final WebClient webClient;
    private final MeterRegistry meterRegistry;

    private final ServicesProperties servicesProperties;

//    @Value("${services.sales.base-url}")
//    private String salesBaseUrl;
//
//    @Value("${services.inventory.base-url}")
//    private String inventoryBaseUrl;

    private Counter successCounter;
    private Counter errorCounter;
    private Timer latencyTimer;

        Mono<SalesResponse> salesMono = webClient.get()
                .uri( servicesProperties.getSales().getBaseUrl() + "/sales/v1/orders/{id}", salesId)
                .retrieve()
                .bodyToMono(SalesResponse.class)
                .onErrorResume(ex -> {
                    return Mono.empty();
                });

        return salesMono.flatMap(salesResponse -> {
            Mono<ReservationResponse> reservationMono = webClient.get()
                    .uri( servicesProperties.getInventory().getBaseUrl() + "/inventory/v1/reservations/{id}/user", salesResponse.getUser().getId())
                    .retrieve()
                    .bodyToMono(SalesResponse.class)
                    .onErrorResume(ex -> {
                        errorCounter.increment();
                        return Mono.empty();
                    });

            return salesMono.flatMap(salesResponse -> {

                        Mono<ReservationResponse> reservationMono = webClient.get()
                                .uri(inventoryBaseUrl + "/inventory/v1/reservations/{id}/user",
                                        salesResponse.getUser().getId())
                                .retrieve()
                                .bodyToMono(ReservationResponse.class)
                                .timeout(Duration.ofSeconds(2))
                                .onErrorResume(ex -> {
                                    errorCounter.increment();
                                    return Mono.empty();
                                });

                        return reservationMono
                                .map(reservation -> {
                                    successCounter.increment();
                                    return buildFullResponse(salesResponse, reservation);
                                })
                                .defaultIfEmpty(buildPartialResponse(salesResponse));
                    })
                    .doOnTerminate(() -> sample.stop(latencyTimer)); // ⏱ latency
        });
    }

    private SalesDetailsResponse buildFullResponse(SalesResponse sales,
                                                   ReservationResponse reservation) {
        return SalesDetailsResponse.builder()
                .salesId(sales.getId())
                .userId(reservation.getUserId())
                .status(sales.getStatus())
                .reservation(reservation)
                .build();
    }

    private SalesDetailsResponse buildPartialResponse(SalesResponse sales) {
        return SalesDetailsResponse.builder()
                .salesId(sales.getId())
                .userId(sales.getUser().getId())
                .status(sales.getStatus())
                .reservation(null)
                .build();
    }
}
