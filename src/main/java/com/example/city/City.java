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
    private String name;
    private int width;
    private int height;
    private List<ParkingSpace> parkingSpaces;
    
    public City(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.parkingSpaces = new ArrayList<>();
    }
    
    public String getName() {
        return name;
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
    
    public void setParkingSpaces(List<ParkingSpace> spaces) {
        this.parkingSpaces = spaces;
    }
}
