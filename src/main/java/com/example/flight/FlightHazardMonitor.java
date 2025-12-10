

/*
 * FlightHazardMonitor.java
 * Part of Jetpack Air Traffic Controller
 *
 * Tracks and manages flight hazards for jetpack flights.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.flight;

import java.util.ArrayList;
import java.util.List;

/**
 * FlightHazardMonitor tracks and manages flight hazards for jetpack flights.
 * It provides methods to check active hazards and calculate effective speed.
 */
public class FlightHazardMonitor {
    private boolean inclementWeather;
    private boolean buildingCollapse;
    private boolean airAccident;
    private boolean policeActivity;
    private boolean emergencyHalt;

    public FlightHazardMonitor() {
        initializeHazardFlags();
    }

    private void initializeHazardFlags() {
        this.inclementWeather = false;
        this.buildingCollapse = false;
        this.airAccident = false;
        this.policeActivity = false;
        this.emergencyHalt = false;
    }

    /**
     * Gets list of all active hazards
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
     * Checks if any hazards are currently active
     */
    public boolean hasActiveHazards() {
        return inclementWeather || buildingCollapse || airAccident || policeActivity || emergencyHalt;
    }

    /**
     * Calculates effective speed based on hazards
     */
    public double calculateEffectiveSpeed(double baseSpeed) {
        double effectiveSpeed = baseSpeed;
        if (inclementWeather) {
            effectiveSpeed *= 0.5; // Reduce speed in bad weather
        }
        return effectiveSpeed;
    }

    /**
     * Gets current status based on active hazards
     */
    public String getHazardStatus() {
        if (emergencyHalt) return "EMERGENCY HALT";
        if (inclementWeather) return "WEATHER WARNING";
        if (buildingCollapse) return "BUILDING HAZARD";
        if (airAccident) return "ACCIDENT ZONE";
        if (policeActivity) return "POLICE AREA";
        return "ACTIVE";
    }

    /**
     * Clears all hazard flags
     */
    public void clearAllHazards() {
        initializeHazardFlags();
    }

    /**
     * Clears emergency halt and restores normal operation
     */
    public void clearEmergencyHalt() {
        emergencyHalt = false;
    }

    // Individual hazard setters
    public void setInclementWeather(boolean active) {
        this.inclementWeather = active;
    }

    public void setBuildingCollapse(boolean active) {
        this.buildingCollapse = active;
    }

    public void setAirAccident(boolean active) {
        this.airAccident = active;
    }

    public void setPoliceActivity(boolean active) {
        this.policeActivity = active;
    }

    public void setEmergencyHalt(boolean active) {
        this.emergencyHalt = active;
    }

    // Getters
    public boolean isInclementWeather() { return inclementWeather; }
    public boolean isBuildingCollapse() { return buildingCollapse; }
    public boolean isAirAccident() { return airAccident; }
    public boolean isPoliceActivity() { return policeActivity; }
    public boolean isEmergencyHalt() { return emergencyHalt; }
}
