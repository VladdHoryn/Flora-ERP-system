package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class SalesResponse {

    private Long id;
    private String status;

    private UserDto user;

    private List<PlantOrderDtoResponse> plantOrders;

    // =========================
    // INNER DTOs
    // =========================

    @Data
    public static class UserDto {
        private Long id;
        private String name;
        private String password;
        private String role;
    }

    @Data
    public static class PlantOrderDtoResponse {
        private Long id;
        private String plantType;
        private String plantName;
        private Long plantAge;
        private Long quantity;
    }
}