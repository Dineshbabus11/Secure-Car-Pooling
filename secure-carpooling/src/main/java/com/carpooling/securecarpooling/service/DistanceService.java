package com.carpooling.securecarpooling.service;

import org.springframework.stereotype.Service;

@Service
public class DistanceService {

    // Rate per kilometer in rupees
    private static final Double RATE_PER_KM = 8.0;

    /**
     * Calculate distance between source and destination
     * This is a simple mock implementation
     * In production, use Google Maps Distance Matrix API or similar service
     *
     * @param source - Starting location
     * @param destination - Ending location
     * @return Distance in kilometers
     */
    public Double calculateDistance(String source, String destination) {

        // Convert to lowercase for comparison
        String src = source.toLowerCase().trim();
        String dest = destination.toLowerCase().trim();

        // Mock distance data for demonstration
        // Chennai to Bangalore
        if ((src.contains("chennai") && dest.contains("bangalore")) ||
                (src.contains("bangalore") && dest.contains("chennai"))) {
            return 350.0;
        }

        // Mumbai to Pune
        if ((src.contains("mumbai") && dest.contains("pune")) ||
                (src.contains("pune") && dest.contains("mumbai"))) {
            return 150.0;
        }

        // Delhi to Agra
        if ((src.contains("delhi") && dest.contains("agra")) ||
                (src.contains("agra") && dest.contains("delhi"))) {
            return 230.0;
        }

        // Hyderabad to Vijayawada
        if ((src.contains("hyderabad") && dest.contains("vijayawada")) ||
                (src.contains("vijayawada") && dest.contains("hyderabad"))) {
            return 270.0;
        }

        // Kolkata to Durgapur
        if ((src.contains("kolkata") && dest.contains("durgapur")) ||
                (src.contains("durgapur") && dest.contains("kolkata"))) {
            return 170.0;
        }

        // Default: Random distance between 50-500 km for other routes
        return 100.0 + (Math.random() * 400);
    }

    /**
     * Calculate price per seat based on distance
     *
     * @param distanceKm - Distance in kilometers
     * @param totalSeats - Total number of seats offered
     * @return Price per seat in rupees
     */
    public Double calculatePrice(Double distanceKm, Integer totalSeats) {
        // Total cost = distance × rate per km
        Double totalCost = distanceKm * RATE_PER_KM;

        // Price per seat = total cost ÷ total seats
        Double pricePerSeat = totalCost / totalSeats;

        // Round to 2 decimal places
        return Math.round(pricePerSeat * 100.0) / 100.0;
    }

    /**
     * Calculate total cost for the entire trip
     *
     * @param distanceKm - Distance in kilometers
     * @return Total cost in rupees
     */
    public Double calculateTotalCost(Double distanceKm) {
        return Math.round(distanceKm * RATE_PER_KM * 100.0) / 100.0;
    }
}