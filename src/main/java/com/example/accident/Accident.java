/**
 * Represents an accident incident in the airspace with location, classification, and status tracking.
 * 
 * Purpose:
 * Models an aviation accident or hazardous event that occurs within the city airspace, capturing
 * critical details such as location, type, severity, and active status. This is used by the air
 * traffic control system to alert pilots, reroute flights, and log incident information.
 * 
 * Key Responsibilities:
 * - Store immutable accident identification and location data
 * - Classify accidents by type (COLLISION, GROUND_ACCIDENT, JETPACK_MALFUNCTION, etc.)
 * - Track severity levels (MINOR, MODERATE, SEVERE, CRITICAL)
 * - Maintain active status to indicate if the incident is ongoing or resolved
 * - Provide timestamp for incident tracking and historical analysis
 * 
 * Interactions:
 * - Created by AccidentReporter when incidents occur
 * - Referenced by Radio to broadcast accident alerts
 * - Used by AccidentAlert to determine avoidance zones
 * - Logged by CityLogManager for incident records
 * - Displayed in UI components (RadioInstructionsPanel, JetpackMovementPanel)
 * 
 * Patterns & Constraints:
 * - Immutable core properties (ID, location, type, severity, description, timestamp)
 * - Single mutable field (isActive) for lifecycle management
 * - Value object with comprehensive toString() for logging and debugging
 * - Thread-safe for reads; writes to isActive should be synchronized at caller level
 * 
 * @author Haisam Elkewidy
 */

package com.example.accident;

public class Accident {
    /** Unique identifier for this accident incident (e.g., "ACC-NYC-1234567890") */
    private final String accidentID;
    
    /** X-coordinate location of the accident on the city map */
    private final int x;
    
    /** Y-coordinate location of the accident on the city map */
    private final int y;
    
    /** Type/classification of accident (e.g., "COLLISION", "GROUND_ACCIDENT", "JETPACK_MALFUNCTION") */
    private final String type;
    
    /** Severity level of the accident (e.g., "MINOR", "MODERATE", "SEVERE", "CRITICAL") */
    private final String severity;
    
    /** Detailed description of what happened in the accident */
    private final String description;
    
    /** Unix timestamp (milliseconds) when the accident was created/reported */
    private final long timestamp;
    
    /** Whether this accident is currently active (true) or has been resolved (false) */
    private boolean isActive;

    /**
     * Constructs a new Accident with all required details.
     * Sets timestamp to current system time and marks accident as active.
     * 
     * @param accidentID unique identifier for this accident
     * @param x the x-coordinate location on the map
     * @param y the y-coordinate location on the map
     * @param type the classification/type of accident
     * @param severity the severity level of the accident
     * @param description detailed description of the accident
     */
    public Accident(String accidentID, int x, int y, String type, String severity, String description) {
        this.accidentID = accidentID;                      // Store the unique accident ID
        this.x = x;                                        // Store the x-coordinate
        this.y = y;                                        // Store the y-coordinate
        this.type = type;                                  // Store the accident type
        this.severity = severity;                          // Store the severity level
        this.description = description;                    // Store the description
        this.timestamp = System.currentTimeMillis();       // Record current time as accident time
        this.isActive = true;                              // Mark accident as currently active
    }

    /**
     * Returns the unique identifier for this accident.
     * @return the accident ID string
     */
    public String getAccidentID() { 
        return accidentID;  // Return the stored accident ID
    }
    
    /**
     * Returns the x-coordinate of this accident's location.
     * @return the x-coordinate value
     */
    public int getX() { 
        return x;  // Return the stored x-coordinate
    }
    
    /**
     * Returns the y-coordinate of this accident's location.
     * @return the y-coordinate value
     */
    public int getY() { 
        return y;  // Return the stored y-coordinate
    }
    
    /**
     * Returns the type/classification of this accident.
     * @return the accident type string
     */
    public String getType() { 
        return type;  // Return the stored accident type
    }
    
    /**
     * Returns the severity level of this accident.
     * @return the severity level string
     */
    public String getSeverity() { 
        return severity;  // Return the stored severity level
    }
    
    /**
     * Returns the detailed description of this accident.
     * @return the description text
     */
    public String getDescription() { 
        return description;  // Return the stored description
    }
    
    /**
     * Returns the timestamp when this accident was reported.
     * @return the Unix timestamp in milliseconds
     */
    public long getTimestamp() { 
        return timestamp;  // Return the stored timestamp
    }
    
    /**
     * Checks if this accident is currently active.
     * @return true if active, false if resolved
     */
    public boolean isActive() { 
        return isActive;  // Return the current active status
    }
    
    /**
     * Sets the active status of this accident.
     * @param active true to mark as active, false to mark as resolved
     */
    public void setActive(boolean active) { 
        isActive = active;  // Update the active status flag
    }

    /**
     * Returns a formatted string representation of this accident.
     * Includes all key details in a human-readable format.
     * 
     * @return formatted string with accident details
     */
    @Override
    public String toString() {
        // Format and return all accident details in a structured string
        return String.format("Accident[ID=%s, Type=%s, Severity=%s, Location=(%d,%d), Active=%s]", 
                           accidentID, type, severity, x, y, isActive);
    }
}
