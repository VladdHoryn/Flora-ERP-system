package org.example.repository;

import org.example.domain.plantChangeLog.PlantChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PlantChangeLogRepository extends JpaRepository<PlantChangeLog, Long> {
    List<PlantChangeLog> findByCreatedAtAfter(LocalDateTime lastCheck);
}
