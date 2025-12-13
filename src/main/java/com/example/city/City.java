/**
 * City component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides city functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core city operations
 * - Maintain necessary state for city functionality
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

package com.example.city;

import java.util.ArrayList;
import java.util.List;

import com.example.parking.ParkingSpace;

/**
 * City class represents a city with its dimensions and parking spaces.
 * Encapsulates geographic and infrastructure data for jetpack operations.
 */
public class City {
    // Human-readable name of the city (e.g., "Boston", "New York")
    private String name;
    // Width of the city map in grid units
    private int width;
    // Height of the city map in grid units
    private int height;
    // Collection of available parking spaces for jetpack landing/storage
    private List<ParkingSpace> parkingSpaces;
    
    /**
     * Constructs a new City with specified dimensions.
     * Initializes with empty parking space list.
     *
     * @param name Display name of the city
     * @param width Horizontal extent of city map in grid units
     * @param height Vertical extent of city map in grid units
     */
    public City(String name, int width, int height) {
        // Assign city name for identification
        this.name = name;
        // Set map width boundary
        this.width = width;
        // Set map height boundary
        this.height = height;
        // Initialize empty list to hold parking spaces (will be populated later)
        this.parkingSpaces = new ArrayList<>();
    }
    
    /**
     * Gets the name of this city.
     *
     * @return City name string
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the width of the city map.
     *
     * @return Width in grid units
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of the city map.
     *
     * @return Height in grid units
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the list of parking spaces in this city.
     *
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }
    
    /**
     * Replaces the current parking spaces with a new list.
     *
     * @param spaces New list of parking spaces to assign
     */
    public void setParkingSpaces(List<ParkingSpace> spaces) {
        // Replace entire parking space collection
        this.parkingSpaces = spaces;
    }
}
