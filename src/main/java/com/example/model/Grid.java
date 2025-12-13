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
    /** The horizontal dimension of the grid in coordinate units */
    private final int width;
    /** The vertical dimension of the grid in coordinate units */
    private final int height;

    /**
     * Constructs a new Grid with the specified dimensions.
     * @param width the horizontal dimension
     * @param height the vertical dimension
     */
    public Grid(int width, int height) {
        this.width = width;    // Store the grid width
        this.height = height;  // Store the grid height
    }

    /**
     * Returns the width of the grid.
     * @return the grid width in coordinate units
     */
    public int getWidth() {
        return width;  // Return the stored width value
    }

    /**
     * Returns the height of the grid.
     * @return the grid height in coordinate units
     */
    public int getHeight() {
        return height;  // Return the stored height value
    }

    /**
     * Returns a string representation of this grid.
     * @return formatted string showing grid dimensions
     */
    @Override
    public String toString() {
        return String.format("Grid[width=%d, height=%d]", width, height);  // Format and return grid info
    }
}
