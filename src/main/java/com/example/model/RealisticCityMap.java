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
    private final int width;
    private final int height;
    private final List<ParkingSpace> parkingSpaces;

    public RealisticCityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.parkingSpaces = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }

    public void addParkingSpace(ParkingSpace space) {
        parkingSpaces.add(space);
    }

    @Override
    public String toString() {
        return String.format("RealisticCityMap[width=%d, height=%d, parkingSpaces=%d]", width, height, parkingSpaces.size());
    }
}
