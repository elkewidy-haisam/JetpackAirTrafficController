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
    // Immutable identification fields - cannot change after construction
    
    // Unique identifier for this jetpack instance
    private final String id;
    
    // Serial number with city prefix (e.g., "BOS-001", "NY-042")
    private final String serialNumber;
    
    // Radio callsign for air traffic communications (e.g., "BOS-JP1")
    private final String callsign;
    
    // Registered owner/pilot name
    private final String ownerName;
    
    // Manufacturing year
    private final String year;
    
    // Model designation (e.g., "ModelX", "Turbo-3000")
    private final String model;
    
    // Manufacturer name (e.g., "JetPackCorp", "AeroTech")
    private final String manufacturer;
    
    // Mutable operational state fields - updated during flight operations
    
    // Current 2D position on city map (x, y coordinates)
    private Point position;
    
    // Current altitude in abstract units (typically meters or feet)
    private double altitude;
    
    // Current speed in abstract units (typically mph or m/s)
    private double speed;
    
    // Active status flag - false indicates grounded/deactivated jetpack
    private boolean isActive;
    
    // Timestamp of last state update for staleness detection
    private LocalDateTime lastUpdate;

    // Static counters for generating unique IDs and callsigns across all instances
    // These should be managed carefully in multi-threaded environments
    private static int jetPackCounter = 1;
    private static int callsignCounter = 1;

    /**
     * Full constructor for creating a JetPack with complete specifications.
     * Performs serial number validation and normalization to ensure consistent formatting.
     * Initializes operational state with active status and current timestamp.
     * 
     * @param id Unique identifier for this jetpack
     * @param serialNumber Serial number (will be normalized to city-prefix format)
     * @param callsign Radio callsign for ATC communications
     * @param ownerName Registered owner/pilot name
     * @param year Manufacturing year
     * @param model Model designation
     * @param manufacturer Manufacturer name
     * @param position Initial position on map
     * @param altitude Initial altitude
     * @param speed Initial speed
     */
    public JetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer, Point position, double altitude, double speed) {
        // Store unique identifier
        this.id = id;
        
        // Normalize serial number to standardized format with city prefix
        if (serialNumber != null && (serialNumber.startsWith("BOS-") || serialNumber.startsWith("TEST-"))) {
            // Serial already has valid prefix - use as-is
            this.serialNumber = serialNumber;
        } else if (serialNumber != null) {
            // Extract numeric portion and reformat with BOS prefix and zero-padding
            // Example: "123" becomes "BOS-123", "42" becomes "BOS-042"
            String digits = serialNumber.replaceAll("\\D", "");
            this.serialNumber = "BOS-" + String.format("%03d", Integer.parseInt(digits));
        } else {
            // Handle null serial number
            this.serialNumber = serialNumber;
        }
        
        // Store immutable identity and specification fields
        this.callsign = callsign;
        this.ownerName = ownerName;
        this.year = year;
        this.model = model;
        this.manufacturer = manufacturer;
        
        // Initialize mutable operational state
        this.position = position;
        this.altitude = altitude;
        this.speed = speed;
        
        // New jetpacks start in active state
        this.isActive = true;
        
        // Record creation timestamp for tracking purposes
        this.lastUpdate = LocalDateTime.now();
    }

    /**
     * Simplified constructor for basic jetpack creation with minimal parameters.
     * Delegates to full constructor with default values for optional fields.
     * Initializes position at origin (0,0) with zero altitude and speed.
     * 
     * @param id Unique identifier
     * @param serialNumber Serial number
     * @param callsign Radio callsign
     * @param ownerName Owner/pilot name
     * @param year Manufacturing year
     * @param model Model designation
     */
    public JetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model) {
        // Delegate to full constructor with defaults: Unknown manufacturer, origin position, zero altitude/speed
        this(id, serialNumber, callsign, ownerName, year, model, "Unknown", new Point(0, 0), 0.0, 0.0);
    }

    /**
     * Factory method for creating city-specific jetpacks with standardized naming.
     * Used primarily in testing and bulk jetpack generation for specific cities.
     * Generates consistent callsigns and serial numbers based on city prefix.
     * 
     * @param cityPrefix City code prefix (e.g., "NY", "BOS", "DAL")
     * @param number Sequential number for this jetpack
     * @return New JetPack instance configured for the specified city
     */
    public static JetPack createForCity(String cityPrefix, int number) {
        // Generate serial number: CITY-NNN (e.g., "NY-001", "BOS-042")
        String serial = cityPrefix + "-" + String.format("%03d", number);
        
        // Generate callsign: CITY-JPNNN (e.g., "NY-JP1", "BOS-JP42")
        String callsign = cityPrefix + "-JP" + number;
        
        // Create jetpack with standardized test/simulation values
        return new JetPack("JP" + number, serial, callsign, "TestOwner", "2025", "ModelX", "JetPackCorp", new Point(0,0), 0.0, 0.0);
    }

    /**
     * Resets the static callsign counter to 1.
     * Used primarily in testing to ensure consistent callsign generation
     * across test runs and to avoid counter overflow in long-running simulations.
     */
    public static void resetCallsignCounter() {
        // Reset to 1 (not 0) since callsigns typically start from 1
        callsignCounter = 1;
    }

    // Getter methods for immutable identity fields - no setters provided
    
    /** Returns the unique identifier for this jetpack */
    public String getId() { return id; }
    
    /** Returns the serial number (with city prefix) */
    public String getSerialNumber() { return serialNumber; }
    
    /** Returns the radio callsign used for ATC communications */
    public String getCallsign() { return callsign; }
    
    /** Returns the registered owner/pilot name */
    public String getOwnerName() { return ownerName; }
    
    /** Returns the manufacturing year */
    public String getYear() { return year; }
    
    /** Returns the model designation */
    public String getModel() { return model; }
    
    /** Returns the manufacturer name */
    public String getManufacturer() { return manufacturer; }
    
    // Getter methods for mutable operational state
    
    /** Returns the current 2D position on the city map */
    public Point getPosition() { return position; }
    
    /** Returns the current altitude */
    public double getAltitude() { return altitude; }
    
    /** Returns the current speed */
    public double getSpeed() { return speed; }
    
    /** Returns true if jetpack is active and operational */
    public boolean isActive() { return isActive; }
    
    /** Returns the timestamp of the last state update */
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    /**
     * Updates the jetpack's position on the city map.
     * Automatically updates the lastUpdate timestamp to track state freshness.
     * 
     * @param position New position coordinates
     */
    public void setPosition(Point position) { 
        this.position = position; 
        // Update timestamp to track when position was last modified
        this.lastUpdate = LocalDateTime.now(); 
    }
    
    /**
     * Updates the jetpack's altitude.
     * Automatically updates the lastUpdate timestamp.
     * 
     * @param altitude New altitude value
     */
    public void setAltitude(double altitude) { 
        this.altitude = altitude; 
        // Update timestamp to track altitude change
        this.lastUpdate = LocalDateTime.now(); 
    }
    
    /**
     * Updates the jetpack's speed.
     * Automatically updates the lastUpdate timestamp.
     * 
     * @param speed New speed value
     */
    public void setSpeed(double speed) { 
        this.speed = speed; 
        // Update timestamp to track speed change
        this.lastUpdate = LocalDateTime.now(); 
    }
    
    /**
     * Deactivates the jetpack, marking it as grounded or out of service.
     * Once deactivated, the jetpack should not be assigned new flight paths.
     * Updates timestamp to record when deactivation occurred.
     */
    public void deactivate() { 
        // Set active flag to false
        this.isActive = false; 
        // Record deactivation timestamp
        this.lastUpdate = LocalDateTime.now(); 
    }

    // Stub methods for flight operations - placeholders for future implementation
    // These provide interface compatibility for testing without full implementation
    
    /** Initiates takeoff sequence - stub for future implementation */
    public void takeOff() {}
    
    /** Increases altitude - stub for future implementation */
    public void ascend() {}
    
    /** Decreases altitude - stub for future implementation */
    public void descend() {}
    
    /** Initiates landing sequence - stub for future implementation */
    public void land() {}
    
    /** Parks jetpack at designated parking space - stub for future implementation */
    public void park() {}
}
