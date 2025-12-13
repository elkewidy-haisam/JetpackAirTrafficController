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
    private final String callsign;
    private final String pilotName;
    private boolean available;

    public JetPack(String callsign, String pilotName) {
        this.callsign = callsign;
        this.pilotName = pilotName;
        this.available = true;
    }

    public String getCallsign() {
        return callsign;
    }

    public String getPilotName() {
        return pilotName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("JetPack[callsign=%s, pilot=%s, available=%s]", callsign, pilotName, available);
    }
}
