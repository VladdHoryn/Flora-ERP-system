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

    @Enumerated(EnumType.STRING)
    private PlantType plantType;
    private String plantsName;
    private Long age;

    private int quantityChange; // +1 або -1
    @Enumerated(EnumType.STRING)
    private ChangeType changeType; // "CREATE", "UPDATE", "DELETE", "DISEASE"

    private LocalDateTime createdAt;
}
