/**
 * Test stub for water detection functionality.
 * 
 * Purpose:
 * Simplified test implementation of WaterDetector for unit testing purposes. Provides minimal water
 * detection logic (y < 0 treated as water) and simulates resource loading failures for testing error
 * handling paths. The full implementation is in com.example.utility.water.WaterDetector.
 * 
 * Key Responsibilities:
 * - Provide dummy water detection for testing
 * - Simulate resource loading failures
 * - Support unit test scenarios
 * 
 * Interactions:
 * - Used in unit tests for water detection features
 * - Allows testing without full map image resources
 * 
 * Patterns & Constraints:
 * - Test stub/mock implementation
 * - Intentionally simplified logic
 * - Not for production use
 * 
 * @author Haisam Elkewidy
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
