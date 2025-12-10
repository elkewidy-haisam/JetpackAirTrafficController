package com.example.parking;

/**
 * ParkingSpace.java
 * by Haisam Elkewidy
 * 
 * This class represents a parking space for a jetpack.
 * 
 * Attributes:
 * - String id: Unique identifier for the parking space
 * - boolean occupied: Indicates if the parking space is currently occupied
 * - double x, y: Location coordinates
 * 
 * Methods:
 * - boolean isOccupied(): Returns true if the space is occupied
 * - void occupy(): Marks the space as occupied
 * - void vacate(): Marks the space as vacant
 */
public class ParkingSpace {

    private String id;
    private boolean occupied;
    private double x;
    private double y;

    public ParkingSpace(String id) {
        this.id = id;
        this.occupied = false;
        this.x = 0;
        this.y = 0;
    }

    public ParkingSpace(String id, double x, double y) {
        this.id = id;
        this.occupied = false;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    
}
