/**
 * ParkingSpaceManager.java
 * by Haisam Elkewidy
 *
 * Manages parking spaces for jetpacks in the city map panel.
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
