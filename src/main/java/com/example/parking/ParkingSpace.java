/**
 * Domain model representing a designated parking location for jetpack aircraft landing and storage.
 * 
 * Purpose:
 * Encapsulates a single parking space with unique identification, map coordinates, and occupancy state.
 * Serves as the fundamental unit of parking infrastructure within the air traffic control system,
 * enabling jetpacks to safely land, park for rest periods, and vacate when ready to resume flight
 * operations. Provides occupancy tracking to prevent double-booking and parking conflicts.
 * 
 * Key Responsibilities:
 * - Store immutable parking space identity (ID) and location coordinates
 * - Track current occupancy status (occupied vs. available)
 * - Provide location queries for distance calculations and map rendering
 * - Support occupy/vacate operations for parking lifecycle management
 * - Enable identification in logs and UI displays with formatted ID (e.g., NYC-P42, BOS-P17)
 * - Maintain simple state suitable for high-frequency queries during landing operations
 * 
 * Interactions:
 * - Created by ParkingSpaceGenerator and ParkingSpaceManager during map initialization
 * - Allocated by JetPackFlightState when jetpack enters parking mode
 * - Queried by FlightEmergencyHandler to find nearest available space during emergencies
 * - Rendered in CityMapPanel as green markers (available) or red markers (occupied)
 * - Referenced in movement logs when jetpack parks or departs
 * - Used by collision detection to avoid landing conflicts
 * - Tracked by ParkingSpaceManager for availability queries
 * 
 * Patterns & Constraints:
 * - Immutable identity (ID) and location after construction
 * - Mutable occupancy state for dynamic allocation/deallocation
 * - Lightweight data object suitable for collections of 100+ spaces per city
 * - Thread-safe for read operations; external synchronization for occupy/vacate
 * - ID format: [CITY_CODE]-P[number] (e.g., NYC-P001, BOS-P042, HOU-P099)
 * - Location guaranteed to be on land (not water) via generation-time filtering
 * - No ownership tracking; only binary occupied/available state
 * 
 * @author Haisam Elkewidy
 */

package com.example.parking;

import java.awt.Point;

public class ParkingSpace {
    private final String id;
    private final Point location;
    private boolean isOccupied;

    public ParkingSpace(String id, int x, int y) {
        this.id = id;
        this.location = new Point(x, y);
        this.isOccupied = false;
    }

    public String getId() { return id; }
    public Point getLocation() { return location; }
    public boolean isOccupied() { return isOccupied; }
    public int getX() { return location.x; }
    public int getY() { return location.y; }
    public void occupy() { this.isOccupied = true; }
    public void vacate() { this.isOccupied = false; }

    @Override
    public String toString() {
        return String.format("ParkingSpace[%s]: Location=%s Occupied=%b", id, location, isOccupied);
    }
}
