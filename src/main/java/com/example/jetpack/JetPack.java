package com.example.jetpack;
/**
 *  Jetpack.java
 *  by Haisam Elkewidy
 * 
 *   This class represents an individual jetpack someone purchased and uses to fly around within a locale.
 *   It contains attributes and methods relevant to the jetpack's functionality as well as identification.
 * 
 *   Attributes for identification:
 *   - String serialNumber: Unique identifier for the jetpack.
 *   - String callsign: A name or identifier used during communication.
 *   - String ownerName: Name of the person who owns the jetpack.
 *   - String year: The manufacturing year of the jetpack.
 *   - String model: The model name or number of the jetpack.
 *   - String manufacturer: The company that manufactured the jetpack.
 *    
 *    Functionality methods:
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
 * 
 */
public class JetPack {

    private String serialNumber;
    private String callsign;
    private String ownerName;
    private String year;
    private String model;
    private String manufacturer;

    public JetPack(String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer) {
        this.serialNumber = serialNumber;
        this.callsign = callsign;
        this.ownerName = ownerName;
        this.year = year;
        this.model = model;
        this.manufacturer = manufacturer;
    }

    public void takeOff() {
        // Jetpack is taking off
    }

    public void land() {
        // Jetpack is landing
    }

    public void ascend() {
        // Jetpack is ascending
    }

    public void descend() {
        // Jetpack is descending
    }

    public void park() {
        // Jetpack is parking
    }

    public void refuel() {
        // Jetpack is refueling
    }

    public void emergencyShutdown() {
        // Jetpack performing emergency shutdown
    }

    public void moveLeft() {
        // Jetpack moving left
    }

    public void moveRight() {
        // Jetpack moving right
    }

    public void moveForward() {
        // Jetpack moving forward
    }

    public void turnAround(int degrees) {
        // Jetpack turning around
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

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
    
}
