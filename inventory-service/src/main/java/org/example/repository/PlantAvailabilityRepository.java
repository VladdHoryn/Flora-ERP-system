package org.example.repository;

import org.example.domain.PlantAvailability;
import org.example.domain.PlantStatus;
import org.example.domain.inventory.PlantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantAvailabilityRepository extends JpaRepository<PlantAvailability, Long> {
    List<PlantAvailability> findByStatus(PlantStatus status);

    List<PlantAvailability> findAllByReservationId(Long reservationId);

//    List<PlantAvailability> findByReservationId(Long reservationId);
//
//    Optional<PlantAvailability> findByPlantId(Long plantId);

//    Page<PlantAvailability> findAvailable(PageRequest of);

//    List<PlantAvailability> findAvailablePlants(PlantType plantType, String s, Long aLong, Long quantity);
}
