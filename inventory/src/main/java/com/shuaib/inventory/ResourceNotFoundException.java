package com.shuaib.inventory;
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) { super("Item not found with id " + id); }
}
