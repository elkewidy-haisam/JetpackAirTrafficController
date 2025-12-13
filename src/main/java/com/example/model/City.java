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
    private final String name;
    private final int width;
    private final int height;
    private final List<ParkingSpace> parkingSpaces;

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

    public void addParkingSpace(ParkingSpace space) {
        parkingSpaces.add(space);
    }

    @Override
    public String toString() {
        return String.format("City[name=%s, width=%d, height=%d, parkingSpaces=%d]", name, width, height, parkingSpaces.size());
    }
}
