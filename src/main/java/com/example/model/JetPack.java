/**
 * Represents an individual jetpack vehicle with unique identification and pilot information.
 * 
 * Purpose:
 * Models a jetpack aircraft within the air traffic control system, storing essential identification
 * (callsign), operator details (pilot name), and operational status (availability). Acts as a
 * fundamental data entity referenced across radar tracking, radio communications, and flight
 * management subsystems.
 * 
 * Key Responsibilities:
 * - Maintain immutable identification data (callsign, pilot name)
 * - Track availability state to indicate whether jetpack is in service or grounded
 * - Provide standardized string representation for logging and display
 * 
 * Interactions:
 * - Referenced by Radar for position tracking
 * - Used in Radio broadcasts and communications
 * - Tracked by AirTrafficController in managed jetpack registry
 * - Associated with FlightPath objects for flight plan management
 * - Displayed in UI panels and 3D renderers
 * 
 * Patterns & Constraints:
 * - Immutable identity fields (callsign, pilotName) ensure data integrity
 * - Lightweight value object suitable for high-frequency updates
 * - No direct dependencies on UI or infrastructure layers
 * - Thread-safe for read operations; write to availability requires synchronization at higher layers
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class JetPack {
    // Radio callsign for ATC communications (e.g., "N123JP")
    private final String callsign;
    // Name of jetpack pilot/operator
    private final String pilotName;
    // Whether jetpack is available for flight operations
    private boolean available;

    /**
     * Constructs a new JetPack with specified identification.
     * Jetpack is initially marked as available.
     * 
     * @param callsign Radio callsign for ATC communications
     * @param pilotName Name of pilot/operator
     */
    public JetPack(String callsign, String pilotName) {
        // Store callsign for radio identification
        this.callsign = callsign;
        // Store pilot name for operator records
        this.pilotName = pilotName;
        // Initialize as available for flight operations
        this.available = true;
    }

    /**
     * Returns the radio callsign of this jetpack.
     * 
     * @return Callsign string
     */
    public String getCallsign() {
        // Return stored callsign identifier
        return callsign;
    }

    /**
     * Returns the name of the pilot operating this jetpack.
     * 
     * @return Pilot name string
     */
    public String getPilotName() {
        // Return stored pilot name
        return pilotName;
    }

    /**
     * Returns whether this jetpack is available for operations.
     * 
     * @return true if available, false if grounded or unavailable
     */
    public boolean isAvailable() {
        // Return current availability status
        return available;
    }

    /**
     * Sets the availability status of this jetpack.
     * 
     * @param available true for available, false for unavailable
     */
    public void setAvailable(boolean available) {
        // Update availability status to new value
        this.available = available;
    }

    /**
     * Returns formatted string representation of this jetpack.
     * 
     * @return String with callsign, pilot name, and availability
     */
    @Override
    public String toString() {
        // Format jetpack details as readable string for logging/display
        return String.format("JetPack[callsign=%s, pilot=%s, available=%s]", callsign, pilotName, available);
    }
}
