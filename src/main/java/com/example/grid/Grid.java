package com.example.grid;
/**
*  Grid.java 
*  by Haisam Elkewidy
* 
*   This class represents the grid system used for navigation and positioning within a locale.
*   It contains attributes and methods relevant to the grid's functionality as well as identification.
*
*   Attributes for identification:
*   - int width: The width of the grid.
*   - int height: The height of the grid.
*   - String gridType: The type of grid (e.g., Cartesian, Polar).
*   - String localeName: The name of the locale where the grid is used.
*   - String coordinateSystem: The coordinate system used (e.g., GPS, Local).
*
*    Functionality methods:
*   - void displayGrid(): Displays the grid layout.
*   - void updateGrid(int newWidth, int newHeight): Updates the grid dimensions.
*   - void setCoordinateSystem(String newCoordinateSystem): Sets a new coordinate system.
*   - void getGridInfo(): Retrieves information about the grid.
*
 */
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

    public void getGridInfo() {
        // Return grid info
    }
    
}
