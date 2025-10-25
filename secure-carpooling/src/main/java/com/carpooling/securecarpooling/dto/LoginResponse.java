package com.carpooling.securecarpooling.dto;

public class LoginResponse {

    private String role;
    private Long userId;
    private String userName;
    private String email;

    // Default Constructor
    public LoginResponse() {
    }

    // Constructor with parameters
    public LoginResponse(String role, Long userId, String userName, String email) {
        this.role = role;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }

    // Getters and Setters

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "role='" + role + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}