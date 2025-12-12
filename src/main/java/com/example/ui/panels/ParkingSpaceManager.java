/*
 * ParkingSpaceManager.java
 * Part of Jetpack Air Traffic Controller
 *
 * Manages parking spaces for jetpacks in the city map panel.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */

package com.example.ui.panels;
import com.example.model.ParkingSpace;

import java.util.ArrayList;
import java.util.List;

public class ParkingSpaceManager {
    private final List<ParkingSpace> parkingSpaces;

    public ParkingSpaceManager() {
        this.parkingSpaces = new ArrayList<>();
    }

    public void addParkingSpace(ParkingSpace space) {
        parkingSpaces.add(space);
    }

    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }
}
