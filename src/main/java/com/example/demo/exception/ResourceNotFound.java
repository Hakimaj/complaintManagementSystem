package com.example.demo.exception;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String message) {

        super(message);
    }
}
