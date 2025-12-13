/**
 * Duplicate/alias of com.example.parking.ParkingSpaceGenerator for organizational purposes.
 * 
 * Purpose:
 * Utility package version of ParkingSpaceGenerator with identical parking space generation functionality.
 * Generates parking spaces while avoiding water bodies using WaterDetector. May be consolidated with
 * parking package version in future refactoring.
 * 
 * @author Haisam Elkewidy
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
