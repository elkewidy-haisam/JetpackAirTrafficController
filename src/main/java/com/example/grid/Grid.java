
/*
 * Grid.java
 * Part of Jetpack Air Traffic Controller
 *
 * Represents the grid system used for navigation and positioning within a locale.
 * Contains attributes and methods relevant to the grid's functionality and identification.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
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
