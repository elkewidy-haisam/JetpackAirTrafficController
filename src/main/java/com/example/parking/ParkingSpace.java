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
    /** Unique identifier for this parking space (e.g., "NYC-P001", "BOS-P042") */
    private final String id;
    
    /** Geographic location of this parking space on the city map */
    private final Point location;
    
    /** Whether this parking space is currently occupied by a jetpack */
    private boolean isOccupied;

    /**
     * Constructs a new ParkingSpace at the specified coordinates.
     * Initially the space is vacant (not occupied).
     * 
     * @param id unique identifier for this parking space
     * @param x the x-coordinate location on the map
     * @param y the y-coordinate location on the map
     */
    public ParkingSpace(String id, int x, int y) {
        this.id = id;                          // Store the unique parking space ID
        this.location = new Point(x, y);       // Create Point object with coordinates
        this.isOccupied = false;               // Initialize as vacant
    }

    /**
     * Returns the unique identifier of this parking space.
     * @return the parking space ID string
     */
    public String getId() { 
        return id;  // Return the stored ID
    }
    
    /**
     * Returns the geographic location of this parking space.
     * @return the Point object containing x,y coordinates
     */
    public Point getLocation() { 
        return location;  // Return the location Point
    }
    
    /**
     * Checks if this parking space is currently occupied.
     * @return true if occupied, false if vacant
     */
    public boolean isOccupied() { 
        return isOccupied;  // Return the occupation status
    }
    
    /**
     * Returns the x-coordinate of this parking space.
     * @return the x-coordinate value
     */
    public int getX() { 
        return location.x;  // Return x from the location Point
    }
    
    /**
     * Returns the y-coordinate of this parking space.
     * @return the y-coordinate value
     */
    public int getY() { 
        return location.y;  // Return y from the location Point
    }
    
    /**
     * Marks this parking space as occupied.
     * Called when a jetpack lands in this space.
     */
    public void occupy() { 
        this.isOccupied = true;  // Set occupied flag to true
    }
    
    /**
     * Marks this parking space as vacant.
     * Called when a jetpack departs from this space.
     */
    public void vacate() { 
        this.isOccupied = false;  // Set occupied flag to false
    }

    /**
     * Returns a formatted string representation of this parking space.
     * 
     * @return formatted string with ID, location, and occupation status
     */
    @Override
    public String toString() {
        // Format and return parking space details
        return String.format("ParkingSpace[%s]: Location=%s Occupied=%b", id, location, isOccupied);
    }
}
