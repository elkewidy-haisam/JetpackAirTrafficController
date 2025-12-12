/**
 * Road3D.java
 * by Haisam Elkewidy
 *
 * This class handles Road3D functionality in the Air Traffic Controller system.
 *
 * Methods:
 *   - Road3D(x, y, length, width, angle)
 *
 */

package com.example.model;

public class Road3D {
    public final double x, y, length, width, angle;

    public Road3D(double x, double y, double length, double width, double angle) {
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
