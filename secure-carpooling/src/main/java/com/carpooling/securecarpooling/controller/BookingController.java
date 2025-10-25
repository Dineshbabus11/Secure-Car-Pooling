package com.carpooling.securecarpooling.controller;

import com.carpooling.securecarpooling.dto.BookRideRequest;
import com.carpooling.securecarpooling.dto.BookingResponse;
import com.carpooling.securecarpooling.dto.MessageResponse;
import com.carpooling.securecarpooling.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Book a ride
     * POST: http://localhost:8080/api/bookings/book
     * Header: userId (Long)
     * @param bookRideRequest - Contains rideId and seatsBooked
     * @param userId - Passenger's user ID from header
     * @return BookingResponse with booking details
     */
    @PostMapping("/book")
    public ResponseEntity<?> bookRide(
            @RequestBody BookRideRequest bookRideRequest,
            @RequestHeader("userId") Long userId) {

        try {
            // Validate input
            if (bookRideRequest.getRideId() == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Ride ID is required!"));
            }
            if (bookRideRequest.getSeatsBooked() == null || bookRideRequest.getSeatsBooked() < 1) {
                return ResponseEntity.badRequest().body(new MessageResponse("At least 1 seat must be booked!"));
            }

            // Book the ride
            BookingResponse bookingResponse = bookingService.bookRide(bookRideRequest, userId);
            return ResponseEntity.ok(bookingResponse);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to book ride: " + e.getMessage()));
        }
    }

    /**
     * Get all bookings made by logged-in user (as passenger)
     * GET: http://localhost:8080/api/bookings/my-bookings
     * Header: userId (Long)
     * @param userId - User ID from header
     * @return List of user's bookings
     */
    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings(@RequestHeader("userId") Long userId) {
        try {
            List<BookingResponse> bookings = bookingService.getMyBookings(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch bookings: " + e.getMessage()));
        }
    }

    /**
     * Get all passengers who booked driver's rides
     * GET: http://localhost:8080/api/bookings/passengers
     * Header: userId (Long)
     * @param userId - Driver's user ID from header
     * @return List of bookings for driver's rides
     */
    @GetMapping("/passengers")
    public ResponseEntity<?> getPassengersForDriver(@RequestHeader("userId") Long userId) {
        try {
            List<BookingResponse> bookings = bookingService.getBookingsForDriver(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch passengers: " + e.getMessage()));
        }
    }

    /**
     * Get all bookings for a specific ride
     * GET: http://localhost:8080/api/bookings/ride/{rideId}
     * @param rideId - Ride ID
     * @return List of bookings for the ride
     */
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<?> getBookingsForRide(@PathVariable Long rideId) {
        try {
            List<BookingResponse> bookings = bookingService.getBookingsForRide(rideId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch bookings: " + e.getMessage()));
        }
    }

    /**
     * Get booking details by ID
     * GET: http://localhost:8080/api/bookings/{id}
     * @param id - Booking ID
     * @return BookingResponse with booking details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            BookingResponse booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to fetch booking: " + e.getMessage()));
        }
    }

    /**
     * Cancel a booking
     * PUT: http://localhost:8080/api/bookings/cancel/{bookingId}
     * Header: userId (Long)
     * @param bookingId - Booking ID to cancel
     * @param userId - User ID from header
     * @return Success message
     */
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(
            @PathVariable Long bookingId,
            @RequestHeader("userId") Long userId) {

        try {
            String message = bookingService.cancelBooking(bookingId, userId);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to cancel booking: " + e.getMessage()));
        }
    }

    /**
     * Mark booking as completed
     * PUT: http://localhost:8080/api/bookings/complete/{bookingId}
     * Header: userId (Long)
     * @param bookingId - Booking ID to complete
     * @param userId - User ID from header (driver or passenger)
     * @return Success message
     */
    @PutMapping("/complete/{bookingId}")
    public ResponseEntity<?> completeBooking(
            @PathVariable Long bookingId,
            @RequestHeader("userId") Long userId) {

        try {
            String message = bookingService.completeBooking(bookingId, userId);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to complete booking: " + e.getMessage()));
        }
    }

    /**
     * Test endpoint to check if Booking API is working
     * GET: http://localhost:8080/api/bookings/test
     * @return Test message
     */
    @GetMapping("/test")
    public ResponseEntity<?> testApi() {
        return ResponseEntity.ok(new MessageResponse("Booking API is working!"));
    }
}