package com.carpooling.securecarpooling.service;

import com.carpooling.securecarpooling.dto.LoginRequest;
import com.carpooling.securecarpooling.dto.LoginResponse;
import com.carpooling.securecarpooling.dto.RegisterRequest;
import com.carpooling.securecarpooling.model.User;
import com.carpooling.securecarpooling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     * @param registerRequest - contains user registration data
     * @return Success message
     * @throws RuntimeException if email already exists
     */
    public String registerUser(RegisterRequest registerRequest) {

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        // Check if phone already exists
        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new RuntimeException("Phone number already exists!");
        }

        // Create new user object
        User newUser = new User();
        newUser.setName(registerRequest.getName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPhone(registerRequest.getPhone());
        newUser.setIdProof(registerRequest.getIdProof());

        // Encrypt password before saving
        String encryptedPassword = passwordEncoder.encode(registerRequest.getPassword());
        newUser.setPassword(encryptedPassword);

        // Save user to database
        userRepository.save(newUser);

        return "User registered successfully!";
    }

    /**
     * Login user
     * @param loginRequest - contains email and password
     * @return LoginResponse with user details
     * @throws RuntimeException if credentials are invalid
     */
    public LoginResponse loginUser(LoginRequest loginRequest) {

        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with email: " + loginRequest.getEmail());
        }

        User user = userOptional.get();

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        // Create and return login response
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setRole(user.getRole());
        loginResponse.setUserId(user.getId());
        loginResponse.setUserName(user.getName());
        loginResponse.setEmail(user.getEmail());

        return loginResponse;
    }

    /**
     * Get user by ID
     * @param userId - user ID
     * @return User object
     * @throws RuntimeException if user not found
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /**
     * Get user by email
     * @param email - user email
     * @return User object
     * @throws RuntimeException if user not found
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}