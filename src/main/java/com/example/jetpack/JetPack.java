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
    // Unique identifier for this jetpack instance (e.g., "JP001")
    private final String id;
    // Federal registration serial number (e.g., "BOS-001")
    private final String serialNumber;
    // Radio callsign for air traffic communications (e.g., "BOS-JP1")
    private final String callsign;
    // Name of jetpack owner/operator
    private final String ownerName;
    // Manufacturing year of this jetpack model
    private final String year;
    // Model designation (e.g., "ModelX", "Thunderbolt")
    private final String model;
    // Manufacturer company name (e.g., "JetPackCorp")
    private final String manufacturer;
    // Current 2D position in city grid coordinates
    private Point position;
    // Current altitude in feet above ground level
    private double altitude;
    // Current speed in miles per hour
    private double speed;
    // Whether jetpack is currently active/operational
    private boolean isActive;
    // Timestamp of last position/status update
    private LocalDateTime lastUpdate;

    // Global counter for generating jetpack IDs (currently unused)
    private static int jetPackCounter = 1;
    // Global counter for generating callsigns (currently unused)
    private static int callsignCounter = 1;

    /**
     * Full constructor for creating a jetpack with all parameters.
     * 
     * @param id Unique jetpack identifier
     * @param serialNumber Federal registration serial
     * @param callsign Radio callsign
     * @param ownerName Owner's name
     * @param year Manufacturing year
     * @param model Model designation
     * @param manufacturer Manufacturer name
     * @param position Starting position
     * @param altitude Starting altitude
     * @param speed Starting speed
     */
    public JetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer, Point position, double altitude, double speed) {
        // Store unique identifier for this jetpack
        this.id = id;
        // Normalize serial number format to BOS-XXX or keep TEST- prefix
        if (serialNumber != null && (serialNumber.startsWith("BOS-") || serialNumber.startsWith("TEST-"))) {
            // Serial already properly formatted, use as-is
            this.serialNumber = serialNumber;
        } else if (serialNumber != null) {
            // Extract numeric digits from serial number
            String digits = serialNumber.replaceAll("\\D", "");
            // Reformat as BOS-XXX with zero padding
            this.serialNumber = "BOS-" + String.format("%03d", Integer.parseInt(digits));
        } else {
            // No serial provided, store null
            this.serialNumber = serialNumber;
        }
        // Store radio callsign for ATC communications
        this.callsign = callsign;
        // Store owner name for registration records
        this.ownerName = ownerName;
        // Store manufacturing year for compliance tracking
        this.year = year;
        // Store model designation for identification
        this.model = model;
        // Store manufacturer name for maintenance records
        this.manufacturer = manufacturer;
        // Set initial position in city grid
        this.position = position;
        // Set initial altitude above ground
        this.altitude = altitude;
        // Set initial speed
        this.speed = speed;
        // Mark jetpack as active by default
        this.isActive = true;
        // Record current time as last update timestamp
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Simplified constructor with defaults for position/altitude/speed.
     * 
     * @param id Unique jetpack identifier
     * @param serialNumber Federal registration serial
     * @param callsign Radio callsign
     * @param ownerName Owner's name
     * @param year Manufacturing year
     * @param model Model designation
     */
    public JetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model) {
        // Delegate to full constructor with default values
        this(id, serialNumber, callsign, ownerName, year, model, "Unknown", new Point(0, 0), 0.0, 0.0);
    }

    /**
     * Factory method for creating city-specific test jetpacks.
     * Used for testing and simulation with consistent naming patterns.
     * 
     * @param cityPrefix City code (e.g., "BOS", "NYC")
     * @param number Sequential jetpack number
     * @return Configured JetPack instance
     */
    public static JetPack createForCity(String cityPrefix, int number) {
        // Format serial number with city prefix and zero-padded number
        String serial = cityPrefix + "-" + String.format("%03d", number);
        // Format callsign with city prefix and jetpack identifier
        String callsign = cityPrefix + "-JP" + number;
        // Create and return jetpack with standard test parameters
        return new JetPack("JP" + number, serial, callsign, "TestOwner", "2025", "ModelX", "JetPackCorp", new Point(0,0), 0.0, 0.0);
    }

    /**
     * Resets the global callsign counter to 1.
     * Used for test isolation and simulation restarts.
     */
    public static void resetCallsignCounter() {
        // Reset counter to initial value for fresh simulation
        callsignCounter = 1;
    }

    // Returns unique identifier of this jetpack
    public String getId() { return id; }
    // Returns federal registration serial number
    public String getSerialNumber() { return serialNumber; }
    // Returns radio callsign for ATC communications
    public String getCallsign() { return callsign; }
    // Returns owner's name
    public String getOwnerName() { return ownerName; }
    // Returns manufacturing year
    public String getYear() { return year; }
    // Returns model designation
    public String getModel() { return model; }
    // Returns manufacturer name
    public String getManufacturer() { return manufacturer; }
    // Returns current position in city grid
    public Point getPosition() { return position; }
    // Returns current altitude in feet
    public double getAltitude() { return altitude; }
    // Returns current speed in MPH
    public double getSpeed() { return speed; }
    // Returns whether jetpack is active
    public boolean isActive() { return isActive; }
    // Returns timestamp of last update
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    // Updates position and refreshes timestamp
    public void setPosition(Point position) { this.position = position; this.lastUpdate = LocalDateTime.now(); }
    // Updates altitude and refreshes timestamp
    public void setAltitude(double altitude) { this.altitude = altitude; this.lastUpdate = LocalDateTime.now(); }
    // Updates speed and refreshes timestamp
    public void setSpeed(double speed) { this.speed = speed; this.lastUpdate = LocalDateTime.now(); }
    // Deactivates jetpack and refreshes timestamp
    public void deactivate() { this.isActive = false; this.lastUpdate = LocalDateTime.now(); }

    // Stub method for takeoff operation (implementation pending)
    public void takeOff() {}
    // Stub method for ascending (implementation pending)
    public void ascend() {}
    // Stub method for descending (implementation pending)
    public void descend() {}
    // Stub method for landing operation (implementation pending)
    public void land() {}
    // Stub method for parking operation (implementation pending)
    public void park() {}
}
