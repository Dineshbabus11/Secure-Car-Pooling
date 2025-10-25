package com.carpooling.securecarpooling.controller;

import com.carpooling.securecarpooling.dto.CreateRideRequest;
import com.carpooling.securecarpooling.dto.MessageResponse;
import com.carpooling.securecarpooling.dto.RideResponse;
import com.carpooling.securecarpooling.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "http://localhost:3000")
public class RideController {

    @Autowired
    private RideService rideService;

    /**
     * Create a new ride
     * POST: http://localhost:8080/api/rides/create
     * Header: userId (Long)
     * @param createRideRequest - Ride details
     * @param userId - Driver's user ID from header
     * @return RideResponse with created ride details
     */
    @PostMapping("/create")
    public ResponseEntity<?> createRide(
            @RequestBody CreateRideRequest createRideRequest,
            @RequestHeader("userId") Long userId) {

        try {
            // Validate input
            if (createRideRequest.getSource() == null || createRideRequest.getSource().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Source location is required!"));
            }
            if (createRideRequest.getDestination() == null || createRideRequest.getDestination().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Destination location is required!"));
            }
            if (createRideRequest.getDateTime() == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Date and time is required!"));
            }
            if (createRideRequest.getSeatsAvailable() == null || createRideRequest.getSeatsAvailable() < 1) {
                return ResponseEntity.badRequest().body(new MessageResponse("At least 1 seat is required!"));
            }
            if (createRideRequest.getCarModel() == null || createRideRequest.getCarModel().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Car model is required!"));
            }
            if (createRideRequest.getCarNumber() == null || createRideRequest.getCarNumber().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Car number is required!"));
            }
            if (createRideRequest.getCarColor() == null || createRideRequest.getCarColor().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Car color is required!"));
            }

            // Create ride
            RideResponse rideResponse = rideService.createRide(createRideRequest, userId);
            return ResponseEntity.ok(rideResponse);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to create ride: " + e.getMessage()));
        }
    }

    /**
     * Get all available rides
     * GET: http://localhost:8080/api/rides/available
     * @return List of available rides
     */
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableRides() {
        try {
            List<RideResponse> rides = rideService.getAvailableRides();
            return ResponseEntity.ok(rides);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch rides: " + e.getMessage()));
        }
    }

    /**
     * Search rides by source and destination
     * GET: http://localhost:8080/api/rides/search?source=Chennai&destination=Bangalore
     * @param source - Starting location
     * @param destination - Ending location
     * @return List of matching rides
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchRides(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination) {

        try {
            // If both source and destination provided, search by route
            if (source != null && !source.trim().isEmpty() &&
                    destination != null && !destination.trim().isEmpty()) {
                List<RideResponse> rides = rideService.searchRides(source, destination);
                return ResponseEntity.ok(rides);
            } else {
                // Otherwise return all available rides
                List<RideResponse> rides = rideService.getAvailableRides();
                return ResponseEntity.ok(rides);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to search rides: " + e.getMessage()));
        }
    }

    /**
     * Get rides created by logged-in user
     * GET: http://localhost:8080/api/rides/my-rides
     * Header: userId (Long)
     * @param userId - User ID from header
     * @return List of user's rides
     */
    @GetMapping("/my-rides")
    public ResponseEntity<?> getMyRides(@RequestHeader("userId") Long userId) {
        try {
            List<RideResponse> rides = rideService.getMyRides(userId);
            return ResponseEntity.ok(rides);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch your rides: " + e.getMessage()));
        }
    }

    /**
     * Get ride details by ID
     * GET: http://localhost:8080/api/rides/{id}
     * @param id - Ride ID
     * @return RideResponse with ride details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRideById(@PathVariable Long id) {
        try {
            RideResponse ride = rideService.getRideById(id);
            return ResponseEntity.ok(ride);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch ride details: " + e.getMessage()));
        }
    }

    /**
     * Complete a ride
     * PUT: http://localhost:8080/api/rides/complete/{id}
     * Header: userId (Long)
     * @param id - Ride ID to complete
     * @param userId - Driver's user ID from header
     * @return Success message
     */
    @PutMapping("/complete/{id}")
    public ResponseEntity<?> completeRide(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId) {

        try {
            String message = rideService.completeRide(id, userId);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to complete ride: " + e.getMessage()));
        }
    }

    /**
     * Test endpoint to check if Ride API is working
     * GET: http://localhost:8080/api/rides/test
     * @return Test message
     */
    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok(new MessageResponse("Ride API is working!"));
    }
}