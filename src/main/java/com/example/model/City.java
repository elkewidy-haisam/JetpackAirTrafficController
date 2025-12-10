/*
 * City.java
 * Part of Jetpack Air Traffic Controller
 *
 * Represents a city in the simulation, including grid and parking spaces.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
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
