package com.carpooling.securecarpooling.dto;

public class RegisterRequest {

    private String name;
    private String email;
    private String phone;
    private String password;
    private String idProof;

    // Default Constructor
    public RegisterRequest() {
    }

    // Constructor with parameters
    public RegisterRequest(String name, String email, String phone, String password, String idProof) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.idProof = idProof;
    }

    // Getters and Setters

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

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", idProof='" + idProof + '\'' +
                '}';
    }
}