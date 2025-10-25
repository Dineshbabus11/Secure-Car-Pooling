package com.carpooling.securecarpooling.controller;

import com.carpooling.securecarpooling.dto.MessageResponse;
import com.carpooling.securecarpooling.model.Booking;
import com.carpooling.securecarpooling.model.Ride;
import com.carpooling.securecarpooling.model.User;
import com.carpooling.securecarpooling.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Get dashboard statistics
     * GET: http://localhost:8080/api/admin/dashboard
     * Header: userId (Long)
     * @param userId - Admin user ID from header
     * @return Dashboard statistics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats(@RequestHeader("userId") Long userId) {
        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            Map<String, Object> stats = adminService.getDashboardStats();
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch dashboard stats: " + e.getMessage()));
        }
    }

    /**
     * Get all users
     * GET: http://localhost:8080/api/admin/users
     * Header: userId (Long)
     * @param userId - Admin user ID from header
     * @return List of all users
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader("userId") Long userId) {
        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            List<User> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch users: " + e.getMessage()));
        }
    }

    /**
     * Get all users with statistics
     * GET: http://localhost:8080/api/admin/users/stats
     * Header: userId (Long)
     * @param userId - Admin user ID from header
     * @return List of users with their stats
     */
    @GetMapping("/users/stats")
    public ResponseEntity<?> getUsersWithStats(@RequestHeader("userId") Long userId) {
        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            List<Map<String, Object>> usersWithStats = adminService.getUsersWithStats();
            return ResponseEntity.ok(usersWithStats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch user stats: " + e.getMessage()));
        }
    }

    /**
     * Get all rides
     * GET: http://localhost:8080/api/admin/rides
     * Header: userId (Long)
     * @param userId - Admin user ID from header
     * @return List of all rides
     */
    @GetMapping("/rides")
    public ResponseEntity<?> getAllRides(@RequestHeader("userId") Long userId) {
        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            List<Ride> rides = adminService.getAllRides();
            return ResponseEntity.ok(rides);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch rides: " + e.getMessage()));
        }
    }

    /**
     * Get rides by status
     * GET: http://localhost:8080/api/admin/rides/status/{status}
     * Header: userId (Long)
     * @param userId - Admin user ID from header
     * @param status - Ride status
     * @return List of rides with given status
     */
    @GetMapping("/rides/status/{status}")
    public ResponseEntity<?> getRidesByStatus(
            @RequestHeader("userId") Long userId,
            @PathVariable String status) {

        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            List<Ride> rides = adminService.getRidesByStatus(status.toUpperCase());
            return ResponseEntity.ok(rides);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch rides: " + e.getMessage()));
        }
    }

    /**
     * Get all bookings
     * GET: http://localhost:8080/api/admin/bookings
     * Header: userId (Long)
     * @param userId - Admin user ID from header
     * @return List of all bookings
     */
    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings(@RequestHeader("userId") Long userId) {
        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            List<Booking> bookings = adminService.getAllBookings();
            return ResponseEntity.ok(bookings);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch bookings: " + e.getMessage()));
        }
    }

    /**
     * Get bookings by status
     * GET: http://localhost:8080/api/admin/bookings/status/{status}
     * Header: userId (Long)
     * @param userId - Admin user ID from header
     * @param status - Booking status
     * @return List of bookings with given status
     */
    @GetMapping("/bookings/status/{status}")
    public ResponseEntity<?> getBookingsByStatus(
            @RequestHeader("userId") Long userId,
            @PathVariable String status) {

        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            List<Booking> bookings = adminService.getBookingsByStatus(status.toUpperCase());
            return ResponseEntity.ok(bookings);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch bookings: " + e.getMessage()));
        }
    }

    /**
     * Delete user
     * DELETE: http://localhost:8080/api/admin/users/{id}
     * Header: userId (Long)
     * @param userId - Admin user ID from header
     * @param id - User ID to delete
     * @return Success message
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(
            @RequestHeader("userId") Long userId,
            @PathVariable Long id) {

        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            String message = adminService.deleteUser(id);
            return ResponseEntity.ok(new MessageResponse(message));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to delete user: " + e.getMessage()));
        }
    }

    /**
     * Update user role
     * PUT: http://localhost:8080/api/admin/users/{id}/role
     * Header: userId (Long)
     * Body: { "role": "ADMIN" }
     * @param userId - Admin user ID from header
     * @param id - User ID to update
     * @param roleData - Map containing new role
     * @return Updated user
     */
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(
            @RequestHeader("userId") Long userId,
            @PathVariable Long id,
            @RequestBody Map<String, String> roleData) {

        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            String newRole = roleData.get("role");
            if (newRole == null || newRole.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Role is required!"));
            }

            User updatedUser = adminService.updateUserRole(id, newRole.toUpperCase());
            return ResponseEntity.ok(updatedUser);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to update role: " + e.getMessage()));
        }
    }

    /**
     * Delete ride
     * DELETE: http://localhost:8080/api/admin/rides/{id}
     * Header: userId (Long)
     * @param userId - Admin user ID from header
     * @param id - Ride ID to delete
     * @return Success message
     */
    @DeleteMapping("/rides/{id}")
    public ResponseEntity<?> deleteRide(
            @RequestHeader("userId") Long userId,
            @PathVariable Long id) {

        try {
            // Check if user is admin
            if (!adminService.isAdmin(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse("Access denied! Admin only."));
            }

            String message = adminService.deleteRide(id);
            return ResponseEntity.ok(new MessageResponse(message));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to delete ride: " + e.getMessage()));
        }
    }

    /**
     * Test endpoint to check if Admin API is working
     * GET: http://localhost:8080/api/admin/test
     * @return Test message
     */
    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok(new MessageResponse("Admin API is working!"));
    }
}