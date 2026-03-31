package org.example.domain.plantChangeLog;

import jakarta.persistence.*;
import lombok.*;
import org.example.domain.PlantType;

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

    private PlantType plantType;
    private String plantsName;
    private Long age;

    private int quantityChange; // +1 або -1
    private ChangeType changeType; // "CREATE", "UPDATE", "DELETE", "DISEASE"

    private LocalDateTime createdAt;
}
