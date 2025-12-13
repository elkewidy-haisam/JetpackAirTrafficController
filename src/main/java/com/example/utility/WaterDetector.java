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
        /**
     * Test stub constructor that simulates resource loading.
     * @param resourcePath the resource path (checked for test conditions)
     * @throws IOException if resource path triggers simulated failure
     */
    public WaterDetector(String resourcePath) throws IOException {
        // Simulate resource loading failure for test
                // Simulate resource loading failure for testing
        if (resourcePath == null || resourcePath.isEmpty() || resourcePath.contains("boston_map.png")) {
                        throw new IOException("Could not find resource");  // Simulated error
        }
                // Otherwise, assume resource loaded successfully
    }

        /**
     * Dummy water detection for testing.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if considered water (y < 0 in this stub)
     */
    public boolean isWater(double x, double y) {
                // Dummy logic: treat y < 0 as water for demonstration purposes
                return y < 0;  // Simple test condition
    }
}
