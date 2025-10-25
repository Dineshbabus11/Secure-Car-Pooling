package com.carpooling.securecarpooling.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(name = "id_proof")
    private String idProof;

    @Column(nullable = false)
    private String role = "USER";

    @Column(nullable = false)
    private Double rating = 5.0;

    @Column(name = "total_rides")
    private Integer totalRides = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Default Constructor
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with parameters
    public User(String name, String email, String phone, String password, String idProof) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.idProof = idProof;
        this.createdAt = LocalDateTime.now();
        this.role = "USER";
        this.rating = 5.0;
        this.totalRides = 0;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getTotalRides() {
        return totalRides;
    }

    public void setTotalRides(Integer totalRides) {
        this.totalRides = totalRides;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", rating=" + rating +
                ", totalRides=" + totalRides +
                '}';
    }
}