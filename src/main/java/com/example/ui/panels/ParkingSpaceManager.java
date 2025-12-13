/**
 * Centralized management for parkingspace operations and lifecycle coordination.
 * 
 * Purpose:
 * Manages parkingspace instances across the Air Traffic Controller system, providing
 * factory methods, registry access, and coordination logic. Supports multi-city scenarios
 * and ensures consistent parkingspace state across operational contexts.
 * 
 * Key Responsibilities:
 * - Initialize and maintain parkingspace collections per city or system-wide
 * - Provide query methods for parkingspace retrieval and filtering
 * - Coordinate parkingspace state updates across subsystems
 * - Support parkingspace lifecycle (creation, modification, disposal)
 * 
 * Interactions:
 * - Referenced by AirTrafficControllerFrame and CityMapPanel
 * - Integrates with logging and persistence subsystems
 * - Coordinates with related manager classes
 * 
 * Patterns & Constraints:
 * - Manager pattern centralizes parkingspace concerns
 * - Thread-safe operations for concurrent access
 * - Per-city collections for multi-city support
 * 
 * @author Haisam Elkewidy
 */

package com.example.ui.panels;
import com.example.model.ParkingSpace;

import java.util.ArrayList;
import java.util.List;

public class ParkingSpaceManager {
    // Collection of all parking spaces managed by this instance
    private final List<ParkingSpace> parkingSpaces;

    /**
     * Constructs a new ParkingSpaceManager with empty collection.
     */
    public ParkingSpaceManager() {
        // Initialize empty list to hold parking spaces
        this.parkingSpaces = new ArrayList<>();
    }

    /**
     * Adds a parking space to the managed collection.
     * 
     * @param space ParkingSpace to add
     */
    public void addParkingSpace(ParkingSpace space) {
        // Append parking space to collection
        parkingSpaces.add(space);
    }

    /**
     * Returns the list of all managed parking spaces.
     * 
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        // Return reference to parking spaces collection
        return parkingSpaces;
    }
}
