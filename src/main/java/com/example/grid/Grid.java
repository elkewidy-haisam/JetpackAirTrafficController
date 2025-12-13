/**
 * Represents a geographic reference grid system for mapping and coordinate representation.
 * 
 * Purpose:
 * Models a spatial grid structure with dimensions, type classification, and coordinate system information.
 * Used to define the reference frame for mapping city layouts and positioning jetpacks within
 * a standardized coordinate space. Supports different grid types and coordinate systems for flexibility.
 * 
 * Key Responsibilities:
 * - Store grid dimensions (width, height) defining the coordinate space
 * - Maintain grid type classification (Cartesian, Polar, UTM, etc.)
 * - Track locale name for geographic context
 * - Manage coordinate system specification
 * - Support dynamic grid resizing and coordinate system changes
 * 
 * Interactions:
 * - Referenced by GridRenderer for visual grid overlay rendering
 * - Used by CityMapPanel to define mapping coordinate space
 * - Provides coordinate system context for position calculations
 * - Supports conversion between different grid representations
 * 
 * Patterns & Constraints:
 * - Simple data model with getters and configuration methods
 * - Mutable dimensions to support dynamic grid updates
 * - String-based type and coordinate system for flexibility
 * - No validation logic - assumes valid inputs from caller
 * - Thread-safety not guaranteed - external synchronization required
 * 
 * @author Haisam Elkewidy
 */

package com.example.grid;

public class Grid {
    /** Width of the grid in coordinate units */
    private int width;
    
    /** Height of the grid in coordinate units */
    private int height;
    
    /** Type classification of the grid (e.g., "Cartesian", "Polar", "UTM") */
    private String gridType;
    
    /** Geographic locale name that this grid represents */
    private String localeName;
    
    /** Coordinate system specification used by this grid */
    private String coordinateSystem;

    /**
     * Constructs a new Grid with specified parameters.
     * 
     * @param width the horizontal dimension of the grid
     * @param height the vertical dimension of the grid
     * @param gridType the classification/type of grid system
     * @param localeName the geographic location name
     * @param coordinateSystem the coordinate system specification
     */
    public Grid(int width, int height, String gridType, String localeName, String coordinateSystem) {
        this.width = width;                           // Set the grid width
        this.height = height;                         // Set the grid height
        this.gridType = gridType;                     // Set the grid type classification
        this.localeName = localeName;                 // Set the locale name
        this.coordinateSystem = coordinateSystem;     // Set the coordinate system
    }

    /**
     * Displays the grid visualization.
     * Currently a placeholder method for future grid rendering implementation.
     */
    public void displayGrid() {
        // Display grid - implementation pending
    }

    /**
     * Updates the dimensions of the grid.
     * 
     * @param newWidth the new width to set for the grid
     * @param newHeight the new height to set for the grid
     */
    public void updateGrid(int newWidth, int newHeight) {
        this.width = newWidth;      // Update the grid width to new value
        this.height = newHeight;    // Update the grid height to new value
    }

    /**
     * Changes the coordinate system specification for this grid.
     * 
     * @param newCoordinateSystem the new coordinate system to use
     */
    public void setCoordinateSystem(String newCoordinateSystem) {
        this.coordinateSystem = newCoordinateSystem;  // Update coordinate system
    }

    /**
     * Returns a formatted string with all grid information.
     * 
     * @return formatted string containing grid type, dimensions, locale, and coordinate system
     */
    public String getGridInfo() {
        // Format and return comprehensive grid information
        return String.format("Grid[%s, %dx%d, %s, %s]", gridType, width, height, localeName, coordinateSystem);
    }
}
