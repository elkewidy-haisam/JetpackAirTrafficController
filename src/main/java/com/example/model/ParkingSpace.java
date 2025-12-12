/**
 * ParkingSpace.java
 * by Haisam Elkewidy
 *
 * This class represents a designated parking location for jetpacks within the city.
 *
 * Variables:
 *   - id (String)
 *   - x (double)
 *   - y (double)
 *   - occupied (boolean)
 *
 * Methods:
 *   - ParkingSpace(id, x, y)
 *   - ParkingSpace(id, x, y)
 *   - occupy()
 *   - vacate()
 *   - toString()
 *
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
