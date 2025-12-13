/**
 * Grid component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides grid functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core grid operations
 * - Maintain necessary state for grid functionality
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

package com.example.grid;

public class Grid {
    // Horizontal extent of grid in coordinate units
    private int width;
    // Vertical extent of grid in coordinate units
    private int height;
    // Type classification of grid (e.g., "Cartesian", "Polar")
    private String gridType;
    // Geographic locale name for this grid (e.g., city name)
    private String localeName;
    // Coordinate system identifier (e.g., "Local", "UTM", "LatLong")
    private String coordinateSystem;

    /**
     * Constructs a new Grid with specified dimensions and properties.
     * 
     * @param width Horizontal extent in coordinate units
     * @param height Vertical extent in coordinate units
     * @param gridType Type of grid system (e.g., "Cartesian")
     * @param localeName Geographic locale name
     * @param coordinateSystem Coordinate system identifier
     */
    public Grid(int width, int height, String gridType, String localeName, String coordinateSystem) {
        // Store horizontal boundary of grid
        this.width = width;
        // Store vertical boundary of grid
        this.height = height;
        // Store grid type for rendering/calculation methods
        this.gridType = gridType;
        // Store locale name for identification
        this.localeName = localeName;
        // Store coordinate system for spatial transformations
        this.coordinateSystem = coordinateSystem;
    }

    /**
     * Displays the grid (stub for future visualization).
     */
    public void displayGrid() {
        // Stub method - will display grid when visualization implemented
    }

    /**
     * Updates the grid dimensions dynamically.
     * 
     * @param newWidth New horizontal extent
     * @param newHeight New vertical extent
     */
    public void updateGrid(int newWidth, int newHeight) {
        // Update horizontal boundary to new width
        this.width = newWidth;
        // Update vertical boundary to new height
        this.height = newHeight;
    }

    /**
     * Changes the coordinate system for this grid.
     * 
     * @param newCoordinateSystem New coordinate system identifier
     */
    public void setCoordinateSystem(String newCoordinateSystem) {
        // Update coordinate system to new value
        this.coordinateSystem = newCoordinateSystem;
    }

    /**
     * Returns formatted string with grid configuration details.
     * 
     * @return String containing grid type, dimensions, locale, and coordinate system
     */
    public String getGridInfo() {
        // Format and return grid configuration as readable string
        return String.format("Grid[%s, %dx%d, %s, %s]", gridType, width, height, localeName, coordinateSystem);
    }
}
