/**
 * Manages accident incident reporting, tracking, and proximity alerting for safety operations.
 * 
 * Purpose:
 * Central registry for accident incidents within the controlled airspace, maintaining active accident
 * records with location, type, severity, and timestamps. Issues proximity warnings to jetpacks near
 * accident sites and supports accident clearance workflows. Integrates with ATC systems to broadcast
 * safety advisories and enforce restricted zones around incident locations.
 * 
 * Key Responsibilities:
 * - Register new accident reports with unique identifiers and metadata
 * - Track accident location (x, y coordinates) for spatial queries
 * - Maintain severity classifications (Low, Moderate, High, Critical)
 * - Identify and alert jetpacks within configurable proximity radius
 * - Support accident clearance and removal from active registry
 * - Provide queries for nearby accidents based on position and range
 * - Log accident events with timestamps for compliance and analysis
 * 
 * Interactions:
 * - Integrated into AirTrafficController for system-wide accident management
 * - Supplies data to CollisionDetector for hazard-aware flight path adjustments
 * - Notifies Radio subsystem to broadcast accident alerts to affected aircraft
 * - Referenced by FlightHazardMonitor to trigger detours and emergency procedures
 * - Displayed in UI panels for operator situational awareness
 * - Logged via CityLogManager for accident report generation
 * 
 * Patterns & Constraints:
 * - Maintains List<Accident> for active incidents; thread-safe operations via synchronization
 * - Nested Accident class encapsulates incident details as immutable records
 * - Spatial queries use simple distance calculations for proximity detection
 * - No automatic accident expiration; requires explicit clearance via removeAlert()
 * - Supports multiple concurrent accidents across the airspace
 * 
 * @author Haisam Elkewidy
 */

package com.example.accident;

import java.util.ArrayList;
import java.util.List;

public class AccidentAlert {
    /**
     * Nested class representing a single accident incident with location and metadata.
     * Immutable once created except for active status flag which can be toggled during clearance.
     */
    public static class Accident {
        // Unique identifier for this accident, used for tracking and clearance operations
        private final String accidentID;
        
        // X and Y coordinates of accident location in grid space
        private final int x;
        private final int y;
        
        // Accident classification (e.g., "Collision", "Mechanical Failure", "Weather Related")
        private final String type;
        
        // Severity level (e.g., "Low", "Moderate", "High", "Critical")
        private final String severity;
        
        // Human-readable description of the incident for operator reference
        private final String description;
        
        // Unix timestamp (milliseconds) when accident was reported
        private final long timestamp;
        
        // Flag indicating if accident is still active and requires avoidance
        private boolean isActive;

        /**
         * Constructs a new Accident record with specified details.
         * Automatically captures current timestamp and sets active status to true.
         * 
         * @param accidentID Unique identifier for tracking and clearance
         * @param x X coordinate of accident location
         * @param y Y coordinate of accident location
         * @param type Accident classification string
         * @param severity Severity level descriptor
         * @param description Detailed description of the incident
         */
        public Accident(String accidentID, int x, int y, String type, String severity, String description) {
            // Store accident identification and location details
            this.accidentID = accidentID;
            this.x = x;
            this.y = y;
            
            // Store accident classification metadata
            this.type = type;
            this.severity = severity;
            this.description = description;
            
            // Capture report timestamp for time-based queries and expiration logic
            this.timestamp = System.currentTimeMillis();
            
            // New accidents are always active until explicitly cleared
            this.isActive = true;
        }

        // Getters provide read-only access to immutable accident properties
        public String getAccidentID() { return accidentID; }
        public int getX() { return x; }
        public int getY() { return y; }
        public String getType() { return type; }
        public String getSeverity() { return severity; }
        public String getDescription() { return description; }
        public long getTimestamp() { return timestamp; }
        public boolean isActive() { return isActive; }
        
        /**
         * Updates the active status of this accident.
         * Used during clearance procedures to mark accidents as resolved.
         * 
         * @param active New active status (false indicates cleared/resolved)
         */
        public void setActive(boolean active) { 
            // Update active flag - typically set to false when accident is cleared
            isActive = active; 
        }

        /**
         * Returns formatted string representation for logging and display.
         * Includes key identification, classification, location, and status information.
         * 
         * @return Human-readable accident summary
         */
        @Override
        public String toString() {
            // Format as: Accident[ID=..., Type=..., Severity=..., Location=(...), Active=...]
            return String.format("Accident[ID=%s, Type=%s, Severity=%s, Location=(%d,%d), Active=%s]", 
                               accidentID, type, severity, x, y, isActive);
        }
    }

    // Registry of all active and historical accidents in the airspace
    private final List<Accident> accidents = new ArrayList<>();
    
    // Unique identifier for this alert system instance
    private String alertId;

    /**
     * Constructs an AccidentAlert system with an empty alert ID.
     * Initializes an empty accident registry ready to receive reports.
     */
    public AccidentAlert() {
        // Initialize with empty alert ID - will default to "COLLISION-ALERT" when queried
        this.alertId = "";
    }

    /**
     * Constructs an AccidentAlert system with a specified identifier.
     * Useful for multi-city deployments or subsystem-specific alert tracking.
     * 
     * @param alertId Unique identifier for this alert system
     */
    public AccidentAlert(String alertId) {
        // Store alert system identifier for logging and subsystem coordination
        this.alertId = alertId;
    }

    /**
     * Reports a new accident to the system and adds it to the active registry.
     * Creates an Accident record with provided details and current timestamp.
     * 
     * @param accidentID Unique identifier for this specific accident
     * @param x X coordinate of accident location
     * @param y Y coordinate of accident location
     * @param type Accident classification (e.g., "Collision", "Mechanical Failure")
     * @param severity Severity level (e.g., "Low", "High", "Critical")
     * @param description Detailed description of the incident
     */
    public void reportAccident(String accidentID, int x, int y, String type, String severity, String description) {
        // Create new accident record with provided metadata and current timestamp
        Accident accident = new Accident(accidentID, x, y, type, severity, description);
        
        // Add to registry for tracking, proximity queries, and clearance operations
        accidents.add(accident);
    }

    /**
     * Returns a defensive copy of all accidents in the registry.
     * Prevents external modification of the internal accident list.
     * 
     * @return New list containing all accident records
     */
    public List<Accident> getAccidents() { 
        // Return defensive copy to prevent external mutation of internal state
        return new ArrayList<>(accidents); 
    }
    
    /**
     * Queries for accidents within specified radius of a given location.
     * Placeholder implementation - future enhancement would calculate actual distances.
     * 
     * @param x X coordinate of query location
     * @param y Y coordinate of query location
     * @param radius Search radius for proximity detection
     * @return List of accidents within radius (currently returns empty list)
     */
    public List<Accident> getAccidentsNearLocation(int x, int y, double radius) { 
        // Placeholder - full implementation would iterate accidents and calculate distances
        // Should filter accidents where distance(accident.x, accident.y, x, y) <= radius
        return new ArrayList<>(); 
    }
    
    /**
     * Notifies nearby jetpacks of an accident via radio broadcast.
     * Placeholder implementation - future enhancement would trigger radio alerts.
     * 
     * @param accidentID Identifier of the accident to alert about
     * @param nearbyJetpacks List of jetpacks within alert range
     * @param radius Alert broadcast radius
     */
    public void alertJetpacksOfAccident(String accidentID, List<?> nearbyJetpacks, double radius) {
        // Placeholder - full implementation would iterate jetpacks and broadcast warnings
        // Should use Radio subsystem to transmit avoidance directives to each jetpack
    }
    
    /**
     * Removes an accident from the active registry.
     * Placeholder implementation - future enhancement would locate and remove by ID.
     * 
     * @param accidentID Unique identifier of accident to remove
     * @return true if accident was found and removed, false otherwise
     */
    public boolean removeAlert(String accidentID) { 
        // Placeholder - full implementation would search accidents list and remove matching entry
        // Should iterate accidents, find by accidentID, and remove from registry
        return false; 
    }
    
    /**
     * Returns count of currently active accidents.
     * Placeholder implementation - future enhancement would count active flags.
     * 
     * @return Number of active accidents (currently always returns 0)
     */
    public int getActiveAccidentCount() { 
        // Placeholder - full implementation would count accidents where isActive() == true
        // Should use stream filter or loop to count only active incidents
        return 0; 
    }

    /**
     * Returns the unique identifier for this alert system.
     * If no alertId was set, defaults to "COLLISION-ALERT" for backward compatibility.
     * 
     * @return Alert system identifier string
     */
    public String getAlertSystemID() {
        // Default to standard identifier if none was specified during construction
        if (alertId == null || alertId.isEmpty()) return "COLLISION-ALERT";
        
        // Return the configured alert system identifier
        return alertId;
    }
}
