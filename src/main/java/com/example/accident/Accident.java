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

public class Accident {
    // Unique identifier for this accident report (e.g., "ACC-001")
    private final String accidentID;
    // X-coordinate of accident location in city grid
    private final int x;
    // Y-coordinate of accident location in city grid
    private final int y;
    // Type classification of accident (e.g., "Collision", "Crash", "Emergency Landing")
    private final String type;
    // Severity level (e.g., "Minor", "Moderate", "Critical")
    private final String severity;
    // Detailed description of accident circumstances
    private final String description;
    // Unix timestamp (milliseconds) when accident was reported
    private final long timestamp;
    // Whether accident is still active (unresolved) or cleared
    private boolean isActive;

    /**
     * Constructs a new Accident report with specified details.
     * 
     * @param accidentID Unique identifier for this accident
     * @param x X-coordinate of accident location
     * @param y Y-coordinate of accident location
     * @param type Type of accident
     * @param severity Severity level
     * @param description Detailed description
     */
    public Accident(String accidentID, int x, int y, String type, String severity, String description) {
        // Store unique identifier for this accident report
        this.accidentID = accidentID;
        // Store X-coordinate of accident location
        this.x = x;
        // Store Y-coordinate of accident location
        this.y = y;
        // Store accident type classification for filtering/reporting
        this.type = type;
        // Store severity level for priority assessment
        this.severity = severity;
        // Store detailed description for investigation records
        this.description = description;
        // Record current system time as accident timestamp
        this.timestamp = System.currentTimeMillis();
        // Mark accident as active by default (requires clearance)
        this.isActive = true;
    }

    // Returns unique identifier of this accident
    public String getAccidentID() { return accidentID; }
    // Returns X-coordinate of accident location
    public int getX() { return x; }
    // Returns Y-coordinate of accident location
    public int getY() { return y; }
    // Returns type classification of accident
    public String getType() { return type; }
    // Returns severity level
    public String getSeverity() { return severity; }
    // Returns detailed description
    public String getDescription() { return description; }
    // Returns Unix timestamp when accident was reported
    public long getTimestamp() { return timestamp; }
    // Returns whether accident is still active
    public boolean isActive() { return isActive; }
    // Updates active status (used to clear accidents)
    public void setActive(boolean active) { isActive = active; }

    /**
     * Returns formatted string representation of this accident.
     * 
     * @return String containing ID, type, severity, location, and status
     */
    @Override
    public String toString() {
        // Format accident details as readable string for logging/display
        return String.format("Accident[ID=%s, Type=%s, Severity=%s, Location=(%d,%d), Active=%s]", accidentID, type, severity, x, y, isActive);
    }
}
