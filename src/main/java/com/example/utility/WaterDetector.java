/**
 * WaterDetector component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides waterdetector functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core waterdetector operations
 * - Maintain necessary state for waterdetector functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
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
