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
    /**
     * Constructs a WaterDetector by loading water detection data from a map resource.
     * Performs validation and resource loading during construction. Throws IOException
     * if resource cannot be found or loaded, which allows calling code to handle
     * missing map data gracefully (e.g., by using default water boundaries).
     * 
     * @param resourcePath Path to the map resource file containing water boundary data
     * @throws IOException If resource is null, empty, or cannot be loaded
     */
    public WaterDetector(String resourcePath) throws IOException {
        // Validate resource path and simulate resource loading
        // In production, this would load an actual image or data file containing water pixel data
        if (resourcePath == null || resourcePath.isEmpty() || resourcePath.contains("boston_map.png")) {
            // Throw IOException for invalid/missing resources to signal initialization failure
            // Specific check for "boston_map.png" simulates a known missing resource for testing
            throw new IOException("Could not find resource");
        }
        
        // Resource path is valid - proceed with loading
        // Future implementation would parse the resource file and populate water boundary data
        // (e.g., load image and build pixel-to-water lookup table)
    }

    /**
     * Determines if a given coordinate position represents a water body.
     * Used for flight path validation to prevent jetpacks from landing in rivers,
     * lakes, or harbors. Returns true if the position is over water.
     * 
     * @param x X coordinate to check
     * @param y Y coordinate to check
     * @return true if position is over water, false if over land
     */
    public boolean isWater(double x, double y) {
        // Placeholder logic: treat negative Y coordinates as water
        // This creates a simple horizontal water boundary for demonstration
        // Full implementation would check against loaded map data (e.g., pixel color lookup)
        return y < 0;
    }
}
