// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

contract CarPooling {
    
    // Structure to store Ride information
    struct RideRecord {
        uint256 rideId;           // Ride ID from database
        address driver;           // Blockchain address of driver
        string source;            // Starting location
        string destination;       // Ending location
        uint256 seats;            // Number of seats
        uint256 pricePerSeat;     // Price per seat
        uint256 timestamp;        // When ride was created
    }
    
    // Structure to store Booking information
    struct BookingRecord {
        uint256 bookingId;        // Booking ID from database
        uint256 rideId;           // Which ride was booked
        address passenger;        // Who booked
        uint256 seatsBooked;      // How many seats
        uint256 amount;           // Total amount paid
        uint256 timestamp;        // When booking was made
    }
    
    // Structure to store Cancellation information
    struct CancellationRecord {
        uint256 bookingId;        // Which booking was cancelled
        address cancelledBy;      // Who cancelled
        string reason;            // Why cancelled
        uint256 penalty;          // Penalty amount
        uint256 timestamp;        // When cancelled
    }
    
    // Storage: mapping to store all records
    mapping(uint256 => RideRecord) public rides;
    mapping(uint256 => BookingRecord) public bookings;
    mapping(uint256 => CancellationRecord) public cancellations;
    
    // Counters
    uint256 public rideCount = 0;
    uint256 public bookingCount = 0;
    uint256 public cancellationCount = 0;
    
    // Events (like logs - to track what happened)
    event RideCreated(uint256 rideId, address driver, string source, string destination);
    event RideBooked(uint256 bookingId, uint256 rideId, address passenger, uint256 amount);
    event BookingCancelled(uint256 bookingId, address cancelledBy, uint256 penalty);
    
    // Function 1: Record a new ride on blockchain
    function createRide(
        uint256 _rideId,
        string memory _source,
        string memory _destination,
        uint256 _seats,
        uint256 _pricePerSeat
    ) public {
        rideCount++;
        rides[_rideId] = RideRecord(
            _rideId,
            msg.sender,              // Driver's blockchain address
            _source,
            _destination,
            _seats,
            _pricePerSeat,
            block.timestamp          // Current blockchain time
        );
        
        emit RideCreated(_rideId, msg.sender, _source, _destination);
    }
    
    // Function 2: Record a booking on blockchain
    function bookRide(
        uint256 _bookingId,
        uint256 _rideId,
        uint256 _seatsBooked,
        uint256 _amount
    ) public {
        bookingCount++;
        bookings[_bookingId] = BookingRecord(
            _bookingId,
            _rideId,
            msg.sender,              // Passenger's blockchain address
            _seatsBooked,
            _amount,
            block.timestamp
        );
        
        emit RideBooked(_bookingId, _rideId, msg.sender, _amount);
    }
    
    // Function 3: Record a cancellation on blockchain
    function cancelBooking(
        uint256 _bookingId,
        string memory _reason,
        uint256 _penalty
    ) public {
        cancellationCount++;
        cancellations[_bookingId] = CancellationRecord(
            _bookingId,
            msg.sender,              // Who cancelled
            _reason,
            _penalty,
            block.timestamp
        );
        
        emit BookingCancelled(_bookingId, msg.sender, _penalty);
    }
    
    // Function 4: Get ride details by ID
    function getRide(uint256 _rideId) public view returns (
        uint256 rideId,
        address driver,
        string memory source,
        string memory destination,
        uint256 seats,
        uint256 pricePerSeat,
        uint256 timestamp
    ) {
        RideRecord memory ride = rides[_rideId];
        return (
            ride.rideId,
            ride.driver,
            ride.source,
            ride.destination,
            ride.seats,
            ride.pricePerSeat,
            ride.timestamp
        );
    }
    
    // Function 5: Get booking details by ID
    function getBooking(uint256 _bookingId) public view returns (
        uint256 bookingId,
        uint256 rideId,
        address passenger,
        uint256 seatsBooked,
        uint256 amount,
        uint256 timestamp
    ) {
        BookingRecord memory booking = bookings[_bookingId];
        return (
            booking.bookingId,
            booking.rideId,
            booking.passenger,
            booking.seatsBooked,
            booking.amount,
            booking.timestamp
        );
    }
}