package com.carpooling.securecarpooling.repository;

import com.carpooling.securecarpooling.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find bookings by passenger ID
    List<Booking> findByPassengerId(Long passengerId);

    // Find bookings by ride ID
    List<Booking> findByRideId(Long rideId);

    // Find bookings by status
    List<Booking> findByStatus(String status);

    // Find passenger's confirmed bookings
    @Query("SELECT b FROM Booking b WHERE b.passenger.id = :passengerId AND b.status = 'CONFIRMED'")
    List<Booking> findConfirmedBookingsByPassenger(@Param("passengerId") Long passengerId);

    // Find all bookings for a specific driver's rides
    @Query("SELECT b FROM Booking b WHERE b.ride.driver.id = :driverId")
    List<Booking> findBookingsByDriver(@Param("driverId") Long driverId);

    // Check if user already booked a specific ride
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.ride.id = :rideId AND b.passenger.id = :passengerId AND b.status = 'CONFIRMED'")
    boolean existsByRideIdAndPassengerId(@Param("rideId") Long rideId, @Param("passengerId") Long passengerId);
}