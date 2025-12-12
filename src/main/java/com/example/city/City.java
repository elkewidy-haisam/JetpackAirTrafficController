/**
 * City.java
 * by Haisam Elkewidy
 *
 * City class represents a city with its dimensions and parking spaces
 */

package com.example.city;

import java.util.ArrayList;
import java.util.List;

import com.example.parking.ParkingSpace;

/**
 * City class represents a city with its dimensions and parking spaces
 */
public class City {
    private String name;
    private int width;
    private int height;
    private List<ParkingSpace> parkingSpaces;
    
    public City(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.parkingSpaces = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }
    
    public void setParkingSpaces(List<ParkingSpace> spaces) {
        this.parkingSpaces = spaces;
    }
}
