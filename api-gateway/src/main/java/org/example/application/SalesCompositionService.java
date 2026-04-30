package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.dto.ReservationResponse;
import org.example.dto.SalesDetailsResponse;
import org.example.dto.SalesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SalesCompositionService {
    private final WebClient webClient;

    @Value("${services.sales.base-url}")
    private String salesBaseUrl;

    @Value("${services.inventory.base-url}")
    private String inventoryBaseUrl;

    public Mono<SalesDetailsResponse> getSalesDetails(Long salesId){

        Mono<SalesResponse> salesMono = webClient.get()
                .uri(salesBaseUrl + "/sales/v1/orders/{id}", salesId)
                .retrieve()
                .bodyToMono(SalesResponse.class)
                .onErrorResume(ex -> {
                    return Mono.empty();
                });

        return salesMono.flatMap(salesResponse -> {
            Mono<ReservationResponse> reservationMono = webClient.get()
                    .uri(inventoryBaseUrl + "/inventory/v1/reservations/{id}/user", salesResponse.getUser().getId())
                    .retrieve()
                    .bodyToMono(ReservationResponse.class)
                    .timeout(Duration.ofSeconds(2))
                    .onErrorResume(ex -> {
                        return Mono.empty();
                    });

            return reservationMono.map(reservation -> buildFullResponse(salesResponse, reservation))
                    .defaultIfEmpty(buildPartialResponse(salesResponse));
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
