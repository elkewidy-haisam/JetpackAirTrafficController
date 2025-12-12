/*
 * ParkingSpaceGenerator.java
 * Part of Jetpack Air Traffic Controller
 *
 * Generates parking spaces for jetpacks in the city grid.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
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
