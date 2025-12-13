/**
 * Model representing city map data with geographic information.
 * 
 * Purpose:
 * Stores city map metadata and configuration for rendering realistic city layouts. May include map
 * dimensions, tile information, or geographic reference data used by map rendering components.
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class RealisticCityMap {
    /** Width of the city map in coordinate units or pixels */
    private final int width;
    /** Height of the city map in coordinate units or pixels */
    private final int height;
    /** List of parking spaces available on this city map */
    private final List<ParkingSpace> parkingSpaces;

    /**
     * Constructs a new RealisticCityMap with specified dimensions.
     * Initializes with empty parking space list.
     * 
     * @param width the width of the city map
     * @param height the height of the city map
     */
    public RealisticCityMap(int width, int height) {
        this.width = width;                        // Store map width
        this.height = height;                      // Store map height
        this.parkingSpaces = new ArrayList<>();   // Initialize empty parking list
    }

    /**
     * Returns the width of this city map.
     * @return map width in coordinate units
     */
    public int getWidth() {
        return width;  // Return stored width
    }

    /**
     * Returns the height of this city map.
     * @return map height in coordinate units
     */
    public int getHeight() {
        return height;  // Return stored height
    }

    /**
     * Returns the list of parking spaces on this map.
     * Returns the actual list (not a copy), allowing external modification.
     * 
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;  // Return parking spaces list
    }

    /**
     * Adds a parking space to this city map.
     * Appends the space to the parking spaces list.
     * 
     * @param space the ParkingSpace to add to this map
     */
    public void addParkingSpace(ParkingSpace space) {
        parkingSpaces.add(space);  // Add space to list
    }

    /**
     * Returns a formatted string representation of this city map.
     * Includes dimensions and parking space count.
     * 
     * @return formatted string with map details
     */
    @Override
    public String toString() {
        // Format string with width, height, and number of parking spaces
        return String.format("RealisticCityMap[width=%d, height=%d, parkingSpaces=%d]", 
                width, height, parkingSpaces.size());
    }
}
