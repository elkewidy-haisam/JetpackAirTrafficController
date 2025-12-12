/**
 * Bridge3D.java
 * by Haisam Elkewidy
 *
 * This class handles Bridge3D functionality in the Air Traffic Controller system.
 *
 * Methods:
 *   - Bridge3D(x, y, length, width, angle)
 *
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
