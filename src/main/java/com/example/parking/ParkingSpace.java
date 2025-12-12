/**
 * ParkingSpace.java
 * by Haisam Elkewidy
 *
 * This class handles ParkingSpace functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - id (String)
 *   - location (Point)
 *   - isOccupied (boolean)
 *
 * Methods:
 *   - ParkingSpace(id, x, y)
 *   - occupy()
 *   - vacate()
 *   - toString()
 *
 */

package com.example.parking;

import java.awt.Point;

public class ParkingSpace {
    private final String id;
    private final Point location;
    private boolean isOccupied;

    public ParkingSpace(String id, int x, int y) {
        this.id = id;
        this.location = new Point(x, y);
        this.isOccupied = false;
    }

    public String getId() { return id; }
    public Point getLocation() { return location; }
    public boolean isOccupied() { return isOccupied; }
    public int getX() { return location.x; }
    public int getY() { return location.y; }
    public void occupy() { this.isOccupied = true; }
    public void vacate() { this.isOccupied = false; }

    @Override
    public String toString() {
        return String.format("ParkingSpace[%s]: Location=%s Occupied=%b", id, location, isOccupied);
    }
}
