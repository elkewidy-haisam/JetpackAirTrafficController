
/*
 * JetPack.java
 * Part of Jetpack Air Traffic Controller
 *
 * Represents an individual jetpack with identification and control methods.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.jetpack;

/**
 * JetPack represents an individual jetpack with identification and control methods.
 * It provides methods for takeoff, landing, movement, and emergency handling.
 *
 * Attributes for identification:
 *   - String serialNumber: Unique identifier for the jetpack.
 *   - String callsign: A name or identifier used during communication.
 *   - String ownerName: Name of the person who owns the jetpack.
 *   - String year: The manufacturing year of the jetpack.
 *   - String model: The model name or number of the jetpack.
 *   - String manufacturer: The company that manufactured the jetpack.
 *
 * Functionality methods:
 *   - void takeOff(): Initiates the jetpack's takeoff sequence.
 *   - void land(): Initiates the jetpack's landing sequence.
 *   - void ascend(): Increases altitude.
 *   - void descend(): Decreases altitude.
 *   - void park(): Parks the jetpack safely.
 *   - void refuel(): Refuels the jetpack.
 *   - void emergencyShutdown(): Shuts down the jetpack in case of emergency.
 *   - void moveLeft(): Moves the jetpack to the left.
 *   - void moveRight(): Moves the jetpack to the right.
 *   - void moveForward(): Moves the jetpack forward.
 *   - void turnAround(): Turns the jetpack a determined angle (in degrees).
 */
public class JetPack {
    private String serialNumber;
    private String callsign;
    private String ownerName;
    private String year;
    private String model;
    private String manufacturer;

    /**
     * Constructs a JetPack with the specified parameters.
     */
    public JetPack(String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer) {
        this.serialNumber = serialNumber;
        this.callsign = callsign;
        this.ownerName = ownerName;
        this.year = year;
        this.model = model;
        this.manufacturer = manufacturer;
    }

    /**
     * Initiates the jetpack's takeoff sequence.
     */
    public void takeOff() {
        // Jetpack is taking off
    }

    /**
     * Initiates the jetpack's landing sequence.
     */
    public void land() {
        // Jetpack is landing
    }

    /**
     * Increases altitude.
     */
    public void ascend() {
        // Jetpack is ascending
    }

    /**
     * Decreases altitude.
     */
    public void descend() {
        // Jetpack is descending
    }

    /**
     * Parks the jetpack safely.
     */
    public void park() {
        // Jetpack is parked
    }

    /**
     * Refuels the jetpack.
     */
    public void refuel() {
        // Jetpack is refueling
    }

    /**
     * Shuts down the jetpack in case of emergency.
     */
    public void emergencyShutdown() {
        // Emergency shutdown
    }

    /**
     * Moves the jetpack to the left.
     */
    public void moveLeft() {
        // Move jetpack left
    }

    /**
     * Moves the jetpack to the right.
     */
    public void moveRight() {
        // Move jetpack right
    }

    /**
     * Moves the jetpack forward.
     */
    public void moveForward() {
        // Move jetpack forward
    }

    /**
     * Turns the jetpack a determined angle (in degrees).
     */
    public void turnAround() {
        // Turn jetpack around
    }

    // Getters and setters
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
