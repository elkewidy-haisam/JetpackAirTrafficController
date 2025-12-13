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
    /** Unique radio callsign for this jetpack (e.g., "ALPHA-01", "BRAVO-05") */
    private final String callsign;
    /** Name of the pilot operating this jetpack */
    private final String pilotName;
    /** Whether this jetpack is available for assignment (true) or in use/maintenance (false) */
    private boolean available;

    /**
     * Constructs a new JetPack with specified callsign and pilot.
     * Initializes the jetpack as available by default.
     * 
     * @param callsign unique radio callsign for ATC communications
     * @param pilotName name of the pilot operating this jetpack
     */
    public JetPack(String callsign, String pilotName) {
        this.callsign = callsign;    // Store immutable callsign
        this.pilotName = pilotName;  // Store immutable pilot name
        this.available = true;       // Mark as available initially
    }

    /**
     * Returns the radio callsign of this jetpack.
     * @return callsign string used for ATC communications
     */
    public String getCallsign() {
        return callsign;  // Return immutable callsign
    }

    /**
     * Returns the pilot's name.
     * @return name of the pilot operating this jetpack
     */
    public String getPilotName() {
        return pilotName;  // Return immutable pilot name
    }

    /**
     * Checks if this jetpack is available for assignment.
     * @return true if available, false if in use or under maintenance
     */
    public boolean isAvailable() {
        return available;  // Return availability status
    }

    /**
     * Sets the availability status of this jetpack.
     * Used to mark jetpack as in-use or available.
     * 
     * @param available true to mark available, false to mark unavailable
     */
    public void setAvailable(boolean available) {
        this.available = available;  // Update availability status
    }

    /**
     * Returns a formatted string representation of this jetpack.
     * Includes callsign, pilot name, and availability status.
     * 
     * @return formatted string with jetpack details
     */
    @Override
    public String toString() {
        // Format jetpack details into readable string
        return String.format("JetPack[callsign=%s, pilot=%s, available=%s]", 
                callsign, pilotName, available);
    }
}
