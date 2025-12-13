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
