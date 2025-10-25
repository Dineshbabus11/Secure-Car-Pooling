package com.carpooling.securecarpooling.dto;

public class MessageResponse {

    private String message;

    // Default Constructor
    public MessageResponse() {
    }

    // Constructor with parameter
    public MessageResponse(String message) {
        this.message = message;
    }

    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}