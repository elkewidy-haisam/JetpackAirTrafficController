/**
 * Defines the Cartesian coordinate system for airspace organization and flight tracking.
 * 
 * Purpose:
 * Establishes a two-dimensional grid structure with defined width and height, providing the spatial
 * framework for positioning jetpacks, calculating distances, and rendering geographic features. Acts
 * as the mathematical foundation for radar tracking, collision detection, and map visualization.
 * 
 * Key Responsibilities:
 * - Define airspace boundaries in grid units (width x height)
 * - Provide immutable spatial parameters for consistent calculations
 * - Support coordinate-based queries and position validation
 * - Enable grid-based rendering and display scaling
 * 
 * Interactions:
 * - Used by AirTrafficController to define controlled airspace extents
 * - Referenced by Radar for position tracking within boundaries
 * - Consumed by GridRenderer for visual representation of airspace sectors
 * - Utilized in collision detection algorithms for proximity checks
 * - Supports NavigationCalculator for route planning and distance computations
 * 
 * Patterns & Constraints:
 * - Immutable dimensions ensure thread-safe access across subsystems
 * - Simple value object with minimal logic; computation delegated to utilities
 * - No dependency on rendering or UI layers; pure spatial model
 * - Assumes origin at (0,0) with positive axes; extends to (width, height)
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class Grid {
    private final int width;
    private final int height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return String.format("Grid[width=%d, height=%d]", width, height);
    }
}
