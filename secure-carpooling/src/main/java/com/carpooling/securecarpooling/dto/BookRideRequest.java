package com.carpooling.securecarpooling.dto;

public class BookRideRequest {

    private Long rideId;
    private Integer seatsBooked;

    // Default Constructor
    public BookRideRequest() {
    }

    // Constructor with parameters
    public BookRideRequest(Long rideId, Integer seatsBooked) {
        this.rideId = rideId;
        this.seatsBooked = seatsBooked;
    }

    // Getters and Setters

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Integer getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(Integer seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    @Override
    public String toString() {
        return "BookRideRequest{" +
                "rideId=" + rideId +
                ", seatsBooked=" + seatsBooked +
                '}';
    }
}