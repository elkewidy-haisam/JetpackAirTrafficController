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
    private final String accidentID;
    private final int x;
    private final int y;
    private final String type;
    private final String severity;
    private final String description;
    private final long timestamp;
    private boolean isActive;

    public Accident(String accidentID, int x, int y, String type, String severity, String description) {
        this.accidentID = accidentID;
        this.x = x;
        this.y = y;
        this.type = type;
        this.severity = severity;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
        this.isActive = true;
    }

    public String getAccidentID() { return accidentID; }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getType() { return type; }
    public String getSeverity() { return severity; }
    public String getDescription() { return description; }
    public long getTimestamp() { return timestamp; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return String.format("Accident[ID=%s, Type=%s, Severity=%s, Location=(%d,%d), Active=%s]", accidentID, type, severity, x, y, isActive);
    }
}
