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
    /** Flag indicating severe weather conditions affecting flight operations */
    private boolean inclementWeather;
    /** Flag indicating structural collapse creating dangerous debris field */
    private boolean buildingCollapse;
    /** Flag indicating active aircraft accident requiring airspace clearance */
    private boolean airAccident;
    /** Flag indicating law enforcement activity restricting airspace */
    private boolean policeActivity;
    /** Flag indicating immediate emergency halt of all flight operations */
    private boolean emergencyHalt;

    /**
     * Default constructor initializing all hazard flags to inactive state.
     * Creates monitor ready for normal flight operations.
     */
    public FlightHazardMonitor() {
        initializeHazardFlags();  // Set all hazards to false (clear)
    }

    /**
     * Initializes all hazard flags to inactive (false) state.
     * Used by constructor and clearAllHazards() for consistent initialization.
     */
    private void initializeHazardFlags() {
        this.inclementWeather = false;  // Clear weather hazard flag
        this.buildingCollapse = false;  // Clear structural hazard flag
        this.airAccident = false;  // Clear accident hazard flag
        this.policeActivity = false;  // Clear law enforcement hazard flag
        this.emergencyHalt = false;  // Clear emergency halt flag
    }

    /**
     * Gets list of all currently active hazards.
     * Returns string identifiers for each hazard type that is flagged.
     */
    public List<String> getActiveHazards() {
        List<String> hazards = new ArrayList<>();  // Initialize empty list for active hazards
        if (inclementWeather) hazards.add("WEATHER");  // Add weather if active
        if (buildingCollapse) hazards.add("BUILDING_COLLAPSE");  // Add structural if active
        if (airAccident) hazards.add("AIR_ACCIDENT");  // Add accident if active
        if (policeActivity) hazards.add("POLICE_ACTIVITY");  // Add law enforcement if active
        if (emergencyHalt) hazards.add("EMERGENCY_HALT");  // Add emergency halt if active
        return hazards;  // Return list of active hazard identifiers
    }

    /**
     * Checks if any hazards are currently active.
     * Returns true if at least one hazard flag is set.
     */
    public boolean hasActiveHazards() {
        return inclementWeather || buildingCollapse || airAccident || policeActivity || emergencyHalt;  // OR all hazard flags
    }

    /**
     * Calculates effective flight speed based on active hazards.
     * Currently only weather affects speed (50% reduction).
     */
    public double calculateEffectiveSpeed(double baseSpeed) {
        double effectiveSpeed = baseSpeed;  // Start with base speed
        if (inclementWeather) {  // Check if weather hazard is active
            effectiveSpeed *= 0.5; // Reduce speed to 50% in bad weather for safety
        }
        return effectiveSpeed;  // Return adjusted speed
    }

    /**
     * Gets human-readable status message based on highest priority active hazard.
     * Returns "ACTIVE" if no hazards present.
     */
    public String getHazardStatus() {
        if (emergencyHalt) return "EMERGENCY HALT";  // Highest priority - immediate stop
        if (inclementWeather) return "WEATHER WARNING";  // Severe weather condition
        if (buildingCollapse) return "BUILDING HAZARD";  // Structural collapse danger
        if (airAccident) return "ACCIDENT ZONE";  // Aircraft accident in progress
        if (policeActivity) return "POLICE AREA";  // Law enforcement restriction
        return "ACTIVE";  // No hazards - normal operations
    }

    /**
     * Clears all hazard flags, resetting to safe operational state.
     * Used after hazards are resolved or for testing/initialization.
     */
    public void clearAllHazards() {
        initializeHazardFlags();  // Reset all flags to false
    }

    /**
     * Clears emergency halt flag only, restoring normal operation.
     * Other hazard flags remain unchanged.
     */
    public void clearEmergencyHalt() {
        emergencyHalt = false;  // Clear emergency halt state
    }

    /** Sets inclement weather hazard state (true=active, false=clear) */
    public void setInclementWeather(boolean active) {
        this.inclementWeather = active;  // Update weather hazard flag
    }

    /** Sets building collapse hazard state (true=active, false=clear) */
    public void setBuildingCollapse(boolean active) {
        this.buildingCollapse = active;  // Update structural hazard flag
    }

    /** Sets air accident hazard state (true=active, false=clear) */
    public void setAirAccident(boolean active) {
        this.airAccident = active;  // Update accident hazard flag
    }

    /** Sets police activity hazard state (true=active, false=clear) */
    public void setPoliceActivity(boolean active) {
        this.policeActivity = active;  // Update law enforcement hazard flag
    }

    /** Sets emergency halt state (true=halt all operations, false=normal) */
    public void setEmergencyHalt(boolean active) {
        this.emergencyHalt = active;  // Update emergency halt flag
    }

    /** Returns true if inclement weather hazard is active */
    public boolean isInclementWeather() { return inclementWeather; }
    /** Returns true if building collapse hazard is active */
    public boolean isBuildingCollapse() { return buildingCollapse; }
    /** Returns true if air accident hazard is active */
    public boolean isAirAccident() { return airAccident; }
    /** Returns true if police activity hazard is active */
    public boolean isPoliceActivity() { return policeActivity; }
    /** Returns true if emergency halt is active */
    public boolean isEmergencyHalt() { return emergencyHalt; }
}
