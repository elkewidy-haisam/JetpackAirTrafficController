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
    private int width;
    private int height;
    private String gridType;
    private String localeName;
    private String coordinateSystem;

    public Grid(int width, int height, String gridType, String localeName, String coordinateSystem) {
        this.width = width;
        this.height = height;
        this.gridType = gridType;
        this.localeName = localeName;
        this.coordinateSystem = coordinateSystem;
    }

    public void displayGrid() {
        // Display grid
    }

    public void updateGrid(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
    }

    public void setCoordinateSystem(String newCoordinateSystem) {
        this.coordinateSystem = newCoordinateSystem;
    }

    public String getGridInfo() {
        return String.format("Grid[%s, %dx%d, %s, %s]", gridType, width, height, localeName, coordinateSystem);
    }
}
