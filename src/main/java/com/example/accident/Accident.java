/**
 * Accident component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides accident functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core accident operations
 * - Maintain necessary state for accident functionality
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

package com.example.accident;

/**
 * Represents a recorded accident incident in the airspace.
 * Tracks accident location, type, severity, and status for safety management.
 */
public class Accident {
    // Unique identifier for this accident incident
    private final String accidentID;
    // X-coordinate of accident location
    private final int x;
    // Y-coordinate of accident location
    private final int y;
    // Type/category of accident (e.g., "COLLISION", "MECHANICAL_FAILURE")
    private final String type;
    // Severity level (e.g., "MINOR", "MAJOR", "CRITICAL")
    private final String severity;
    // Detailed description of the accident
    private final String description;
    // Unix timestamp when accident was recorded
    private final long timestamp;
    // Flag indicating if accident is currently active (not resolved)
    private boolean isActive;

    /**
     * Constructs a new Accident with specified details.
     * Automatically sets timestamp to current time and active status to true.
     *
     * @param accidentID Unique identifier for the accident
     * @param x X-coordinate of accident location
     * @param y Y-coordinate of accident location
     * @param type Type/category of the accident
     * @param severity Severity level of the accident
     * @param description Detailed description of what occurred
     */
    public Accident(String accidentID, int x, int y, String type, String severity, String description) {
        // Assign unique accident identifier
        this.accidentID = accidentID;
        // Set accident X position
        this.x = x;
        // Set accident Y position
        this.y = y;
        // Set accident type/category
        this.type = type;
        // Set severity level
        this.severity = severity;
        // Set detailed description
        this.description = description;
        // Record current time as accident timestamp
        this.timestamp = System.currentTimeMillis();
        // Mark accident as active by default
        this.isActive = true;
    }

    /**
     * Gets the unique accident identifier.
     *
     * @return Accident ID string
     */
    public String getAccidentID() { return accidentID; }
    
    /**
     * Gets the X-coordinate of accident location.
     *
     * @return X position
     */
    public int getX() { return x; }
    
    /**
     * Gets the Y-coordinate of accident location.
     *
     * @return Y position
     */
    public int getY() { return y; }
    
    /**
     * Gets the type/category of the accident.
     *
     * @return Accident type string
     */
    public String getType() { return type; }
    
    /**
     * Gets the severity level of the accident.
     *
     * @return Severity string
     */
    public String getSeverity() { return severity; }
    
    /**
     * Gets the detailed description of the accident.
     *
     * @return Description string
     */
    public String getDescription() { return description; }
    
    /**
     * Gets the timestamp when accident was recorded.
     *
     * @return Unix timestamp in milliseconds
     */
    public long getTimestamp() { return timestamp; }
    
    /**
     * Checks if the accident is currently active.
     *
     * @return true if active, false if resolved
     */
    public boolean isActive() { return isActive; }
    
    /**
     * Sets the active status of the accident.
     *
     * @param active New active status (true = active, false = resolved)
     */
    public void setActive(boolean active) { isActive = active; }

    /**
     * Returns a formatted string representation of the accident.
     * Includes ID, type, severity, location, and active status.
     *
     * @return Formatted accident string
     */
    @Override
    public String toString() {
        // Format as: Accident[ID=X, Type=Y, Severity=Z, Location=(x,y), Active=boolean]
        return String.format("Accident[ID=%s, Type=%s, Severity=%s, Location=(%d,%d), Active=%s]", accidentID, type, severity, x, y, isActive);
    }
}
