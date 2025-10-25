package com.carpooling.securecarpooling.repository;

import com.carpooling.securecarpooling.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    // Find all active rides with available seats
    List<Ride> findByStatusAndSeatsAvailableGreaterThan(String status, Integer seats);

    // Find rides by driver ID
    List<Ride> findByDriverId(Long driverId);

    // Find rides by source and destination
    List<Ride> findBySourceAndDestination(String source, String destination);

    // Find active rides by source and destination
    @Query("SELECT r FROM Ride r WHERE r.source = :source AND r.destination = :destination AND r.status = 'ACTIVE' AND r.seatsAvailable > 0")
    List<Ride> findActiveRidesByRoute(@Param("source") String source, @Param("destination") String destination);

    // Find rides by status
    List<Ride> findByStatus(String status);

    // Find upcoming rides (future date/time and active)
    @Query("SELECT r FROM Ride r WHERE r.dateTime > :currentTime AND r.status = 'ACTIVE' AND r.seatsAvailable > 0 ORDER BY r.dateTime ASC")
    List<Ride> findUpcomingRides(@Param("currentTime") LocalDateTime currentTime);

    // Find driver's active rides
    @Query("SELECT r FROM Ride r WHERE r.driver.id = :driverId AND r.status = 'ACTIVE'")
    List<Ride> findActiveRidesByDriver(@Param("driverId") Long driverId);
}