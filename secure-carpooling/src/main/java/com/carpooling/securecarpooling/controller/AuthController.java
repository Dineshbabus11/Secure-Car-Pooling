package com.carpooling.securecarpooling.controller;

import com.carpooling.securecarpooling.dto.LoginRequest;
import com.carpooling.securecarpooling.dto.LoginResponse;
import com.carpooling.securecarpooling.dto.MessageResponse;
import com.carpooling.securecarpooling.dto.RegisterRequest;
import com.carpooling.securecarpooling.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     * POST: http://localhost:8080/api/auth/register
     * @param registerRequest - User registration data
     * @return Success or error message
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Validate input
            if (registerRequest.getName() == null || registerRequest.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Name is required!"));
            }
            if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Email is required!"));
            }
            if (registerRequest.getPhone() == null || registerRequest.getPhone().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Phone is required!"));
            }
            if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Password is required!"));
            }
            if (registerRequest.getPassword().length() < 6) {
                return ResponseEntity.badRequest().body(new MessageResponse("Password must be at least 6 characters!"));
            }

            // Call service to register user
            String message = authService.registerUser(registerRequest);
            return ResponseEntity.ok(new MessageResponse(message));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Login user
     * POST: http://localhost:8080/api/auth/login
     * @param loginRequest - User login credentials
     * @return LoginResponse with user details
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Validate input
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Email is required!"));
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Password is required!"));
            }

            // Call service to login user
            LoginResponse loginResponse = authService.loginUser(loginRequest);
            return ResponseEntity.ok(loginResponse);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Login failed: " + e.getMessage()));
        }
    }

    /**
     * Test endpoint to check if API is working
     * GET: http://localhost:8080/api/auth/test
     * @return Test message
     */
    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok(new MessageResponse("Auth API is working!"));
    }
}