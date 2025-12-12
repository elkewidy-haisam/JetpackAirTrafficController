/**
 * Grid.java
 * by Haisam Elkewidy
 *
 * This class handles Grid functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - width (int)
 *   - height (int)
 *   - gridType (String)
 *   - localeName (String)
 *   - coordinateSystem (String)
 *
 * Methods:
 *   - Grid(width, height, gridType, localeName, coordinateSystem)
 *   - displayGrid()
 *   - updateGrid(newWidth, newHeight)
 *
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
