/*
 * Bridge3D.java
 * Represents a 3D bridge in the city model.
 * Stores position, dimensions, and orientation.
 *
 * Last updated: December 10, 2025
 * Author: Jetpack Air Traffic Controller Team
 */
package com.example.model;

public class Bridge3D {
    public final double x, y, length, width, angle;

    public Bridge3D(double x, double y, double length, double width, double angle) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.angle = angle;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getLength() { return length; }
    public double getWidth() { return width; }
}
