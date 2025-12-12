/**
 * RealisticCityMap.java
 * by Haisam Elkewidy
 *
 * Represents a realistic city map for jetpack navigation and parking.
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
