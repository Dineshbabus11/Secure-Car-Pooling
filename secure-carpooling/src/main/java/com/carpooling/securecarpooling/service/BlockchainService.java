package com.carpooling.securecarpooling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import jakarta.annotation.PostConstruct;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

@Service
public class BlockchainService {

    @Autowired
    private Web3j web3j;

    @Autowired
    private Credentials credentials;

    @Autowired
    private DefaultGasProvider gasProvider;

    @Value("${blockchain.contract.address}")
    private String contractAddress;



    /**
     * Record ride creation on blockchain
     */
    public String recordRideCreation(Long rideId, String source, String destination,
                                     Integer seats, Double pricePerSeat) {
        try {
            System.out.println("Recording ride on blockchain: " + rideId);

            // Prepare function call
            Function function = new Function(
                    "createRide",
                    Arrays.asList(
                            new Uint256(rideId),
                            new Utf8String(source),
                            new Utf8String(destination),
                            new Uint256(seats),
                            new Uint256(pricePerSeat.longValue())
                    ),
                    Collections.emptyList()
            );

            // Send transaction
            String txHash = sendTransaction(function);

            System.out.println("Ride recorded on blockchain. TX Hash: " + txHash);
            return txHash;

        } catch (Exception e) {
            System.err.println("Blockchain error: " + e.getMessage());
            e.printStackTrace();
            return "BLOCKCHAIN_ERROR";
        }
    }

    /**
     * Record booking on blockchain
     */
    public String recordBooking(Long bookingId, Long rideId,
                                Integer seatsBooked, Double amount) {
        try {
            System.out.println("Recording booking on blockchain: " + bookingId);

            Function function = new Function(
                    "bookRide",
                    Arrays.asList(
                            new Uint256(bookingId),
                            new Uint256(rideId),
                            new Uint256(seatsBooked),
                            new Uint256(amount.longValue())
                    ),
                    Collections.emptyList()
            );

            String txHash = sendTransaction(function);

            System.out.println("Booking recorded on blockchain. TX Hash: " + txHash);
            return txHash;

        } catch (Exception e) {
            System.err.println("Blockchain error: " + e.getMessage());
            e.printStackTrace();
            return "BLOCKCHAIN_ERROR";
        }
    }

    /**
     * Record cancellation on blockchain
     */
    public String recordCancellation(Long bookingId, String reason, Double penalty) {
        try {
            System.out.println("Recording cancellation on blockchain: " + bookingId);

            Function function = new Function(
                    "cancelBooking",
                    Arrays.asList(
                            new Uint256(bookingId),
                            new Utf8String(reason),
                            new Uint256(penalty.longValue())
                    ),
                    Collections.emptyList()
            );

            String txHash = sendTransaction(function);

            System.out.println("Cancellation recorded on blockchain. TX Hash: " + txHash);
            return txHash;

        } catch (Exception e) {
            System.err.println("Blockchain error: " + e.getMessage());
            e.printStackTrace();
            return "BLOCKCHAIN_ERROR";
        }
    }

    /**
     * Helper method to send transaction to blockchain
     */
    private String sendTransaction(Function function) throws Exception {
        // Encode function
        String encodedFunction = FunctionEncoder.encode(function);

        // Create transaction manager
        TransactionManager transactionManager = new RawTransactionManager(
                web3j,
                credentials
        );

        // Send transaction
        EthSendTransaction ethSendTransaction = transactionManager.sendTransaction(
                gasProvider.getGasPrice(),
                gasProvider.getGasLimit(),
                contractAddress,
                encodedFunction,
                BigInteger.ZERO
        );

        // Get transaction hash
        String transactionHash = ethSendTransaction.getTransactionHash();

        if (transactionHash == null) {
            throw new Exception("Transaction failed: " + ethSendTransaction.getError().getMessage());
        }

        return transactionHash;
    }

    /**
     * Test blockchain connection
     */
    public boolean testConnection() {
        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            System.out.println("Connected to blockchain: " + clientVersion);
            return true;
        } catch (Exception e) {
            System.err.println("Blockchain connection failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Test connection when application starts
     */
    @PostConstruct
    public void init() {
        try {
            System.out.println("========================================");
            System.out.println("🔗 Testing Blockchain Connection...");
            System.out.println("========================================");

            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            System.out.println("✅ Connected to blockchain: " + clientVersion);
            System.out.println("📍 Contract Address: " + contractAddress);
            System.out.println("🔑 Account Address: " + credentials.getAddress());
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("========================================");
            System.err.println("❌ Blockchain connection FAILED!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("⚠️  Make sure Ganache is running on http://127.0.0.1:7545");
            System.err.println("========================================");
        }
    }
}