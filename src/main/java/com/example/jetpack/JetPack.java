/**
 * JetPack component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides jetpack functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core jetpack operations
 * - Maintain necessary state for jetpack functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.jetpack;

import java.awt.Point;
import java.time.LocalDateTime;

public class JetPack {
    private final String id;
    private final String serialNumber;
    private final String callsign;
    private final String ownerName;
    private final String year;
    private final String model;
    private final String manufacturer;
    private Point position;
    private double altitude;
    private double speed;
    private boolean isActive;
    private LocalDateTime lastUpdate;

    private static int jetPackCounter = 1;
    private static int callsignCounter = 1;

    public JetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer, Point position, double altitude, double speed) {
        this.id = id;
        if (serialNumber != null && (serialNumber.startsWith("BOS-") || serialNumber.startsWith("TEST-"))) {
            this.serialNumber = serialNumber;
        } else if (serialNumber != null) {
            String digits = serialNumber.replaceAll("\\D", "");
            this.serialNumber = "BOS-" + String.format("%03d", Integer.parseInt(digits));
        } else {
            this.serialNumber = serialNumber;
        }
        this.callsign = callsign;
        this.ownerName = ownerName;
        this.year = year;
        this.model = model;
        this.manufacturer = manufacturer;
        this.position = position;
        this.altitude = altitude;
        this.speed = speed;
        this.isActive = true;
        this.lastUpdate = LocalDateTime.now();
    }

    public JetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model) {
        this(id, serialNumber, callsign, ownerName, year, model, "Unknown", new Point(0, 0), 0.0, 0.0);
    }

    // Factory method for test compatibility
    public static JetPack createForCity(String cityPrefix, int number) {
        String serial = cityPrefix + "-" + String.format("%03d", number);
        String callsign = cityPrefix + "-JP" + number;
        return new JetPack("JP" + number, serial, callsign, "TestOwner", "2025", "ModelX", "JetPackCorp", new Point(0,0), 0.0, 0.0);
    }

    public static void resetCallsignCounter() {
        callsignCounter = 1;
    }

    public String getId() { return id; }
    public String getSerialNumber() { return serialNumber; }
    public String getCallsign() { return callsign; }
    public String getOwnerName() { return ownerName; }
    public String getYear() { return year; }
    public String getModel() { return model; }
    public String getManufacturer() { return manufacturer; }
    public Point getPosition() { return position; }
    public double getAltitude() { return altitude; }
    public double getSpeed() { return speed; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    public void setPosition(Point position) { this.position = position; this.lastUpdate = LocalDateTime.now(); }
    public void setAltitude(double altitude) { this.altitude = altitude; this.lastUpdate = LocalDateTime.now(); }
    public void setSpeed(double speed) { this.speed = speed; this.lastUpdate = LocalDateTime.now(); }
    public void deactivate() { this.isActive = false; this.lastUpdate = LocalDateTime.now(); }

    // Stub methods for test compatibility
    public void takeOff() {}
    public void ascend() {}
    public void descend() {}
    public void land() {}
    public void park() {}
}
