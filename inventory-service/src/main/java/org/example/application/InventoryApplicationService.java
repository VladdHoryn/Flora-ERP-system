package org.example.application;

import lombok.AllArgsConstructor;
import org.example.domain.PlantAvailability;
import org.example.domain.inventory.PlantInventory;
import org.example.domain.inventory.PlantType;
import org.example.domain.reservation.Reservation;
import org.example.dto.PlantData;
import org.example.repository.PlantAvailabilityRepository;
import org.example.repository.PlantInventoryRepository;
import org.example.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryApplicationService {
    private final PlantInventoryRepository plantInventoryRepository;
    private final ReservationRepository reservationRepository;
    private final PlantAvailabilityRepository plantAvailabilityRepository;

    public PlantInventory CalculateStock(PlantType plantType, String plantName, Integer plantAge){
        return new PlantInventory();
    }
    public PlantInventory getInventory(PlantType plantType, String plantName, Integer plantAge){
        return new PlantInventory();
    }

    public Reservation createReservation(List<PlantData> plants){
        return new Reservation();
    }

    public PlantAvailability addPlantToReservation(Long reservationId, Long PlantId){
        return new PlantAvailability();
    }

    public void cancelReservation(Long reservationId){

    }

    public void expireReservations(Long reservationId){

    }

    public void removePlantFromReservation(Long PlantId){

    }
}
