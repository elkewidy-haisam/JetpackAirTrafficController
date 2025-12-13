/**
 * JetPackFlight component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides jetpackflight functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core jetpackflight operations
 * - Maintain necessary state for jetpackflight functionality
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

package com.example.model;

public class JetPackFlight {
    // Unique identifier of jetpack associated with this flight
    private final String jetpackId;
    // Current X-coordinate of flight position in city grid
    private double x;
    // Current Y-coordinate of flight position in city grid
    private double y;
    // Whether this flight is currently active (true) or completed/terminated (false)
    private boolean active;

    /**
     * Constructs a new JetPackFlight at specified position.
     * Flight is initialized as active.
     * 
     * @param jetpackId Unique identifier of jetpack
     * @param x Initial X-coordinate position
     * @param y Initial Y-coordinate position
     */
    public JetPackFlight(String jetpackId, double x, double y) {
        // Store jetpack identifier for tracking this flight
        this.jetpackId = jetpackId;
        // Set initial X-coordinate position
        this.x = x;
        // Set initial Y-coordinate position
        this.y = y;
        // Mark flight as active by default
        this.active = true;
    }

    /**
     * Returns the jetpack identifier for this flight.
     * 
     * @return Jetpack ID string
     */
    public String getJetpackId() {
        // Return stored jetpack identifier
        return jetpackId;
    }

    /**
     * Returns the current X-coordinate of flight position.
     * 
     * @return X-coordinate in city grid
     */
    public double getX() {
        // Return current X-coordinate
        return x;
    }

    /**
     * Returns the current Y-coordinate of flight position.
     * 
     * @return Y-coordinate in city grid
     */
    public double getY() {
        // Return current Y-coordinate
        return y;
    }

    /**
     * Returns whether this flight is currently active.
     * 
     * @return true if flight is active, false if completed/terminated
     */
    public boolean isActive() {
        // Return current active status
        return active;
    }

    /**
     * Updates the position of this flight.
     * 
     * @param x New X-coordinate
     * @param y New Y-coordinate
     */
    public void setPosition(double x, double y) {
        // Update X-coordinate to new value
        this.x = x;
        // Update Y-coordinate to new value
        this.y = y;
    }

    /**
     * Sets the active status of this flight.
     * 
     * @param active true to mark as active, false to mark as completed
     */
    public void setActive(boolean active) {
        // Update active status to new value
        this.active = active;
    }

    /**
     * Returns formatted string representation of this flight.
     * 
     * @return String with jetpack ID, position, and active status
     */
    @Override
    public String toString() {
        // Format flight details as readable string with 2 decimal places for coordinates
        return String.format("JetPackFlight[id=%s, x=%.2f, y=%.2f, active=%s]", jetpackId, x, y, active);
    }
}
