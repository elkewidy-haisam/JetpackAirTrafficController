/**
 * ParkingSpace.java
 * by Haisam Elkewidy
 *
 * Represents a parking space for jetpacks in the city grid.
 */

package com.example.model;

public class ParkingSpace {
    private final String id;
    private final double x;
    private final double y;
    private boolean occupied;

    public ParkingSpace(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.occupied = false;
    }

    public ParkingSpace(String id, int x, int y) {
        this(id, (double)x, (double)y);
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void occupy() {
        this.occupied = true;
    }

    public void vacate() {
        this.occupied = false;
    }

    @Override
    public String toString() {
        return String.format("ParkingSpace[id=%s, x=%.3f, y=%.3f, occupied=%s]", id, x, y, occupied);
    }
}
