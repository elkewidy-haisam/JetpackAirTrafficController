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
    /** The width of the city map in coordinate units */
    private final int width;
    /** The height of the city map in coordinate units */
    private final int height;
    /** List of parking spaces available in this city map */
    private final List<ParkingSpace> parkingSpaces;

    /**
     * Constructs a new RealisticCityMap with specified dimensions.
     * Initializes empty parking space list.
     * 
     * @param width the map width in coordinate units
     * @param height the map height in coordinate units
     */
    public RealisticCityMap(int width, int height) {
        this.width = width;                        // Store the map width
        this.height = height;                      // Store the map height
        this.parkingSpaces = new ArrayList<>();    // Initialize empty parking list
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
     * Returns the list of parking spaces in this map.
     * @return List of ParkingSpace objects
     */
    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;  // Return the parking spaces list
    }

    /**
     * Adds a parking space to this city map.
     * 
     * @param space the ParkingSpace to add
     */
    public void addParkingSpace(ParkingSpace space) {
        parkingSpaces.add(space);  // Append parking space to the list
    }

    /**
     * Returns a string representation of this city map.
     * @return formatted string with map dimensions and parking count
     */
    @Override
    public String toString() {
        return String.format("RealisticCityMap[width=%d, height=%d, parkingSpaces=%d]", width, height, parkingSpaces.size());  // Format and return map info
    }
}
