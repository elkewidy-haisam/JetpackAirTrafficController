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

/**
 * Immutable value object representing a two-dimensional grid coordinate system.
 * Defines spatial boundaries for airspace, maps, and collision detection.
 */
public class Grid {
    // Width of the grid in discrete units (X-axis extent)
    private final int width;
    // Height of the grid in discrete units (Y-axis extent)
    private final int height;

    /**
     * Constructs a new Grid with specified dimensions.
     * Both dimensions are immutable after construction for thread safety.
     *
     * @param width Horizontal size of the grid
     * @param height Vertical size of the grid
     */
    public Grid(int width, int height) {
        // Assign immutable width (X-axis extent)
        this.width = width;
        // Assign immutable height (Y-axis extent)
        this.height = height;
    }

    /**
     * Gets the width of this grid.
     *
     * @return Grid width in units
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of this grid.
     *
     * @return Grid height in units
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a string representation of this grid.
     * Useful for debugging and logging grid dimensions.
     *
     * @return Formatted string showing width and height
     */
    @Override
    public String toString() {
        // Format as: Grid[width=X, height=Y]
        return String.format("Grid[width=%d, height=%d]", width, height);
    }
}
