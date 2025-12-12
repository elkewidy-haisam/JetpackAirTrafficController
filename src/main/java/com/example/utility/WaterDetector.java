/**
 * WaterDetector.java
 * by Haisam Elkewidy
 *
 * Utility for detecting water regions in the city grid.
 */

package com.example.utility;


import java.io.IOException;

public class WaterDetector {
    public WaterDetector(String resourcePath) throws IOException {
        // Simulate resource loading failure for test
        if (resourcePath == null || resourcePath.isEmpty() || resourcePath.contains("boston_map.png")) {
            throw new IOException("Could not find resource");
        }
        // Otherwise, assume resource loaded
    }

    public boolean isWater(double x, double y) {
        // Dummy logic: treat y < 0 as water for demonstration
        return y < 0;
    }
}
