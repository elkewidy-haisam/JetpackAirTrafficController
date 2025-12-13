/**
 * Simplified model representing a jetpack's current flight position and active status.
 * 
 * Purpose:
 * Lightweight data model tracking a jetpack's real-time position (x, y coordinates) and operational
 * status (active/inactive). Used as a simpler alternative or companion to the full JetPackFlight
 * animation class in AirTrafficControllerFrame. Provides basic position updates without the complexity
 * of full flight simulation, movement, and parking behavior.
 * 
 * Key Responsibilities:
 * - Store jetpack unique identifier
 * - Track current 2D position coordinates
 * - Maintain active/inactive operational status
 * - Provide position update methods
 * - Support simple flight state queries
 * 
 * Interactions:
 * - May be used by simplified UI components for position display
 * - Could serve as data transfer object for flight information
 * - Provides basic flight data without animation logic
 * - Alternative to more complex inner JetPackFlight class in AirTrafficControllerFrame
 * 
 * Patterns & Constraints:
 * - Simple data model (POJO) with minimal behavior
 * - Immutable jetpack ID
 * - Mutable position and active status
 * - No animation, movement, or parking logic
 * - Thread-safety not guaranteed - external synchronization required
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class JetPackFlight {
    /** Unique identifier for this jetpack (immutable once set) */
    private final String jetpackId;
    
    /** Current x-coordinate position on the map */
    private double x;
    
    /** Current y-coordinate position on the map */
    private double y;
    
    /** Whether this jetpack flight is currently active (true) or inactive (false) */
    private boolean active;

    /**
     * Constructs a new JetPackFlight at specified position.
     * Flight is initially marked as active.
     * 
     * @param jetpackId unique identifier for the jetpack
     * @param x initial x-coordinate position
     * @param y initial y-coordinate position
     */
    public JetPackFlight(String jetpackId, double x, double y) {
        this.jetpackId = jetpackId;  // Store immutable jetpack ID
        this.x = x;                  // Set initial x position
        this.y = y;                  // Set initial y position
        this.active = true;          // Mark as active by default
    }

    /**
     * Returns the unique jetpack identifier.
     * @return the jetpack ID string
     */
    public String getJetpackId() {
        return jetpackId;  // Return the ID
    }

    /**
     * Returns the current x-coordinate position.
     * @return the x position value
     */
    public double getX() {
        return x;  // Return current x
    }

    /**
     * Returns the current y-coordinate position.
     * @return the y position value
     */
    public double getY() {
        return y;  // Return current y
    }

    /**
     * Checks if this flight is currently active.
     * @return true if active, false if inactive
     */
    public boolean isActive() {
        return active;  // Return active status
    }

    /**
     * Updates the jetpack's position to new coordinates.
     * 
     * @param x new x-coordinate position
     * @param y new y-coordinate position
     */
    public void setPosition(double x, double y) {
        this.x = x;  // Update x coordinate
        this.y = y;  // Update y coordinate
    }

    /**
     * Sets the active status of this flight.
     * 
     * @param active true to mark as active, false for inactive
     */
    public void setActive(boolean active) {
        this.active = active;  // Update active flag
    }

    /**
     * Returns a formatted string representation of this flight.
     * Includes jetpack ID, position, and active status.
     * 
     * @return formatted string with flight details
     */
    @Override
    public String toString() {
        // Format and return flight information
        return String.format("JetPackFlight[id=%s, x=%.2f, y=%.2f, active=%s]", jetpackId, x, y, active);
    }
}
