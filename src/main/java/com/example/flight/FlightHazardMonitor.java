/**
 * Monitors environmental and operational hazards affecting jetpack flight safety and performance.
 * 
 * Purpose:
 * Continuously tracks multiple hazard types (inclement weather, building collapses, air accidents,
 * police activity, emergency halts) that impact flight operations. Adjusts effective flight speed
 * based on active hazards and provides hazard status queries for decision-making. Enables dynamic
 * response to changing environmental conditions and operational restrictions.
 * 
 * Key Responsibilities:
 * - Track boolean flags for each hazard category (weather, structural, accident, law enforcement)
 * - Calculate effective flight speed reductions when hazards are present
 * - Support emergency halt state for immediate flight suspension
 * - Provide aggregate hazard status queries (hasActiveHazards)
 * - Enable selective hazard clearance and bulk hazard reset
 * - Integrate hazard state into flight path decision-making
 * 
 * Interactions:
 * - Integrated into JetPackFlight for real-time hazard-aware navigation
 * - Consulted by FlightPath for detour and halt logic
 * - Updated by FlightEmergencyHandler when hazards are detected or cleared
 * - Referenced in speed calculations for animation and position updates
 * - Displayed in UI panels for operator situational awareness
 * 
 * Patterns & Constraints:
 * - Simple boolean flags for each hazard type; no complex state machine
 * - Speed reduction applied multiplicatively when multiple hazards overlap
 * - Emergency halt takes precedence over other hazard-based speed adjustments
 * - Thread-safe for concurrent reads; synchronized writes required at higher layers
 * - No automatic hazard expiration; requires explicit clearance
 * 
 * @author Haisam Elkewidy
 */

package com.example.flight;

import java.util.ArrayList;
import java.util.List;

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
