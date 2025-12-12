/**
 * ParkingSpaceManager.java
 * by Haisam Elkewidy
 *
 * This class handles ParkingSpaceManager functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - parkingSpaces (List<ParkingSpace>)
 *
 * Methods:
 *   - ParkingSpaceManager()
 *   - addParkingSpace(space)
 *
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
