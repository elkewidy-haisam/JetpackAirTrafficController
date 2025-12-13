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
    // Unique identifier for this parking space (e.g., "P-001")
    private final String id;
    // 2D coordinates of parking space location in city grid
    private final Point location;
    // Flag indicating whether a jetpack is currently parked here
    private boolean isOccupied;

    /**
     * Constructs a new parking space at specified coordinates.
     * 
     * @param id Unique identifier for this parking space
     * @param x X-coordinate in city grid
     * @param y Y-coordinate in city grid
     */
    public ParkingSpace(String id, int x, int y) {
        // Store the unique identifier for this parking space
        this.id = id;
        // Create Point object with provided coordinates for location tracking
        this.location = new Point(x, y);
        // Initialize as unoccupied; jetpack will occupy it later
        this.isOccupied = false;
    }

    // Returns the unique identifier of this parking space
    public String getId() { return id; }
    // Returns the Point object containing X,Y coordinates
    public Point getLocation() { return location; }
    // Returns whether this space is currently occupied by a jetpack
    public boolean isOccupied() { return isOccupied; }
    // Returns X-coordinate by accessing Point's x field
    public int getX() { return location.x; }
    // Returns Y-coordinate by accessing Point's y field
    public int getY() { return location.y; }
    // Marks this parking space as occupied when jetpack parks
    public void occupy() { this.isOccupied = true; }
    // Marks this parking space as vacant when jetpack departs
    public void vacate() { this.isOccupied = false; }

    /**
     * Returns formatted string representation of this parking space.
     * 
     * @return String containing ID, location, and occupancy status
     */
    @Override
    public String toString() {
        // Format parking space details as readable string for logging/display
        return String.format("ParkingSpace[%s]: Location=%s Occupied=%b", id, location, isOccupied);
    }
}
