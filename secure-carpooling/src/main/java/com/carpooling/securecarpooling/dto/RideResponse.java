package com.carpooling.securecarpooling.dto;

import java.time.LocalDateTime;

public class RideResponse {

    private Long id;
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private Double driverRating;
    private String source;
    private String destination;
    private LocalDateTime dateTime;
    private Integer seatsAvailable;
    private Integer totalSeats;
    private String carModel;
    private String carNumber;
    private String carColor;
    private Double distanceKm;
    private Double pricePerSeat;
    private String status;

    // Default Constructor
    public RideResponse() {
    }

    // Constructor with parameters
    public RideResponse(Long id, Long driverId, String driverName, String driverPhone,
                        Double driverRating, String source, String destination, LocalDateTime dateTime,
                        Integer seatsAvailable, Integer totalSeats, String carModel, String carNumber,
                        String carColor, Double distanceKm, Double pricePerSeat, String status) {
        this.id = id;
        this.driverId = driverId;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.driverRating = driverRating;
        this.source = source;
        this.destination = destination;
        this.dateTime = dateTime;
        this.seatsAvailable = seatsAvailable;
        this.totalSeats = totalSeats;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.carColor = carColor;
        this.distanceKm = distanceKm;
        this.pricePerSeat = pricePerSeat;
        this.status = status;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public Double getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(Double driverRating) {
        this.driverRating = driverRating;
    }

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

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
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

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(Double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RideResponse{" +
                "id=" + id +
                ", driverName='" + driverName + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", dateTime=" + dateTime +
                ", seatsAvailable=" + seatsAvailable +
                ", pricePerSeat=" + pricePerSeat +
                ", status='" + status + '\'' +
                '}';
    }
}