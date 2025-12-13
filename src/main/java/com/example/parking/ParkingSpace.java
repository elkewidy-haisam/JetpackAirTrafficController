/**
 * ParkingSpace component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides parkingspace functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core parkingspace operations
 * - Maintain necessary state for parkingspace functionality
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
