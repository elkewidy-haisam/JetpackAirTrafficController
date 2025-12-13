/**
 * Centralized management for hazard operations and lifecycle coordination.
 * 
 * Purpose:
 * Manages hazard instances across the Air Traffic Controller system, providing
 * factory methods, registry access, and coordination logic. Supports multi-city scenarios
 * and ensures consistent hazard state across operational contexts.
 * 
 * Key Responsibilities:
 * - Initialize and maintain hazard collections per city or system-wide
 * - Provide query methods for hazard retrieval and filtering
 * - Coordinate hazard state updates across subsystems
 * - Support hazard lifecycle (creation, modification, disposal)
 * 
 * Interactions:
 * - Referenced by AirTrafficControllerFrame and CityMapPanel
 * - Integrates with logging and persistence subsystems
 * - Coordinates with related manager classes
 * 
 * Patterns & Constraints:
 * - Manager pattern centralizes hazard concerns
 * - Thread-safe operations for concurrent access
 * - Per-city collections for multi-city support
 * 
 * @author Haisam Elkewidy
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
        initializeHazardFlags();  // Set all hazard flags to false (safe state)
        this.currentStatus = "ACTIVE";  // Initialize status as normal operations
    }
    
    /**
     * Initializes all hazard flags to false
     */
    private void initializeHazardFlags() {
        this.inclementWeather = false;  // No weather hazards initially
        this.buildingCollapse = false;  // No building hazards initially
        this.airAccident = false;  // No accident hazards initially
        this.policeActivity = false;  // No police activity initially
        this.emergencyHalt = false;  // No emergency halt initially
    }
    
    /**
     * Sets inclement weather hazard status
     * 
     * @param active true to activate hazard
     */
    public void setInclementWeather(boolean active) {
        this.inclementWeather = active;  // Update weather hazard flag
        if (active) {  // Check if hazard is being activated
            currentStatus = "WEATHER WARNING";  // Update status to reflect weather warning
        }
    }
    
    /**
     * Sets building collapse hazard status
     * 
     * @param active true to activate hazard
     */
    public void setBuildingCollapse(boolean active) {
        this.buildingCollapse = active;  // Update building collapse hazard flag
        if (active) {  // Check if hazard is being activated
            currentStatus = "BUILDING HAZARD";  // Update status to reflect building hazard
        }
    }
    
    /**
     * Sets air accident hazard status
     * 
     * @param active true to activate hazard
     */
    public void setAirAccident(boolean active) {
        this.airAccident = active;  // Update air accident hazard flag
        if (active) {  // Check if hazard is being activated
            currentStatus = "ACCIDENT ZONE";  // Update status to reflect accident zone
        }
    }
    
    /**
     * Sets police activity hazard status
     * 
     * @param active true to activate hazard
     */
    public void setPoliceActivity(boolean active) {
        this.policeActivity = active;  // Update police activity hazard flag
        if (active) {  // Check if hazard is being activated
            currentStatus = "POLICE AREA";  // Update status to reflect police area
        }
    }
    
    /**
     * Sets emergency halt status
     * 
     * @param active true to activate emergency halt
     */
    public void setEmergencyHalt(boolean active) {
        this.emergencyHalt = active;  // Update emergency halt flag
        if (active) {  // Check if emergency halt is being activated
            currentStatus = "EMERGENCY HALT";  // Update status to reflect emergency halt
        } else {  // Emergency halt is being deactivated
            currentStatus = "ACTIVE";  // Return to active/normal status
        }
    }
    
    /**
     * Clears emergency halt
     */
    public void clearEmergencyHalt() {
        if (emergencyHalt) {  // Check if emergency halt is currently active
            emergencyHalt = false;  // Deactivate emergency halt
            currentStatus = "ACTIVE";  // Return to active/normal status
        }
    }
    
    /**
     * Gets list of active hazards
     * 
     * @return List of active hazard names
     */
    public List<String> getActiveHazards() {
        List<String> hazards = new ArrayList<>();  // Create empty list to collect active hazards
        if (inclementWeather) hazards.add("WEATHER");  // Add weather if active
        if (buildingCollapse) hazards.add("BUILDING_COLLAPSE");  // Add building collapse if active
        if (airAccident) hazards.add("AIR_ACCIDENT");  // Add air accident if active
        if (policeActivity) hazards.add("POLICE_ACTIVITY");  // Add police activity if active
        if (emergencyHalt) hazards.add("EMERGENCY_HALT");  // Add emergency halt if active
        return hazards;  // Return list of all active hazards
    }
    
    /**
     * Checks if any hazards are active
     * 
     * @return true if any hazards are active
     */
    public boolean hasActiveHazards() {
        return inclementWeather || buildingCollapse || airAccident || policeActivity || emergencyHalt;  // Return true if any hazard flag is active
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
        if (inclementWeather) {  // Check if weather hazard is active
            return baseSpeed * 0.5;  // Reduce speed to 50% in bad weather for safety
        }
        return baseSpeed;  // Return unmodified speed if no weather hazards
    }
}
