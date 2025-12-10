/*
 * House3D.java
 * Represents a 3D house in the city model.
 * Stores position, dimensions, and height.
 *
 * Last updated: December 10, 2025
 * Author: Jetpack Air Traffic Controller Team
 */
package com.example.model;

public class House3D {
    public final double x, y, width, depth, height;

    public House3D(double x, double y, double width, double depth, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.depth = depth;
        this.height = height;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getLength() { return depth; }
    public double getHeight() { return height; }
}
