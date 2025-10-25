package com.carpooling.securecarpooling.service;

import com.carpooling.securecarpooling.dto.BookingResponse;
import com.carpooling.securecarpooling.dto.RideResponse;
import com.carpooling.securecarpooling.model.Booking;
import com.carpooling.securecarpooling.model.Ride;
import com.carpooling.securecarpooling.model.User;
import com.carpooling.securecarpooling.repository.BookingRepository;
import com.carpooling.securecarpooling.repository.RideRepository;
import com.carpooling.securecarpooling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Check if user is admin
     * @param userId - User ID to check
     * @return true if admin, false otherwise
     */
    public boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole().equals("ADMIN");
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get all rides
     * @return List of all rides
     */
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    /**
     * Get all bookings
     * @return List of all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Get dashboard statistics
     * @return Map with statistics
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Total counts
        long totalUsers = userRepository.count();
        long totalRides = rideRepository.count();
        long totalBookings = bookingRepository.count();

        // Active rides
        List<Ride> activeRides = rideRepository.findByStatus("ACTIVE");
        long activeRidesCount = activeRides.size();

        // Confirmed bookings
        List<Booking> confirmedBookings = bookingRepository.findByStatus("CONFIRMED");
        long confirmedBookingsCount = confirmedBookings.size();

        // Completed bookings
        List<Booking> completedBookings = bookingRepository.findByStatus("COMPLETED");
        long completedBookingsCount = completedBookings.size();

        // Cancelled bookings
        List<Booking> cancelledBookings = bookingRepository.findByStatus("CANCELLED");
        long cancelledBookingsCount = cancelledBookings.size();

        // Total revenue (sum of all completed bookings)
        double totalRevenue = 0.0;
        for (Booking booking : completedBookings) {
            totalRevenue += booking.getTotalAmount();
        }
        totalRevenue = Math.round(totalRevenue * 100.0) / 100.0;

        // Build stats map
        stats.put("totalUsers", totalUsers);
        stats.put("totalRides", totalRides);
        stats.put("totalBookings", totalBookings);
        stats.put("activeRides", activeRidesCount);
        stats.put("confirmedBookings", confirmedBookingsCount);
        stats.put("completedBookings", completedBookingsCount);
        stats.put("cancelledBookings", cancelledBookingsCount);
        stats.put("totalRevenue", totalRevenue);

        return stats;
    }

    /**
     * Get user by ID
     * @param userId - User ID
     * @return User object
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /**
     * Delete user by ID
     * @param userId - User ID to delete
     * @return Success message
     */
    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Cannot delete admin user!");
        }

        userRepository.deleteById(userId);
        return "User deleted successfully!";
    }

    /**
     * Update user role
     * @param userId - User ID
     * @param newRole - New role (USER or ADMIN)
     * @return Updated user
     */
    public User updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (!newRole.equals("USER") && !newRole.equals("ADMIN")) {
            throw new RuntimeException("Invalid role! Use USER or ADMIN");
        }

        user.setRole(newRole);
        return userRepository.save(user);
    }

    /**
     * Get rides by status
     * @param status - Ride status (ACTIVE, IN_PROGRESS, COMPLETED, CANCELLED)
     * @return List of rides with given status
     */
    public List<Ride> getRidesByStatus(String status) {
        return rideRepository.findByStatus(status);
    }

    /**
     * Get bookings by status
     * @param status - Booking status (CONFIRMED, COMPLETED, CANCELLED)
     * @return List of bookings with given status
     */
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    /**
     * Delete ride by ID
     * @param rideId - Ride ID to delete
     * @return Success message
     */
    public String deleteRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with ID: " + rideId));

        // Check if ride has active bookings
        List<Booking> bookings = bookingRepository.findByRideId(rideId);
        for (Booking booking : bookings) {
            if (booking.getStatus().equals("CONFIRMED")) {
                throw new RuntimeException("Cannot delete ride with active bookings!");
            }
        }

        rideRepository.deleteById(rideId);
        return "Ride deleted successfully!";
    }

    /**
     * Get all users with their statistics
     * @return List of user stats
     */
    public List<Map<String, Object>> getUsersWithStats() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> userStatsList = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> userStats = new HashMap<>();
            userStats.put("id", user.getId());
            userStats.put("name", user.getName());
            userStats.put("email", user.getEmail());
            userStats.put("phone", user.getPhone());
            userStats.put("role", user.getRole());
            userStats.put("rating", user.getRating());
            userStats.put("totalRides", user.getTotalRides());
            userStats.put("createdAt", user.getCreatedAt());

            // Count rides created by user
            List<Ride> ridesCreated = rideRepository.findByDriverId(user.getId());
            userStats.put("ridesCreated", ridesCreated.size());

            // Count bookings made by user
            List<Booking> bookingsMade = bookingRepository.findByPassengerId(user.getId());
            userStats.put("bookingsMade", bookingsMade.size());

            userStatsList.add(userStats);
        }

        return userStatsList;
    }
}