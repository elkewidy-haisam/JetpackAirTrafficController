/**
 * Grid.java
 * by Haisam Elkewidy
 *
 * This class represents the airspace grid system for organizing and tracking flight zones.
 *
 * Variables:
 *   - width (int)
 *   - height (int)
 *
 * Methods:
 *   - Grid(width, height)
 *   - toString()
 *
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
