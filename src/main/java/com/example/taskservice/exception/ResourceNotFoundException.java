package com.example.taskservice.exception;

// extend RuntimeException since compiler doesn't force us to handle it
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}