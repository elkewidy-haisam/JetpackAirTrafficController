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
