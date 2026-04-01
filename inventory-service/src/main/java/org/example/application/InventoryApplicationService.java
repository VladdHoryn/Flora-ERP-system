package org.example.application;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.ProductionServiceClient;
import org.example.domain.PlantAvailability;
import org.example.domain.inventory.PlantInventory;
import org.example.domain.inventory.PlantType;
import org.example.domain.reservation.Reservation;
import org.example.dto.PlantChangeDTO;
import org.example.dto.PlantData;
import org.example.exception.InventoryNotFoundException;
import org.example.exception.PlantAvailabilityNotFoundException;
import org.example.exception.ReservationNotFoundException;
import org.example.repository.PlantAvailabilityRepository;
import org.example.repository.PlantInventoryRepository;
import org.example.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class InventoryApplicationService {
    private final PlantInventoryRepository plantInventoryRepository;
    private final ReservationRepository reservationRepository;
    private final PlantAvailabilityRepository plantAvailabilityRepository;
    private final ProductionServiceClient productionServiceClient;

    private LocalDateTime lastCheck;

    public InventoryApplicationService(PlantInventoryRepository plantInventoryRepository,
                                       ReservationRepository reservationRepository,
                                       PlantAvailabilityRepository plantAvailabilityRepository,
                                       ProductionServiceClient productionServiceClient){
        this.plantInventoryRepository = plantInventoryRepository;
        this.reservationRepository = reservationRepository;
        this.plantAvailabilityRepository = plantAvailabilityRepository;
        this.productionServiceClient = productionServiceClient;
        lastCheck = LocalDateTime.now().minusMinutes(1);
    }

    public List<PlantInventory> getAllInventories() {
        return plantInventoryRepository.findAll();
    }

    public PlantInventory getInventoryById(Long id) {
        return plantInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    @Transactional
    public PlantInventory createInventory(PlantInventory inventory) {
        return plantInventoryRepository.save(inventory);
    }

    @Transactional
    public PlantInventory updateInventory(Long id, PlantInventory inventory) {

        PlantInventory existing = getInventoryById(id);

        existing.setPlantType(inventory.getPlantType());
        existing.setPlantsName(inventory.getPlantsName());
        existing.setAge(inventory.getAge());

        return plantInventoryRepository.save(existing);
    }

    @Transactional
    public void deleteInventory(Long id) {
        plantInventoryRepository.deleteById(id);
    }

    public PlantInventory getInventory(PlantType plantType, String plantName, Integer plantAge){

        return plantInventoryRepository
                .findByPlantTypeAndPlantsNameAndAge(plantType, plantName, plantAge.longValue())
                .orElseThrow(() -> new InventoryNotFoundException());
    }

    @Transactional
    public PlantInventory findInventoryByPlantTypeAndPlantsNameAndAge(
            PlantType plantType,
            String plantsName,
            Long age
    ){
        return plantInventoryRepository.findByPlantTypeAndPlantsNameAndAge(plantType, plantsName, age).orElse(
                this.createInventory(new PlantInventory(plantType, plantsName, age))
        );
    }

    @Transactional
    public PlantInventory changeTotalQuantity(Long id, Long totalChanged){
        PlantInventory inventory = plantInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PlantInventory not found"));

        inventory.setTotalQuantity(inventory.getTotalQuantity() + totalChanged);

        return plantInventoryRepository.save(inventory);
    }

    @Transactional
    public PlantInventory changeAvailableQuantity(Long id, Long availableChanged){
        PlantInventory inventory = plantInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PlantInventory not found"));

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + availableChanged);

        return plantInventoryRepository.save(inventory);
    }

    // ---------------- RESERVATIONS ----------------

    @Transactional
    public Reservation createReservation(List<PlantData> plants){

        Reservation reservation = new Reservation();
        reservationRepository.save(reservation);

        for (PlantData plant : plants) {
            PlantInventory inventory = getInventory(
                    plant.plantType(),
                    plant.plantName(),
                    Math.toIntExact(plant.plantAge())
            );

            List<Long> plantIds = productionServiceClient.findHealthyPlantIds(
                    plant.plantType(),
                    plant.plantName(),
                    Math.toIntExact(plant.plantAge())
            );

            List<Long> availablePlantIds = plantIds.stream()
                    .filter(id -> plantAvailabilityRepository.findById(id)
                            .map(PlantAvailability::isAvailable)
                            .orElse(false))
                    .limit(plant.quantity())
                    .toList();

            if (availablePlantIds.size() < plant.quantity()) {
                throw new RuntimeException("Not enough available plants for reservation");
            }

            for (Long plantId : availablePlantIds) {
                addPlantToReservation(reservation.getId(), plantId);
            }

            inventory.reserve((long) plant.quantity());
            plantInventoryRepository.save(inventory);
        }

        return reservation;
    }

    @Transactional
    public PlantAvailability addPlantToReservation(Long reservationId, Long plantId){

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        PlantAvailability availability = plantAvailabilityRepository.findById(plantId)
                .orElseThrow(() -> new PlantAvailabilityNotFoundException(plantId));

        availability.reserve(reservationId);

        return plantAvailabilityRepository.save(availability);
    }

    @Transactional
    public void removePlantFromReservation(Long plantId){

        PlantAvailability plant = plantAvailabilityRepository.findById(plantId)
                .orElseThrow(() -> new PlantAvailabilityNotFoundException(plantId));

        plant.release();

        plantAvailabilityRepository.save(plant);
    }

    @Transactional
    public void cancelReservation(Long reservationId){

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        reservation.cancel();

        List<PlantAvailability> reservedPlants =
                plantAvailabilityRepository.findAllByReservationId(reservationId);

        for (PlantAvailability plant : reservedPlants) {
            plant.release();
            plantAvailabilityRepository.save(plant);
        }


        reservationRepository.save(reservation);
    }

    @Transactional
    public void expireReservations(Long reservationId){

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.getExpiresAt().isBefore(LocalDateTime.now())) {

            reservation.expire();

            List<PlantAvailability> plants =
                    plantAvailabilityRepository.findAllByReservationId(reservationId);

            for (PlantAvailability plant : plants) {
                plant.release();
                plantAvailabilityRepository.save(plant);
            }

            reservationRepository.save(reservation);
        }
    }

    @Scheduled(fixedRate = 60000) //Дописати з врахування захворювання і тп зарезервованої рослини
    public void fetchPlantChanges(){
        log.info("fetching plant changes");
        List<PlantChangeDTO> changes = productionServiceClient.getChanges(lastCheck);
        lastCheck = LocalDateTime.now();

        for (PlantChangeDTO change : changes){ //Дописати
            PlantInventory inventory = this.findInventoryByPlantTypeAndPlantsNameAndAge(change.getPlantType(), change.getPlantsName(), change.getAge());

            if(change.getChangeType().equals("CREATE")){
                log.info("changing TotalQuantity and AvailableQuantity of inventory: " + inventory.getId() + "\nBecause of CREATE\nChanged plant id: " + change.getPlantId());
                this.changeTotalQuantity(inventory.getId(), (long) change.getQuantityChange());
                this.changeAvailableQuantity(inventory.getId(), (long) change.getQuantityChange());
            }
            else if(change.getChangeType().equals("UPDATE")){
                log.info("changing TotalQuantity and AvailableQuantity of inventory: " + inventory.getId() +
                        "\nBecause of UPDATE\nChanged plant id: " + change.getPlantId());

                this.changeTotalQuantity(inventory.getId(), (long) change.getQuantityChange());
                this.changeAvailableQuantity(inventory.getId(), (long) change.getQuantityChange());
            }
            else if(change.getChangeType().equals("DELETE")){
                log.info("changing TotalQuantity and AvailableQuantity of inventory: " + inventory.getId() +
                        "\nBecause of DELETE\nChanged plant id: " + change.getPlantId());

                this.changeTotalQuantity(inventory.getId(), (long) change.getQuantityChange());
                this.changeAvailableQuantity(inventory.getId(), (long) change.getQuantityChange());
            }
            else if(change.getChangeType().equals("DISEASE")){
                log.info("changing TotalQuantity and AvailableQuantity of inventory: " + inventory.getId() +
                        "\nBecause of DISEASE\nChanged plant id: " + change.getPlantId());

                this.changeTotalQuantity(inventory.getId(), (long) change.getQuantityChange());
                this.changeAvailableQuantity(inventory.getId(), (long) change.getQuantityChange());
            }
        }
    }
}
