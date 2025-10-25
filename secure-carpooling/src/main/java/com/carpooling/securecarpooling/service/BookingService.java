package com.carpooling.securecarpooling.service;

import com.carpooling.securecarpooling.dto.BookRideRequest;
import com.carpooling.securecarpooling.dto.BookingResponse;
import com.carpooling.securecarpooling.model.Booking;
import com.carpooling.securecarpooling.model.Ride;
import com.carpooling.securecarpooling.model.User;
import com.carpooling.securecarpooling.repository.BookingRepository;
import com.carpooling.securecarpooling.repository.RideRepository;
import com.carpooling.securecarpooling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockchainService blockchainService;

    /**
     * Book a ride
     * @param bookRideRequest - Contains rideId and seatsBooked
     * @param passengerId - ID of the user booking the ride
     * @return BookingResponse with booking details
     * @throws RuntimeException if validation fails
     */
    @Transactional
    public BookingResponse bookRide(BookRideRequest bookRideRequest, Long passengerId) {

        // Validate seats booked
        if (bookRideRequest.getSeatsBooked() == null || bookRideRequest.getSeatsBooked() < 1) {
            throw new RuntimeException("At least 1 seat must be booked!");
        }

        // Find the ride
        Ride ride = rideRepository.findById(bookRideRequest.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found with ID: " + bookRideRequest.getRideId()));

        // Find the passenger
        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + passengerId));

        // Validation 1: Check if ride is active
        if (!ride.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("This ride is not available for booking!");
        }

        // Validation 2: Check if passenger is trying to book their own ride
        if (ride.getDriver().getId().equals(passengerId)) {
            throw new RuntimeException("You cannot book your own ride!");
        }

        // Validation 3: Check if user already booked this ride
        boolean alreadyBooked = bookingRepository.existsByRideIdAndPassengerId(
                bookRideRequest.getRideId(), passengerId);
        if (alreadyBooked) {
            throw new RuntimeException("You have already booked this ride!");
        }

        // Validation 4: Check if enough seats are available
        if (ride.getSeatsAvailable() < bookRideRequest.getSeatsBooked()) {
            throw new RuntimeException("Only " + ride.getSeatsAvailable() + " seat(s) available!");
        }

        // Calculate total amount
        Double totalAmount = ride.getPricePerSeat() * bookRideRequest.getSeatsBooked();
        totalAmount = Math.round(totalAmount * 100.0) / 100.0; // Round to 2 decimal places

        // Create booking
        Booking booking = new Booking();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        booking.setSeatsBooked(bookRideRequest.getSeatsBooked());
        booking.setTotalAmount(totalAmount);
        booking.setStatus("CONFIRMED");

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);

        // Update ride seats
        ride.setSeatsAvailable(ride.getSeatsAvailable() - bookRideRequest.getSeatsBooked());
        rideRepository.save(ride);
        try {
            String txHash = blockchainService.recordBooking(
                    savedBooking.getId(),
                    savedBooking.getRide().getId(),
                    savedBooking.getSeatsBooked(),
                    savedBooking.getTotalAmount()
            );
            savedBooking.setBlockchainTxHash(txHash);
            bookingRepository.save(savedBooking); // Update with blockchain hash

            System.out.println("✅ Booking " + savedBooking.getId() + " recorded on blockchain: " + txHash);
        } catch (Exception e) {
            System.err.println("⚠️ Blockchain recording failed (booking still saved in DB): " + e.getMessage());
        }
        // ============================================

        // Convert to response and return
        return convertToBookingResponse(savedBooking);
    }

    /**
     * Get all bookings made by a passenger
     * @param passengerId - Passenger's user ID
     * @return List of bookings
     */
    public List<BookingResponse> getMyBookings(Long passengerId) {
        List<Booking> bookings = bookingRepository.findByPassengerId(passengerId);
        return convertToBookingResponseList(bookings);
    }

    /**
     * Get all bookings for a specific ride
     * @param rideId - Ride ID
     * @return List of bookings for the ride
     */
    public List<BookingResponse> getBookingsForRide(Long rideId) {
        List<Booking> bookings = bookingRepository.findByRideId(rideId);
        return convertToBookingResponseList(bookings);
    }

    /**
     * Get all bookings for driver's rides (passengers who booked driver's rides)
     * @param driverId - Driver's user ID
     * @return List of bookings
     */
    public List<BookingResponse> getBookingsForDriver(Long driverId) {
        List<Booking> bookings = bookingRepository.findBookingsByDriver(driverId);
        return convertToBookingResponseList(bookings);
    }

    /**
     * Get booking details by ID
     * @param bookingId - Booking ID
     * @return BookingResponse with booking details
     * @throws RuntimeException if booking not found
     */
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        return convertToBookingResponse(booking);
    }

    /**
     * Cancel a booking
     * @param bookingId - Booking ID
     * @param userId - User ID requesting cancellation
     * @return Success message
     * @throws RuntimeException if validation fails
     */
    @Transactional
    public String cancelBooking(Long bookingId, Long userId) {

        // Find booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // Check if user is the passenger who made the booking
        if (!booking.getPassenger().getId().equals(userId)) {
            throw new RuntimeException("You can only cancel your own bookings!");
        }

        // Check if booking is already cancelled or completed
        if (booking.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Booking is already cancelled!");
        }
        if (booking.getStatus().equals("COMPLETED")) {
            throw new RuntimeException("Cannot cancel a completed booking!");
        }

        // Update booking status
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // Return seats to ride
        Ride ride = booking.getRide();
        ride.setSeatsAvailable(ride.getSeatsAvailable() + booking.getSeatsBooked());
        rideRepository.save(ride);
        // ========== BLOCKCHAIN INTEGRATION ==========
        // Record cancellation on blockchain
        try {
            String txHash = blockchainService.recordCancellation(
                    booking.getId(),
                    "User cancelled booking",
                    0.0  // No penalty for now
            );

            System.out.println("✅ Cancellation " + booking.getId() + " recorded on blockchain: " + txHash);
        } catch (Exception e) {
            System.err.println("⚠️ Blockchain recording failed (cancellation still processed): " + e.getMessage());
        }
        // ============================================

        return "Booking cancelled successfully! " + booking.getSeatsBooked() + " seat(s) returned.";
    }

    /**
     * Mark booking as completed
     * @param bookingId - Booking ID
     * @param userId - User ID (driver or passenger)
     * @return Success message
     * @throws RuntimeException if validation fails
     */
    @Transactional
    public String completeBooking(Long bookingId, Long userId) {

        // Find booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // Check if user is passenger or driver
        boolean isPassenger = booking.getPassenger().getId().equals(userId);
        boolean isDriver = booking.getRide().getDriver().getId().equals(userId);

        if (!isPassenger && !isDriver) {
            throw new RuntimeException("Only passenger or driver can mark booking as completed!");
        }

        // Check if booking is confirmed
        if (!booking.getStatus().equals("CONFIRMED")) {
            throw new RuntimeException("Only confirmed bookings can be marked as completed!");
        }

        // Update booking status
        booking.setStatus("COMPLETED");
        bookingRepository.save(booking);

        return "Booking marked as completed successfully!";
    }

    /**
     * Convert Booking entity to BookingResponse DTO
     * @param booking - Booking entity
     * @return BookingResponse DTO
     */
    private BookingResponse convertToBookingResponse(Booking booking) {
        BookingResponse response = new BookingResponse();

        response.setId(booking.getId());
        response.setRideId(booking.getRide().getId());
        response.setSource(booking.getRide().getSource());
        response.setDestination(booking.getRide().getDestination());
        response.setRideDateTime(booking.getRide().getDateTime());

        response.setPassengerId(booking.getPassenger().getId());
        response.setPassengerName(booking.getPassenger().getName());
        response.setPassengerPhone(booking.getPassenger().getPhone());

        response.setDriverId(booking.getRide().getDriver().getId());
        response.setDriverName(booking.getRide().getDriver().getName());
        response.setDriverPhone(booking.getRide().getDriver().getPhone());

        response.setCarModel(booking.getRide().getCarModel());
        response.setCarNumber(booking.getRide().getCarNumber());
        response.setCarColor(booking.getRide().getCarColor());

        response.setSeatsBooked(booking.getSeatsBooked());
        response.setPricePerSeat(booking.getRide().getPricePerSeat());
        response.setTotalAmount(booking.getTotalAmount());
        response.setStatus(booking.getStatus());
        response.setBookedAt(booking.getBookedAt());

        return response;
    }

    /**
     * Convert list of Booking entities to list of BookingResponse DTOs
     * @param bookings - List of Booking entities
     * @return List of BookingResponse DTOs
     */
    private List<BookingResponse> convertToBookingResponseList(List<Booking> bookings) {
        List<BookingResponse> responseList = new ArrayList<>();
        for (Booking booking : bookings) {
            responseList.add(convertToBookingResponse(booking));
        }
        return responseList;
    }
}