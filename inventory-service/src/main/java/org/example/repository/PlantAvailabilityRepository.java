package org.example.repository;

import org.example.domain.PlantAvailability;
import org.example.domain.PlantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantAvailabilityRepository extends JpaRepository<PlantAvailability, Long> {
    List<PlantAvailability> findByStatus(PlantStatus status);

    List<PlantAvailability> findByReservationId(Long reservationId);

    Optional<PlantAvailability> findByPlantId(Long plantId);
}
