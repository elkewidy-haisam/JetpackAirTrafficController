/**
 * ParkingSpaceGenerator.java
 * by Haisam Elkewidy
 *
 * Generates parking spaces for jetpacks in the city grid.
 */

package com.example.utility;

import com.example.model.ParkingSpace;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpaceGenerator {
    public List<ParkingSpace> generateSpaces(int count) {
        List<ParkingSpace> spaces = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            spaces.add(new ParkingSpace("GEN-P" + (i+1), i * 10.0, i * 20.0));
        }
        return spaces;
    }
}
