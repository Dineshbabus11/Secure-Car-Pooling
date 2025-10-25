package com.carpooling.securecarpooling.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "seats_available", nullable = false)
    private Integer seatsAvailable;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    // Car Details
    @Column(name = "car_model", nullable = false)
    private String carModel;

    @Column(name = "car_number", nullable = false)
    private String carNumber;

    @Column(name = "car_color", nullable = false)
    private String carColor;

    // Pricing
    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "price_per_seat")
    private Double pricePerSeat;

    // Status
    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(name = "blockchain_tx_hash")
    private String blockchainTxHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Default Constructor
    public Ride() {
        this.createdAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // Constructor with parameters
    public Ride(User driver, String source, String destination, LocalDateTime dateTime,
                Integer seatsAvailable, String carModel, String carNumber, String carColor) {
        this.driver = driver;
        this.source = source;
        this.destination = destination;
        this.dateTime = dateTime;
        this.seatsAvailable = seatsAvailable;
        this.totalSeats = seatsAvailable;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.carColor = carColor;
        this.createdAt = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
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

    public String getBlockchainTxHash() {
        return blockchainTxHash;
    }

    public void setBlockchainTxHash(String blockchainTxHash) {
        this.blockchainTxHash = blockchainTxHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", dateTime=" + dateTime +
                ", seatsAvailable=" + seatsAvailable +
                ", carModel='" + carModel + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}