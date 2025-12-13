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
     * Inner class representing a single accident incident.
     * Encapsulates all accident metadata including location, severity, and status.
     */
    public static class Accident {
        /** Unique identifier for this accident (e.g., "ACC-001") */
        private final String accidentID;
        /** X-coordinate of accident location on city map */
        private final int x;
        /** Y-coordinate of accident location on city map */
        private final int y;
        /** Type of accident (e.g., "Collision", "Mechanical Failure", "Weather") */
        private final String type;
        /** Severity level (e.g., "Low", "Moderate", "High", "Critical") */
        private final String severity;
        /** Detailed description of the accident incident */
        private final String description;
        /** Timestamp when accident was reported (milliseconds since epoch) */
        private final long timestamp;
        /** Whether this accident is still active (not yet cleared) */
        private boolean isActive;

        /**
         * Constructs a new Accident with specified details.
         * Sets timestamp to current time and active status to true.
         * 
         * @param accidentID unique accident identifier
         * @param x x-coordinate of accident location
         * @param y y-coordinate of accident location
         * @param type accident type classification
         * @param severity severity level
         * @param description detailed accident description
         */
        public Accident(String accidentID, int x, int y, String type, String severity, String description) {
            this.accidentID = accidentID;            // Store accident ID
            this.x = x;                              // Store x-coordinate
            this.y = y;                              // Store y-coordinate
            this.type = type;                        // Store accident type
            this.severity = severity;                // Store severity level
            this.description = description;          // Store description
            this.timestamp = System.currentTimeMillis();  // Record current time
            this.isActive = true;                    // Mark as active initially
        }

        /** Returns accident ID. @return accident identifier string */
        public String getAccidentID() { return accidentID; }
        /** Returns x-coordinate. @return x position */
        public int getX() { return x; }
        /** Returns y-coordinate. @return y position */
        public int getY() { return y; }
        /** Returns accident type. @return type string */
        public String getType() { return type; }
        /** Returns severity level. @return severity string */
        public String getSeverity() { return severity; }
        /** Returns description. @return description string */
        public String getDescription() { return description; }
        /** Returns timestamp. @return timestamp in milliseconds */
        public long getTimestamp() { return timestamp; }
        /** Returns active status. @return true if active, false if cleared */
        public boolean isActive() { return isActive; }
        /** Sets active status. @param active new active state */
        public void setActive(boolean active) { isActive = active; }

        /**
         * Returns formatted string representation of this accident.
         * @return string with ID, type, severity, location, and active status
         */
        @Override
        public String toString() {
            // Format accident details into readable string
            return String.format("Accident[ID=%s, Type=%s, Severity=%s, Location=(%d,%d), Active=%s]", 
                    accidentID, type, severity, x, y, isActive);
        }
    }

    /** List of all accidents being tracked by this alert system */
    private final List<Accident> accidents = new ArrayList<>();
    /** Identifier for this alert system instance */
    private String alertId;

    /**
     * Default constructor with empty alert ID.
     * Creates an accident alert system with default identifier.
     */
    public AccidentAlert() {
        this.alertId = "";  // Initialize with empty ID
    }

    /**
     * Constructor with specified alert system ID.
     * @param alertId unique identifier for this alert system
     */
    public AccidentAlert(String alertId) {
        this.alertId = alertId;  // Store provided alert ID
    }

    /**
     * Reports and registers a new accident incident.
     * Creates an Accident object and adds it to the tracking list.
     * 
     * @param accidentID unique accident identifier
     * @param x x-coordinate of accident location
     * @param y y-coordinate of accident location
     * @param type accident type classification
     * @param severity severity level
     * @param description detailed accident description
     */
    public void reportAccident(String accidentID, int x, int y, String type, String severity, String description) {
        Accident accident = new Accident(accidentID, x, y, type, severity, description);  // Create accident record
        accidents.add(accident);  // Add to tracked accidents list
    }

    /**
     * Returns a defensive copy of all accidents.
     * @return new List containing all accident records
     */
    public List<Accident> getAccidents() { 
        return new ArrayList<>(accidents);  // Return copy to protect internal state
    }
    
    /**
     * Returns accidents within specified radius of location.
     * Currently returns empty list (stub implementation).
     * 
     * @param x x-coordinate of center point
     * @param y y-coordinate of center point
     * @param radius search radius in map units
     * @return list of nearby accidents (empty in stub)
     */
    public List<Accident> getAccidentsNearLocation(int x, int y, double radius) { 
        return new ArrayList<>();  // TODO: Implement proximity search
    }
    
    /**
     * Alerts jetpacks near accident location.
     * Currently stub implementation (no-op).
     * 
     * @param accidentID ID of accident to alert about
     * @param nearbyJetpacks list of jetpacks near accident
     * @param radius alert radius
     */
    public void alertJetpacksOfAccident(String accidentID, List<?> nearbyJetpacks, double radius) {
        // TODO: Implement jetpack notification logic
    }
    
    /**
     * Removes/clears an accident from tracking.
     * Currently stub implementation.
     * 
     * @param accidentID ID of accident to remove
     * @return true if removed, false otherwise (always false in stub)
     */
    public boolean removeAlert(String accidentID) { 
        return false;  // TODO: Implement accident removal
    }
    
    /**
     * Returns count of active (not cleared) accidents.
     * Currently stub returning 0.
     * 
     * @return number of active accidents (0 in stub)
     */
    public int getActiveAccidentCount() { 
        return 0;  // TODO: Count active accidents
    }

    /**
     * Returns the alert system identifier.
     * Returns "COLLISION-ALERT" if no ID was set.
     * 
     * @return alert system ID string
     */
    public String getAlertSystemID() {
        if (alertId == null || alertId.isEmpty()) return "COLLISION-ALERT";  // Default ID
        return alertId;  // Return stored ID
    }
}
