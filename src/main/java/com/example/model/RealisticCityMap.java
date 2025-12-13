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

/**
 * Represents a realistic city map with defined boundaries and parking infrastructure.
 * Provides spatial context for jetpack navigation and landing zone management.
 */
public class RealisticCityMap {
    // Width of the city map in coordinate units
    private final int width;
    // Height of the city map in coordinate units
    private final int height;
    // Mutable collection of parking spaces within the city
    private final List<ParkingSpace> parkingSpaces;

    /**
     * Constructs a new city map with specified dimensions.
     * Initializes with an empty parking space list.
     *
     * @param width Horizontal extent of the map
     * @param height Vertical extent of the map
     */
    public RealisticCityMap(int width, int height) {
        // Set immutable width boundary
        this.width = width;
        // Set immutable height boundary
        this.height = height;
        // Initialize empty parking space collection (populated externally)
        this.parkingSpaces = new ArrayList<>();
    }

    /**
     * Gets the width of the city map.
     *
     * @return Map width in coordinate units
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the city map.
     *
     * @return Map height in coordinate units
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the list of parking spaces in this city.
     * Returns mutable list for external modification if needed.
     *
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }

    /**
     * Adds a new parking space to the city map.
     *
     * @param space ParkingSpace to add to the collection
     */
    public void addParkingSpace(ParkingSpace space) {
        // Append parking space to internal list
        parkingSpaces.add(space);
    }

    /**
     * Returns a string representation of this city map.
     * Includes dimensions and parking space count for debugging.
     *
     * @return Formatted string with map details
     */
    @Override
    public String toString() {
        // Format as: RealisticCityMap[width=X, height=Y, parkingSpaces=N]
        return String.format("RealisticCityMap[width=%d, height=%d, parkingSpaces=%d]", width, height, parkingSpaces.size());
    }
}
