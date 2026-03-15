package org.example.exception;

public class PlantNotFoundException extends RuntimeException {

    public PlantNotFoundException(Long id) {
        super("Plant not found with id: " + id);
    }
}
