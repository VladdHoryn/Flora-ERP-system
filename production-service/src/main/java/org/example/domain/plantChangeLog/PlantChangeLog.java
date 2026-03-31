package org.example.domain.plantChangeLog;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long plantId;
    private Long batchId;
    private int quantityChange; // +1 або -1
    private ChangeType changeType; // "CREATE", "UPDATE", "DELETE", "DISEASE"

    private LocalDateTime createdAt;
}
