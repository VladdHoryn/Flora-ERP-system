package org.example.domain.plant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Health status")
public enum Condition {
    HEALTHY,
    SICK
}
