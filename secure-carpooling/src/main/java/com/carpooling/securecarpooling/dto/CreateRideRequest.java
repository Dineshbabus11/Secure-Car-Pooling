package com.carpooling.securecarpooling.dto;

import java.time.LocalDateTime;

public class CreateRideRequest {

    private String source;
    private String destination;
    private LocalDateTime dateTime;
    private Integer seatsAvailable;
    private String carModel;
    private String carNumber;
    private String carColor;

    // Default Constructor
    public CreateRideRequest() {
    }

    // Constructor with parameters
    public CreateRideRequest(String source, String destination, LocalDateTime dateTime,
                             Integer seatsAvailable, String carModel, String carNumber, String carColor) {
        this.source = source;
        this.destination = destination;
        this.dateTime = dateTime;
        this.seatsAvailable = seatsAvailable;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.carColor = carColor;
    }

    // Getters and Setters

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    @Override
    public String toString() {
        return "CreateRideRequest{" +
                "source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", dateTime=" + dateTime +
                ", seatsAvailable=" + seatsAvailable +
                ", carModel='" + carModel + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", carColor='" + carColor + '\'' +
                '}';
    }
}