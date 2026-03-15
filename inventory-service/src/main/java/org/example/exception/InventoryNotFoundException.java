package org.example.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(Long id){
        super("Inventory not found with id: " + id);
    }
    public InventoryNotFoundException(){
        super("Inventory not found");
    }
}
