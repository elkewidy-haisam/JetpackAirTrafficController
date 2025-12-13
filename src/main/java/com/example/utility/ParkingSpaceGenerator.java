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
    /**
     * Generates a list of parking spaces with sequential IDs and positions.
     * Creates parking spaces in a simple grid pattern for testing and prototyping.
     * Production version should use WaterDetector to avoid water bodies.
     * 
     * @param count the number of parking spaces to generate
     * @return List of generated ParkingSpace objects with unique IDs and positions
     */
    public List<ParkingSpace> generateSpaces(int count) {
        List<ParkingSpace> spaces = new ArrayList<>();  // Create empty list to hold parking spaces
        for (int i = 0; i < count; i++) {  // Loop to create requested number of spaces
            // Create parking space with ID "GEN-P1", "GEN-P2", etc. at calculated positions
            spaces.add(new ParkingSpace("GEN-P" + (i+1),  // Generate ID with prefix and sequential number
                    i * 10.0,   // X position: space horizontally by 10 units
                    i * 20.0)); // Y position: space vertically by 20 units
        }
        return spaces;  // Return the populated list of parking spaces
    }
}
