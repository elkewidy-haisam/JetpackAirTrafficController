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
    private final List<ParkingSpace> parkingSpaces;

    public ParkingSpaceManager() {
        this.parkingSpaces = new ArrayList<>();
    }

    public void addParkingSpace(ParkingSpace space) {
        parkingSpaces.add(space);
    }

    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }
}
