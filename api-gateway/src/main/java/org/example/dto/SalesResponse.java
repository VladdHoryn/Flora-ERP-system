package org.example.dto;

import lombok.Data;

@Data
public class SalesResponse {
    private Long salesId;
    private Long userId;
    private String status;
}
