package org.example.exception;

public class PlantBatchNotFoundException extends RuntimeException {

    public PlantBatchNotFoundException(Long id) {
        super("Plant batch not found with id: " + id);
    }
    public PlantBatchNotFoundException() {
        super("Plant batch not found");
    }
}
