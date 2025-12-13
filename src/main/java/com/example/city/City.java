/**
 * Represents a city in the air traffic control system with geographic boundaries and parking infrastructure.
 * 
 * Purpose:
 * Models a city as a bounded geographic area where jetpacks can operate, containing essential spatial
 * information (dimensions) and the infrastructure (parking spaces) necessary for jetpack operations.
 * This is the core domain model that defines the operational theater for air traffic control.
 * 
 * Key Responsibilities:
 * - Store city identification (name) and geographic dimensions (width, height)
 * - Maintain a collection of available parking spaces within the city
 * - Provide accessor methods for city properties
 * - Serve as a data container for city-specific configuration
 * 
 * Interactions:
 * - Used by CityMapPanel and UI components to define rendering boundaries
 * - Referenced by ParkingSpaceGenerator to create and assign parking locations
 * - Queried by JetPackFlight to determine if coordinates are within city bounds
 * - Accessed by CityMapLoader to initialize city-specific map displays
 * 
 * Patterns & Constraints:
 * - Simple data model (POJO) with minimal behavior
 * - Immutable identification and dimensions (set only at construction)
 * - Mutable parking space collection to support dynamic infrastructure updates
 * - No business logic - pure data container following JavaBean conventions
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
    /** The unique name identifier for this city (e.g., "New York", "Boston") */
    private String name;
    
    /** The width of the city map in pixels or coordinate units */
    private int width;
    
    /** The height of the city map in pixels or coordinate units */
    private int height;
    
    /** Collection of all parking spaces available in this city for jetpack landings */
    private List<ParkingSpace> parkingSpaces;
    
    /**
     * Constructs a new City with specified dimensions.
     * Initializes an empty list of parking spaces.
     * 
     * @param name the unique identifier name for this city
     * @param width the horizontal dimension of the city map
     * @param height the vertical dimension of the city map
     */
    public City(String name, int width, int height) {
        this.name = name;                           // Store the city name
        this.width = width;                         // Store the map width
        this.height = height;                       // Store the map height
        this.parkingSpaces = new ArrayList<>();     // Initialize empty parking space collection
    }
    
    /**
     * Returns the name of this city.
     * 
     * @return the city name as a String
     */
    public String getName() {
        return name;  // Return the stored city name
    }
    
    /**
     * Returns the width of this city's map.
     * 
     * @return the map width in coordinate units
     */
    public int getWidth() {
        return width;  // Return the stored width value
    }
    
    /**
     * Returns the height of this city's map.
     * 
     * @return the map height in coordinate units
     */
    public int getHeight() {
        return height;  // Return the stored height value
    }
    
    /**
     * Returns the list of all parking spaces in this city.
     * 
     * @return the List of ParkingSpace objects available in this city
     */
    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;  // Return the parking spaces collection
    }
    
    /**
     * Sets or replaces the parking spaces for this city.
     * 
     * @param spaces the new List of ParkingSpace objects to assign to this city
     */
    public void setParkingSpaces(List<ParkingSpace> spaces) {
        this.parkingSpaces = spaces;  // Replace the parking spaces collection
    }
}
