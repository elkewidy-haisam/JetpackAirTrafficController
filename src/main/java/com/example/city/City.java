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
 * City class represents a city with its dimensions and parking spaces
 */
public class City {
    // Name identifier for the city (e.g., "New York", "Boston")
    private String name;
    // Width of city grid in coordinate units
    private int width;
    // Height of city grid in coordinate units
    private int height;
    // Collection of parking spaces available in this city
    private List<ParkingSpace> parkingSpaces;
    
    /**
     * Constructs a new City with specified dimensions.
     * 
     * @param name Display name of the city
     * @param width Width of city grid in coordinate units
     * @param height Height of city grid in coordinate units
     */
    public City(String name, int width, int height) {
        // Initialize city name for identification and display
        this.name = name;
        // Set horizontal boundary of city grid
        this.width = width;
        // Set vertical boundary of city grid
        this.height = height;
        // Create empty list to hold parking spaces, populated later
        this.parkingSpaces = new ArrayList<>();
    }
    
    /**
     * Returns the name of this city.
     * 
     * @return City name string
     */
    public String getName() {
        // Return the stored city name
        return name;
    }
    
    /**
     * Returns the width of the city grid.
     * 
     * @return Width in coordinate units
     */
    public int getWidth() {
        // Return the horizontal extent of the city
        return width;
    }
    
    /**
     * Returns the height of the city grid.
     * 
     * @return Height in coordinate units
     */
    public int getHeight() {
        // Return the vertical extent of the city
        return height;
    }
    
    /**
     * Returns the list of parking spaces in this city.
     * 
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        // Return the parking spaces collection
        return parkingSpaces;
    }
    
    /**
     * Sets the parking spaces for this city.
     * 
     * @param spaces List of ParkingSpace objects to assign
     */
    public void setParkingSpaces(List<ParkingSpace> spaces) {
        // Replace existing parking spaces with new collection
        this.parkingSpaces = spaces;
    }
}
