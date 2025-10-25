package com.carpooling.securecarpooling.service;

import com.carpooling.securecarpooling.dto.CreateRideRequest;
import com.carpooling.securecarpooling.dto.RideResponse;
import com.carpooling.securecarpooling.model.Booking;
import com.carpooling.securecarpooling.model.Ride;
import com.carpooling.securecarpooling.model.User;
import com.carpooling.securecarpooling.repository.RideRepository;
import com.carpooling.securecarpooling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.carpooling.securecarpooling.repository.BookingRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DistanceService distanceService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BlockchainService blockchainService;
    /**
     * Create a new ride
     * @param createRideRequest - Ride details from frontend
     * @param driverId - ID of the user creating the ride
     * @return RideResponse with created ride details
     * @throws RuntimeException if user not found or validation fails
     */
    public RideResponse createRide(CreateRideRequest createRideRequest, Long driverId) {

        // Find the driver
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + driverId));

        // Validate ride date/time (should be in future)
        if (createRideRequest.getDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Ride date/time must be in the future!");
        }

        // Validate seats
        if (createRideRequest.getSeatsAvailable() < 1 || createRideRequest.getSeatsAvailable() > 7) {
            throw new RuntimeException("Seats should be between 1 and 7!");
        }

        // Create new Ride object
        Ride ride = new Ride();
        ride.setDriver(driver);
        ride.setSource(createRideRequest.getSource());
        ride.setDestination(createRideRequest.getDestination());
        ride.setDateTime(createRideRequest.getDateTime());
        ride.setSeatsAvailable(createRideRequest.getSeatsAvailable());
        ride.setTotalSeats(createRideRequest.getSeatsAvailable());
        ride.setCarModel(createRideRequest.getCarModel());
        ride.setCarNumber(createRideRequest.getCarNumber());
        ride.setCarColor(createRideRequest.getCarColor());

        // Calculate distance
        Double distance = distanceService.calculateDistance(
                createRideRequest.getSource(),
                createRideRequest.getDestination()
        );
        ride.setDistanceKm(distance);

        // Calculate price per seat
        Double pricePerSeat = distanceService.calculatePrice(distance, createRideRequest.getSeatsAvailable());
        ride.setPricePerSeat(pricePerSeat);

        // Save ride to database
        Ride savedRide = rideRepository.save(ride);

        // ========== BLOCKCHAIN INTEGRATION ==========
        // Record ride creation on blockchain
        try {
            String txHash = blockchainService.recordRideCreation(
                    savedRide.getId(),
                    savedRide.getSource(),
                    savedRide.getDestination(),
                    savedRide.getTotalSeats(),
                    savedRide.getPricePerSeat()
            );
            savedRide.setBlockchainTxHash(txHash);
            rideRepository.save(savedRide); // Update with blockchain hash

            System.out.println("✅ Ride " + savedRide.getId() + " recorded on blockchain: " + txHash);
        } catch (Exception e) {
            System.err.println("⚠️ Blockchain recording failed (ride still saved in DB): " + e.getMessage());
            // Ride is still saved in database even if blockchain fails
        }
        // ============================================

        // Convert to RideResponse and return
        return convertToRideResponse(savedRide);
    }

    /**
     * Get all available rides
     * @return List of available rides
     */
    public List<RideResponse> getAvailableRides() {
        List<Ride> rides = rideRepository.findByStatusAndSeatsAvailableGreaterThan("ACTIVE", 0);
        return convertToRideResponseList(rides);
    }

    /**
     * Search rides by source and destination
     * @param source - Starting location
     * @param destination - Ending location
     * @return List of matching rides
     */
    public List<RideResponse> searchRides(String source, String destination) {
        List<Ride> rides = rideRepository.findActiveRidesByRoute(source, destination);
        return convertToRideResponseList(rides);
    }

    /**
     * Get rides created by a specific driver
     * @param driverId - Driver's user ID
     * @return List of driver's rides
     */
    public List<RideResponse> getMyRides(Long driverId) {
        List<Ride> rides = rideRepository.findByDriverId(driverId);
        return convertToRideResponseList(rides);
    }

    /**
     * Get ride details by ID
     * @param rideId - Ride ID
     * @return RideResponse with ride details
     * @throws RuntimeException if ride not found
     */
    public RideResponse getRideById(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with ID: " + rideId));
        return convertToRideResponse(ride);
    }

    /**
     * Convert Ride entity to RideResponse DTO
     * @param ride - Ride entity
     * @return RideResponse DTO
     */
    private RideResponse convertToRideResponse(Ride ride) {
        RideResponse response = new RideResponse();
        response.setId(ride.getId());
        response.setDriverId(ride.getDriver().getId());
        response.setDriverName(ride.getDriver().getName());
        response.setDriverPhone(ride.getDriver().getPhone());
        response.setDriverRating(ride.getDriver().getRating());
        response.setSource(ride.getSource());
        response.setDestination(ride.getDestination());
        response.setDateTime(ride.getDateTime());
        response.setSeatsAvailable(ride.getSeatsAvailable());
        response.setTotalSeats(ride.getTotalSeats());
        response.setCarModel(ride.getCarModel());
        response.setCarNumber(ride.getCarNumber());
        response.setCarColor(ride.getCarColor());
        response.setDistanceKm(ride.getDistanceKm());
        response.setPricePerSeat(ride.getPricePerSeat());
        response.setStatus(ride.getStatus());
        return response;
    }

    /**
     * Convert list of Ride entities to list of RideResponse DTOs
     * @param rides - List of Ride entities
     * @return List of RideResponse DTOs
     */
    private List<RideResponse> convertToRideResponseList(List<Ride> rides) {
        List<RideResponse> responseList = new ArrayList<>();
        for (Ride ride : rides) {
            responseList.add(convertToRideResponse(ride));
        }
        return responseList;
    }

    /**
     * Complete a ride
     * @param rideId - Ride ID to complete
     * @param driverId - Driver's user ID
     * @return Success message
     * @throws RuntimeException if validation fails
     */
    @Transactional
    public String completeRide(Long rideId, Long driverId) {

        // Find the ride
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with ID: " + rideId));

        // Check if user is the driver
        if (!ride.getDriver().getId().equals(driverId)) {
            throw new RuntimeException("Only the driver can complete this ride!");
        }

        // Check if ride is active
        if (!ride.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Only active rides can be marked as completed!");
        }

        // Update ride status
        ride.setStatus("COMPLETED");
        rideRepository.save(ride);

        // Mark all confirmed bookings for this ride as completed
        List<Booking> bookings = bookingRepository.findByRideId(rideId);
        for (Booking booking : bookings) {
            if (booking.getStatus().equals("CONFIRMED")) {
                booking.setStatus("COMPLETED");
                bookingRepository.save(booking);
            }
        }

        return "Ride completed successfully! All bookings have been marked as completed.";
    }
}