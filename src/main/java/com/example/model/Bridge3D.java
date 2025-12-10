package com.example.model;

public class Bridge3D {
        public double getX() { return x; }
        public double getY() { return y; }
        public double getLength() { return length; }
        public double getWidth() { return width; }
    public final double x, y, length, width, angle;
    public Bridge3D(double x, double y, double length, double width, double angle) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.angle = angle;
    }
}
