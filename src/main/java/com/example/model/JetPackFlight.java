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
    private final String jetpackId;
    private double x;
    private double y;
    private boolean active;

    public JetPackFlight(String jetpackId, double x, double y) {
        this.jetpackId = jetpackId;
        this.x = x;
        this.y = y;
        this.active = true;
    }

    public String getJetpackId() {
        return jetpackId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("JetPackFlight[id=%s, x=%.2f, y=%.2f, active=%s]", jetpackId, x, y, active);
    }
}
