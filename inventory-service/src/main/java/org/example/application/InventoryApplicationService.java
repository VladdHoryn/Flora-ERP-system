package org.example.application;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.domain.PlantAvailability;
import org.example.domain.PlantStatus;
import org.example.domain.inventory.PlantInventory;
import org.example.domain.reservation.Reservation;
import org.example.domain.reservation.ReservationStatus;
import org.example.repository.PlantAvailabilityRepository;
import org.example.repository.PlantInventoryRepository;
import org.example.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class InventoryApplicationService {
    private final PlantInventoryRepository plantInventoryRepository;
    private final ReservationRepository reservationRepository;
    private final PlantAvailabilityRepository plantAvailabilityRepository;

//    @Transactional
//    public void createReservation(){
//
//    }

    @Transactional
    public void confirmReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.confirm();

        List<PlantAvailability> plants =
                plantAvailabilityRepository.findByReservationId(reservationId);

        for (PlantAvailability plant : plants) {
            plant.sell();
        }
    }

//    @Transactional
//    public void cancelReservation(Long reservationId) {
//        Reservation reservation = reservationRepository.findById(reservationId)
//                .orElseThrow(() -> new RuntimeException("Reservation was not found"));
//
//        reservation.cancel();
//
//        List<PlantAvailability> plants = plantAvailabilityRepository.findByReservationId(reservationId);
//
//        for(var plant : plants){
//            plant.release();
//        }
//
////        PlantInventory inventory = plantInventoryRepository.findById()
//    }

    @Transactional
//    @Scheduled
    public void expireReservations(){
        List<Reservation> expiredReservations = reservationRepository
                .findByStatusAndExpiresAtBefore(ReservationStatus.CREATED, LocalDateTime.now());

        for(var reservation : expiredReservations){
            List<PlantAvailability> plants = plantAvailabilityRepository
                    .findByReservationId(reservation.getId());

            for(var plant: plants){
                plant.release();
            }

            reservation.expire();
        }
    }

    @Transactional
    public void addPlant(Long plantId){
        PlantAvailability plantAvailability = new PlantAvailability(plantId);

        plantAvailabilityRepository.save(plantAvailability);
    }

    @Transactional
    public void deletePlant(Long plantId){
        PlantAvailability plant = plantAvailabilityRepository.findByPlantId(plantId)
                .orElseThrow(() -> new RuntimeException("Plant was not found"));

        if(!plant.isAvailable()){
            throw new RuntimeException("Plant is already reserved");
        }

        plantAvailabilityRepository.delete(plant);
    }



}
