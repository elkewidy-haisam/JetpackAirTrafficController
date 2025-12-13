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
    // Immutable city identifier (e.g., "New York", "Boston", "Houston", "Dallas")
    private final String name;
    
    // Immutable geographic width in abstract units for map boundaries
    private final int width;
    
    // Immutable geographic height in abstract units for map boundaries
    private final int height;
    
    // Mutable collection of parking spaces for jetpack landing zones
    private final List<ParkingSpace> parkingSpaces;

    /**
     * Constructs a new City with specified dimensions and name.
     * Initializes an empty parking space collection ready to receive landing zones.
     * Dimensions define the boundaries for flight operations and map rendering.
     * 
     * @param name City identifier (e.g., "New York")
     * @param width Geographic width for map boundaries
     * @param height Geographic height for map boundaries
     */
    public City(String name, int width, int height) {
        // Store immutable city identification
        this.name = name;
        
        // Store immutable geographic dimensions for boundary calculations
        this.width = width;
        this.height = height;
        
        // Initialize empty parking collection for dynamic infrastructure
        this.parkingSpaces = new ArrayList<>();
    }

    /**
     * Returns the city name identifier.
     * Used for city selection, display labels, and log identification.
     * 
     * @return City name string
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the geographic width of the city.
     * Defines the eastern boundary for flight operations and map rendering.
     * 
     * @return Width in abstract units
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the geographic height of the city.
     * Defines the northern boundary for flight operations and map rendering.
     * 
     * @return Height in abstract units
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the list of parking spaces in this city.
     * Provides access to all available landing zones for jetpack operations.
     * Returns direct reference - callers can modify to add/remove spaces.
     * 
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        // Return direct reference to parking collection
        // Note: Consider defensive copy if external modification is concern
        return parkingSpaces;
    }

    /**
     * Adds a new parking space to the city's infrastructure.
     * Expands the available landing zones for jetpack operations.
     * 
     * @param space ParkingSpace to add to the city
     */
    public void addParkingSpace(ParkingSpace space) {
        // Add parking space to city infrastructure registry
        parkingSpaces.add(space);
    }

    /**
     * Returns formatted string representation of city configuration.
     * Includes name, dimensions, and parking space count for status display.
     * 
     * @return Human-readable city summary
     */
    @Override
    public String toString() {
        // Format as: City[name=New York, width=800, height=600, parkingSpaces=25]
        return String.format("City[name=%s, width=%d, height=%d, parkingSpaces=%d]", name, width, height, parkingSpaces.size());
    }
}
