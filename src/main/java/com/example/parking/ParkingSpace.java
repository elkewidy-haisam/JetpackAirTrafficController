/**
 * Represents a designated parking location for jetpacks with occupation status tracking.
 * 
 * Purpose:
 * Models a physical parking space in the city where jetpacks can land and remain stationary.
 * Tracks the space's unique identifier, geographic location, and current occupation status to
 * support parking assignment, availability queries, and traffic management operations.
 * 
 * Key Responsibilities:
 * - Store unique parking space identifier (city-prefix + number)
 * - Maintain fixed geographic location coordinates
 * - Track occupation state (occupied vs. vacant)
 * - Provide occupy/vacate operations for parking management
 * - Support proximity queries for nearest available space searches
 * 
 * Interactions:
 * - Created by ParkingSpaceGenerator during city initialization
 * - Managed by ParkingSpaceManager for availability tracking
 * - Referenced by JetPackFlightState when jetpacks park
 * - Used by FlightEmergencyHandler to find nearest parking during emergencies
 * - Queried by City for parking infrastructure reporting
 * - Rendered by CityMapPanel as green squares on the map
 * 
 * Patterns & Constraints:
 * - Immutable identifier and location (set at construction)
 * - Single mutable field (isOccupied) for lifecycle management
 * - Simple occupy/vacate operations (no reservation or locking)
 * - No time-based parking limits or expiration
 * - Thread-safety not guaranteed - caller must synchronize access
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
