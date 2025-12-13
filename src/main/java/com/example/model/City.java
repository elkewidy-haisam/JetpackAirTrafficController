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
    // Unique name identifier for this city (e.g., "New York", "Boston")
    private final String name;
    // Horizontal extent of city in grid coordinate units
    private final int width;
    // Vertical extent of city in grid coordinate units
    private final int height;
    // Collection of parking spaces available in this city
    private final List<ParkingSpace> parkingSpaces;

    /**
     * Constructs a new City with specified name and dimensions.
     * Initializes with empty parking spaces list.
     * 
     * @param name Unique name identifier for city
     * @param width Horizontal extent in grid units
     * @param height Vertical extent in grid units
     */
    public City(String name, int width, int height) {
        // Store city name for identification and display
        this.name = name;
        // Store horizontal boundary of city area
        this.width = width;
        // Store vertical boundary of city area
        this.height = height;
        // Initialize empty list to hold parking spaces added later
        this.parkingSpaces = new ArrayList<>();
    }

    /**
     * Returns the name of this city.
     * 
     * @return City name string
     */
    public String getName() {
        // Return stored city name
        return name;
    }

    /**
     * Returns the width of the city area.
     * 
     * @return City width in grid units
     */
    public int getWidth() {
        // Return horizontal extent
        return width;
    }

    /**
     * Returns the height of the city area.
     * 
     * @return City height in grid units
     */
    public int getHeight() {
        // Return vertical extent
        return height;
    }

    /**
     * Returns the list of parking spaces in this city.
     * 
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        // Return reference to parking spaces collection
        return parkingSpaces;
    }

    /**
     * Adds a parking space to this city's infrastructure.
     * 
     * @param space ParkingSpace to add
     */
    public void addParkingSpace(ParkingSpace space) {
        // Append parking space to city's collection
        parkingSpaces.add(space);
    }

    /**
     * Returns formatted string representation of this city.
     * 
     * @return String with name, dimensions, and parking count
     */
    @Override
    public String toString() {
        // Format city details as readable string including parking count
        return String.format("City[name=%s, width=%d, height=%d, parkingSpaces=%d]", name, width, height, parkingSpaces.size());
    }
}
