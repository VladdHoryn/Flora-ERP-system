package org.example.api;

import lombok.RequiredArgsConstructor;
import org.example.application.SalesCompositionService;
import org.example.dto.SalesDetailsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SalesAggregationController {
    private final SalesCompositionService compositionService;

    @GetMapping("/sales/{id}/details")
    public Mono<SalesDetailsResponse> getSalesDetails(@PathVariable(name = "id") Long id){
        return compositionService.getSalesDetails(id);
    }
}
