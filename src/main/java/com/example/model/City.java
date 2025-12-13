/**
 * Represents a metropolitan area with geographic dimensions and parking infrastructure.
 * 
 * Purpose:
 * Models a city entity containing spatial boundaries (width, height) and a registry of parking spaces
 * where jetpacks can land and be stored. Serves as a container for city-specific data including
 * infrastructure layout and resource allocation for flight operations.
 * 
 * Key Responsibilities:
 * - Define city geographic extents for map rendering and flight boundaries
 * - Maintain a collection of ParkingSpace objects for jetpack landing zones
 * - Provide methods to add parking infrastructure dynamically
 * - Support city identification via unique name
 * 
 * Interactions:
 * - Referenced by CityMapPanel for rendering city layouts and parking locations
 * - Used in CitySelectionPanel for city chooser interface
 * - Associated with RealisticCityMap for detailed geographic representations
 * - Consumed by ParkingSpaceManager for landing zone allocation
 * - Displayed in various UI components with city-specific data
 * 
 * Patterns & Constraints:
 * - Immutable dimensions ensure consistent spatial calculations
 * - Parking spaces collection is mutable to support dynamic infrastructure updates
 * - Lightweight domain model suitable for multiple city instances
 * - No direct coupling to rendering or persistence layers
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class City {
    /** The unique identifying name of this city (e.g., "New York", "Boston") */
    private final String name;
    /** The width of the city map in coordinate units */
    private final int width;
    /** The height of the city map in coordinate units */
    private final int height;
    /** Collection of parking spaces available in this city */
    private final List<ParkingSpace> parkingSpaces;

    /**
     * Constructs a new City with specified name and dimensions.
     * Initializes empty parking space list.
     * 
     * @param name the unique city name identifier
     * @param width the city map width in coordinate units
     * @param height the city map height in coordinate units
     */
    public City(String name, int width, int height) {
        this.name = name;                        // Store the city name
        this.width = width;                      // Store the map width
        this.height = height;                    // Store the map height
        this.parkingSpaces = new ArrayList<>();  // Initialize empty parking list
    }

    /**
     * Returns the name of this city.
     * @return city name string
     */
    public String getName() {
        return name;  // Return the stored city name
    }

    /**
     * Returns the width of the city map.
     * @return map width value
     */
    public int getWidth() {
        return width;  // Return the stored width
    }

    /**
     * Returns the height of the city map.
     * @return map height value
     */
    public int getHeight() {
        return height;  // Return the stored height
    }

    /**
     * Returns the list of parking spaces in this city.
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;  // Return the parking spaces list
    }

    /**
     * Adds a parking space to this city.
     * 
     * @param space the ParkingSpace to add
     */
    public void addParkingSpace(ParkingSpace space) {
        parkingSpaces.add(space);  // Append parking space to the list
    }

    /**
     * Returns a string representation of this city.
     * @return formatted string with city name, dimensions, and parking count
     */
    @Override
    public String toString() {
        return String.format("City[name=%s, width=%d, height=%d, parkingSpaces=%d]", name, width, height, parkingSpaces.size());  // Format and return city info
    }
}
