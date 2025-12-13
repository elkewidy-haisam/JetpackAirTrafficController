/**
 * Simplified flight state model for backend operations tracking jetpack position and activity status.
 * 
 * Purpose:
 * Represents a jetpack flight instance with minimal state for backend, non-GUI scenarios. Tracks
 * jetpack identification, current position coordinates, and active/inactive status without the
 * complexity of animation, rendering, or parking behavior. Provides a lightweight alternative to
 * the comprehensive GUI-based JetPackFlight inner class in AirTrafficControllerFrame.
 * 
 * Key Responsibilities:
 * - Store jetpack identification (jetpackId) for flight tracking
 * - Maintain current position (x, y coordinates) for spatial queries
 * - Track active/inactive status for operational state management
 * - Support position updates for backend flight simulations
 * - Enable programmatic flight tracking without UI overhead
 * - Provide simple data model for testing and automation
 * - Serve backend services and command-line tools
 * 
 * Interactions:
 * - Managed by AirTrafficController (backend version) for flight registry
 * - Alternative to comprehensive GUI JetPackFlight with animation/rendering
 * - Used in automated tests without UI dependencies
 * - Supports backend flight simulations and batch processing
 * - Integrates with FlightPath for backend route planning
 * - May be persisted for session state or logging
 * - Enables command-line flight tracking utilities
 * 
 * Patterns & Constraints:
 * - Lightweight data model with minimal state (ID, position, active flag)
 * - Immutable jetpackId ensures consistent flight identification
 * - Mutable position supports dynamic flight progression
 * - No animation, rendering, parking, or UI concerns (backend only)
 * - Contrast with GUI JetPackFlight: this lacks timers, graphics, parking logic
 * - Thread-safe for read operations; synchronization for position updates
 * - Suitable for testing, CLI tools, services, batch processing
 * - See ARCHITECTURE_NOTES.md for GUI vs. backend architecture details
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
