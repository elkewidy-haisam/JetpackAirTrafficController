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
    /** Unique identifier callsign for this jetpack (e.g., "ALPHA-1", "BRAVO-2") */
    private final String callsign;
    /** Name of the pilot operating this jetpack */
    private final String pilotName;
    /** Whether this jetpack is available for operations (true = available, false = grounded) */
    private boolean available;

    /**
     * Constructs a new JetPack with specified identification.
     * Jetpack is initially marked as available.
     * 
     * @param callsign the unique callsign identifier for radio communications
     * @param pilotName the name of the pilot operating this jetpack
     */
    public JetPack(String callsign, String pilotName) {
        this.callsign = callsign;    // Store the callsign identifier
        this.pilotName = pilotName;  // Store the pilot's name
        this.available = true;       // Mark as available initially
    }

    /**
     * Returns the callsign of this jetpack.
     * @return the callsign identifier string
     */
    public String getCallsign() {
        return callsign;  // Return the stored callsign
    }

    /**
     * Returns the name of the pilot operating this jetpack.
     * @return the pilot's name
     */
    public String getPilotName() {
        return pilotName;  // Return the stored pilot name
    }

    /**
     * Checks if this jetpack is available for operations.
     * @return true if available, false if grounded
     */
    public boolean isAvailable() {
        return available;  // Return the availability status
    }

    /**
     * Sets the availability status of this jetpack.
     * @param available true to mark available, false to mark grounded
     */
    public void setAvailable(boolean available) {
        this.available = available;  // Update the availability status
    }

    /**
     * Returns a string representation of this jetpack.
     * @return formatted string with callsign, pilot, and availability
     */
    @Override
    public String toString() {
        return String.format("JetPack[callsign=%s, pilot=%s, available=%s]", callsign, pilotName, available);  // Format and return jetpack info
    }
}
