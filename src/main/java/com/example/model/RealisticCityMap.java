/**
 * RealisticCityMap component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides realisticcitymap functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core realisticcitymap operations
 * - Maintain necessary state for realisticcitymap functionality
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

package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class RealisticCityMap {
    // Horizontal extent of city map in grid coordinate units
    private final int width;
    // Vertical extent of city map in grid coordinate units
    private final int height;
    // Collection of parking spaces available on this map
    private final List<ParkingSpace> parkingSpaces;

    /**
     * Constructs a new RealisticCityMap with specified dimensions.
     * Initializes with empty parking spaces list.
     * 
     * @param width Horizontal extent in grid units
     * @param height Vertical extent in grid units
     */
    public RealisticCityMap(int width, int height) {
        // Store horizontal boundary of map area
        this.width = width;
        // Store vertical boundary of map area
        this.height = height;
        // Initialize empty list to hold parking spaces added later
        this.parkingSpaces = new ArrayList<>();
    }

    /**
     * Returns the width of the city map.
     * 
     * @return Map width in grid units
     */
    public int getWidth() {
        // Return horizontal extent
        return width;
    }

    /**
     * Returns the height of the city map.
     * 
     * @return Map height in grid units
     */
    public int getHeight() {
        // Return vertical extent
        return height;
    }

    /**
     * Returns the list of parking spaces on this map.
     * 
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        // Return reference to parking spaces collection
        return parkingSpaces;
    }

    /**
     * Adds a parking space to this city map's infrastructure.
     * 
     * @param space ParkingSpace to add
     */
    public void addParkingSpace(ParkingSpace space) {
        // Append parking space to map's collection
        parkingSpaces.add(space);
    }

    /**
     * Returns formatted string representation of this city map.
     * 
     * @return String with dimensions and parking count
     */
    @Override
    public String toString() {
        // Format city map details as readable string including parking count
        return String.format("RealisticCityMap[width=%d, height=%d, parkingSpaces=%d]", width, height, parkingSpaces.size());
    }
}
