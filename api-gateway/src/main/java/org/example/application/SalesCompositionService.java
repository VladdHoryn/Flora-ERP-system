package org.example.application;

import lombok.RequiredArgsConstructor;
import org.example.dto.ReservationResponse;
import org.example.dto.SalesDetailsResponse;
import org.example.dto.SalesResponse;
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

    public Mono<SalesDetailsResponse> getSalesDetails(Long salesId){
        Mono<SalesResponse> salesMono = webClient.get()
                .uri(salesBaseUrl + "/sales/v1/orders/{id}", salesId)
                .retrieve()
                .bodyToMono(SalesResponse.class);

        return salesMono.flatMap(salesResponse -> {
            Mono<ReservationResponse> reservationMono = webClient.get()
                    .uri(inventoryBaseUrl + "/inventory/v1/reservations/{id}/user", salesResponse.getUser().getId())
                    .retrieve()
                    .bodyToMono(ReservationResponse.class);

            return reservationMono.map(reservation -> SalesDetailsResponse.builder()
                    .salesId(salesResponse.getId())
                    .userId(reservation.getUserId())
                    .status(salesResponse.getStatus())
                    .reservation(reservation)
                    .build());
        });
    }
}
