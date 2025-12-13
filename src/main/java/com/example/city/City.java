/**
 * Domain model representing a city's airspace with dimensions and parking infrastructure.
 * 
 * Purpose:
 * Defines the operational boundaries and parking resources for a specific city in the air traffic
 * control system. Encapsulates city-level metadata including name, map dimensions, and designated
 * parking spaces for jetpack operations. Serves as a lightweight container for city-specific data
 * used by backend operations and non-GUI scenarios.
 * 
 * Key Responsibilities:
 * - Store city identification (name) for multi-city support
 * - Define airspace boundaries via width and height dimensions
 * - Maintain collection of parking spaces available in this city
 * - Provide accessor methods for city properties and parking allocation
 * - Support parking space registration and management
 * - Enable city-level queries for operational planning
 * 
 * Interactions:
 * - Used by AirTrafficController backend for programmatic city management
 * - Referenced in non-GUI simulations and batch processing scenarios
 * - Provides parking data for flight planning algorithms
 * - Alternative to GUI-based CityMapPanel city representation
 * - Supports testing and automation without UI dependencies
 * - Can be persisted for configuration or session state
 * 
 * Patterns & Constraints:
 * - Simple data transfer object (DTO) pattern for backend use
 * - Mutable parking space list for dynamic allocation
 * - Complementary to GUI city representation in AirTrafficControllerFrame
 * - Minimal dependencies; no UI or rendering concerns
 * - Suitable for command-line tools and service implementations
 * - Dimensions represent map pixel coordinates (width x height)
 * - City name matches map file names (New York, Boston, Houston, Dallas)
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
