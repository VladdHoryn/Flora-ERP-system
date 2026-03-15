package org.example.exception;

public class PlantAvailabilityNotFoundException extends RuntimeException {
    public PlantAvailabilityNotFoundException(Long id) {

        super("Plant Availability was not found with id: " + id);
    }
}
