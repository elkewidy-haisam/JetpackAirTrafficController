/**
 * Primary jetpack model with complete identification, ownership, and operational data.
 * 
 * Purpose:
 * Comprehensive jetpack entity containing all identification (serial, callsign), ownership (pilot name),
 * specifications (year, model, manufacturer), and operational data (position, heading, altitude). This is
 * the main jetpack class used throughout the application for flight tracking and management.
 * 
 * Key Responsibilities:
 * - Store complete jetpack identification and specifications
 * - Track real-time position, heading, and altitude
 * - Provide accessor methods for all properties
 * - Support flight operations and parking
 * 
 * @author Haisam Elkewidy
 */

package com.example.jetpack;

import java.awt.Point;
import java.time.LocalDateTime;

public class JetPack {
    /** Unique identifier for this jetpack */
    private final String id;
    
    /** Manufacturer serial number (formatted as city-prefix-number) */
    private final String serialNumber;
    
    /** Radio callsign used for ATC communications */
    private final String callsign;
    
    /** Name of the jetpack owner/pilot */
    private final String ownerName;
    
    /** Manufacturing year */
    private final String year;
    
    /** Model designation */
    private final String model;
    
    /** Manufacturer company name */
    private final String manufacturer;
    
    /** Current position on the map */
    private Point position;
    
    /** Current altitude in appropriate units */
    private double altitude;
    
    /** Current speed */
    private double speed;
    
    /** Whether jetpack is currently active/operational */
    private boolean isActive;
    
    /** Timestamp of last update to this jetpack's data */
    private LocalDateTime lastUpdate;

    /** Global counter for generating jetpack IDs */
    private static int jetPackCounter = 1;
    
    /** Global counter for generating unique callsigns */
    private static int callsignCounter = 1;

    /**
     * Full constructor for creating a jetpack with all parameters.
     * Serial number is formatted with "BOS-" prefix if not already formatted.
     * 
     * @param id unique jetpack identifier
     * @param serialNumber manufacturer serial (will be formatted if needed)
     * @param callsign radio callsign for ATC
     * @param ownerName pilot/owner name
     * @param year manufacturing year
     * @param model model designation
     * @param manufacturer manufacturer name
     * @param position initial position on map
     * @param altitude initial altitude
     * @param speed initial speed
     */
    public JetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer, Point position, double altitude, double speed) {
        this.id = id;  // Store jetpack ID
        
        // Format serial number with city prefix if needed
        if (serialNumber != null && (serialNumber.startsWith("BOS-") || serialNumber.startsWith("TEST-"))) {
            this.serialNumber = serialNumber;  // Already formatted
        } else if (serialNumber != null) {
            String digits = serialNumber.replaceAll("\\D", "");  // Extract digits
            this.serialNumber = "BOS-" + String.format("%03d", Integer.parseInt(digits));  // Format with prefix
        } else {
            this.serialNumber = serialNumber;  // Use as-is if null
        }
        
        this.callsign = callsign;            // Store callsign
        this.ownerName = ownerName;          // Store owner name
        this.year = year;                    // Store year
        this.model = model;                  // Store model
        this.manufacturer = manufacturer;    // Store manufacturer
        this.position = position;            // Store initial position
        this.altitude = altitude;            // Store initial altitude
        this.speed = speed;                  // Store initial speed
        this.isActive = true;                // Mark as active by default
        this.lastUpdate = LocalDateTime.now(); // Record creation time
    }

    /**
     * Simplified constructor with default operational values.
     * Sets manufacturer to "Unknown" and position/altitude/speed to zero.
     * 
     * @param id unique jetpack identifier
     * @param serialNumber manufacturer serial
     * @param callsign radio callsign
     * @param ownerName pilot/owner name
     * @param year manufacturing year
     * @param model model designation
     */
    public JetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model) {
        // Delegate to full constructor with default values
        this(id, serialNumber, callsign, ownerName, year, model, "Unknown", new Point(0, 0), 0.0, 0.0);
    }

    /**
     * Factory method for creating test jetpacks with city-specific formatting.
     * Used in unit tests for consistent jetpack generation.
     * 
     * @param cityPrefix city code (e.g., "BOS", "NYC")
     * @param number jetpack number for this city
     * @return new JetPack with test configuration
     */
    public static JetPack createForCity(String cityPrefix, int number) {
        String serial = cityPrefix + "-" + String.format("%03d", number);  // Format serial
        String callsign = cityPrefix + "-JP" + number;  // Format callsign
        // Create and return test jetpack with default values
        return new JetPack("JP" + number, serial, callsign, "TestOwner", "2025", "ModelX", "JetPackCorp", new Point(0,0), 0.0, 0.0);
    }

    /**
     * Resets the global callsign counter to 1.
     * Used in tests to ensure consistent callsign generation.
     */
    public static void resetCallsignCounter() {
        callsignCounter = 1;  // Reset to initial value
    }

    /** @return jetpack unique identifier */
    public String getId() { return id; }
    
    /** @return manufacturer serial number */
    public String getSerialNumber() { return serialNumber; }
    
    /** @return radio callsign */
    public String getCallsign() { return callsign; }
    
    /** @return owner/pilot name */
    public String getOwnerName() { return ownerName; }
    
    /** @return manufacturing year */
    public String getYear() { return year; }
    
    /** @return model designation */
    public String getModel() { return model; }
    
    /** @return manufacturer name */
    public String getManufacturer() { return manufacturer; }
    
    /** @return current position */
    public Point getPosition() { return position; }
    
    /** @return current altitude */
    public double getAltitude() { return altitude; }
    
    /** @return current speed */
    public double getSpeed() { return speed; }
    
    /** @return true if active, false if deactivated */
    public boolean isActive() { return isActive; }
    
    /** @return timestamp of last update */
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    /**
     * Updates jetpack position and records update time.
     * @param position new position coordinates
     */
    public void setPosition(Point position) { 
        this.position = position;  // Update position
        this.lastUpdate = LocalDateTime.now();  // Record update time
    }
    
    /**
     * Updates jetpack altitude and records update time.
     * @param altitude new altitude value
     */
    public void setAltitude(double altitude) { 
        this.altitude = altitude;  // Update altitude
        this.lastUpdate = LocalDateTime.now();  // Record update time
    }
    
    /**
     * Updates jetpack speed and records update time.
     * @param speed new speed value
     */
    public void setSpeed(double speed) { 
        this.speed = speed;  // Update speed
        this.lastUpdate = LocalDateTime.now();  // Record update time
    }
    
    /**
     * Marks jetpack as inactive/deactivated and records time.
     */
    public void deactivate() { 
        this.isActive = false;  // Mark as inactive
        this.lastUpdate = LocalDateTime.now();  // Record deactivation time
    }

    /** Stub method for takeoff operations - implementation pending */
    public void takeOff() {}
    
    /** Stub method for ascending - implementation pending */
    public void ascend() {}
    
    /** Stub method for descending - implementation pending */
    public void descend() {}
    
    /** Stub method for landing - implementation pending */
    public void land() {}
    
    /** Stub method for parking - implementation pending */
    public void park() {}
}
