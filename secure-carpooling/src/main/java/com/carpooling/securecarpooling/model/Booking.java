package com.carpooling.securecarpooling.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    @Column(name = "seats_booked", nullable = false)
    private Integer seatsBooked;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String status = "CONFIRMED"; // CONFIRMED, COMPLETED, CANCELLED

    @Column(name = "blockchain_tx_hash")
    private String blockchainTxHash;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    // Default Constructor
    public Booking() {
        this.bookedAt = LocalDateTime.now();
        this.status = "CONFIRMED";
    }

    // Constructor with parameters
    public Booking(Ride ride, User passenger, Integer seatsBooked, Double totalAmount) {
        this.ride = ride;
        this.passenger = passenger;
        this.seatsBooked = seatsBooked;
        this.totalAmount = totalAmount;
        this.bookedAt = LocalDateTime.now();
        this.status = "CONFIRMED";
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public Integer getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(Integer seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", rideId=" + (ride != null ? ride.getId() : null) +
                ", passengerId=" + (passenger != null ? passenger.getId() : null) +
                ", seatsBooked=" + seatsBooked +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", bookedAt=" + bookedAt +
                '}';
    }
}