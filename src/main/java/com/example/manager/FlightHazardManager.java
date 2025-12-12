/**
 * FlightHazardManager.java
 * by Haisam Elkewidy
 *
 * FlightHazardManager.java
 */

package com.example.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * FlightHazardManager.java
 * 
 * Manages flight hazards and their effects on jetpack operations.
 * Tracks hazard states and provides status information.
 */
public class FlightHazardManager {
    
    // Hazard flags
    private boolean inclementWeather;
    private boolean buildingCollapse;
    private boolean airAccident;
    private boolean policeActivity;
    private boolean emergencyHalt;
    
    private String currentStatus;
    
    /**
     * Constructor - initializes all hazards to false
     */
    public FlightHazardManager() {
        initializeHazardFlags();
        this.currentStatus = "ACTIVE";
    }
    
    /**
     * Initializes all hazard flags to false
     */
    private void initializeHazardFlags() {
        this.inclementWeather = false;
        this.buildingCollapse = false;
        this.airAccident = false;
        this.policeActivity = false;
        this.emergencyHalt = false;
    }
    
    /**
     * Sets inclement weather hazard status
     * 
     * @param active true to activate hazard
     */
    public void setInclementWeather(boolean active) {
        this.inclementWeather = active;
        if (active) {
            currentStatus = "WEATHER WARNING";
        }
    }
    
    /**
     * Sets building collapse hazard status
     * 
     * @param active true to activate hazard
     */
    public void setBuildingCollapse(boolean active) {
        this.buildingCollapse = active;
        if (active) {
            currentStatus = "BUILDING HAZARD";
        }
    }
    
    /**
     * Sets air accident hazard status
     * 
     * @param active true to activate hazard
     */
    public void setAirAccident(boolean active) {
        this.airAccident = active;
        if (active) {
            currentStatus = "ACCIDENT ZONE";
        }
    }
    
    /**
     * Sets police activity hazard status
     * 
     * @param active true to activate hazard
     */
    public void setPoliceActivity(boolean active) {
        this.policeActivity = active;
        if (active) {
            currentStatus = "POLICE AREA";
        }
    }
    
    /**
     * Sets emergency halt status
     * 
     * @param active true to activate emergency halt
     */
    public void setEmergencyHalt(boolean active) {
        this.emergencyHalt = active;
        if (active) {
            currentStatus = "EMERGENCY HALT";
        } else {
            currentStatus = "ACTIVE";
        }
    }
    
    /**
     * Clears emergency halt
     */
    public void clearEmergencyHalt() {
        if (emergencyHalt) {
            emergencyHalt = false;
            currentStatus = "ACTIVE";
        }
    }
    
    /**
     * Gets list of active hazards
     * 
     * @return List of active hazard names
     */
    public List<String> getActiveHazards() {
        List<String> hazards = new ArrayList<>();
        if (inclementWeather) hazards.add("WEATHER");
        if (buildingCollapse) hazards.add("BUILDING_COLLAPSE");
        if (airAccident) hazards.add("AIR_ACCIDENT");
        if (policeActivity) hazards.add("POLICE_ACTIVITY");
        if (emergencyHalt) hazards.add("EMERGENCY_HALT");
        return hazards;
    }
    
    /**
     * Checks if any hazards are active
     * 
     * @return true if any hazards are active
     */
    public boolean hasActiveHazards() {
        return inclementWeather || buildingCollapse || airAccident || policeActivity || emergencyHalt;
    }
    
    /**
     * Gets the current status
     * 
     * @return Current status string
     */
    public String getCurrentStatus() {
        return currentStatus;
    }
    
    /**
     * Checks if emergency halt is active
     * 
     * @return true if emergency halt is active
     */
    public boolean isEmergencyHalt() {
        return emergencyHalt;
    }
    
    /**
     * Checks if inclement weather hazard is active
     * 
     * @return true if inclement weather is active
     */
    public boolean isInclementWeather() {
        return inclementWeather;
    }
    
    /**
     * Checks if building collapse hazard is active
     * 
     * @return true if building collapse is active
     */
    public boolean isBuildingCollapse() {
        return buildingCollapse;
    }
    
    /**
     * Checks if air accident hazard is active
     * 
     * @return true if air accident is active
     */
    public boolean isAirAccident() {
        return airAccident;
    }
    
    /**
     * Checks if police activity hazard is active
     * 
     * @return true if police activity is active
     */
    public boolean isPoliceActivity() {
        return policeActivity;
    }
    
    /**
     * Calculates effective speed multiplier based on hazards
     * 
     * @param baseSpeed The base speed
     * @return Adjusted speed accounting for hazards
     */
    public double getEffectiveSpeed(double baseSpeed) {
        if (inclementWeather) {
            return baseSpeed * 0.5; // Reduce speed in bad weather
        }
        return baseSpeed;
    }
}
