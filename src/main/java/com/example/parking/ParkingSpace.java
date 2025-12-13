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
    // Unique identifier for this parking space (e.g., "P1", "NY-P042", "GEN-P15")
    private final String id;
    
    // 2D coordinates of parking space on the city map
    private final Point location;
    
    // Occupancy flag: true if jetpack currently parked, false if available
    private boolean isOccupied;

    /**
     * Constructs a new ParkingSpace with specified ID and coordinates.
     * Initializes as unoccupied (available for jetpack parking).
     * 
     * @param id Unique parking space identifier
     * @param x X coordinate on city map
     * @param y Y coordinate on city map
     */
    public ParkingSpace(String id, int x, int y) {
        // Store immutable identification
        this.id = id;
        
        // Store immutable location as Point object
        this.location = new Point(x, y);
        
        // Initialize as unoccupied - available for use
        this.isOccupied = false;
    }

    /** Returns the unique identifier for this parking space */
    public String getId() { return id; }
    
    /** Returns the Point object representing parking space location */
    public Point getLocation() { return location; }
    
    /** Returns true if a jetpack is currently parked here */
    public boolean isOccupied() { return isOccupied; }
    
    /** Returns the X coordinate of this parking space */
    public int getX() { return location.x; }
    
    /** Returns the Y coordinate of this parking space */
    public int getY() { return location.y; }
    
    /**
     * Marks this parking space as occupied.
     * Should be called when a jetpack parks here.
     */
    public void occupy() { 
        // Set occupied flag to indicate space is in use
        this.isOccupied = true; 
    }
    
    /**
     * Marks this parking space as vacant/available.
     * Should be called when a jetpack departs from this space.
     */
    public void vacate() { 
        // Clear occupied flag to indicate space is available
        this.isOccupied = false; 
    }

    /**
     * Returns a formatted string representation of this parking space.
     * Includes ID, location coordinates, and occupancy status.
     * 
     * @return Human-readable parking space description
     */
    @Override
    public String toString() {
        // Format as: ParkingSpace[ID]: Location=(x,y) Occupied=true/false
        return String.format("ParkingSpace[%s]: Location=%s Occupied=%b", id, location, isOccupied);
    }
}
