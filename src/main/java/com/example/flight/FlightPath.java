/**
 * Defines an approved route from origin to destination with waypoint navigation and hazard tracking.
 * 
 * Purpose:
 * Models an ATC-approved flight plan containing origin, destination, and intermediate waypoints.
 * Supports dynamic route modifications via detours when hazards (weather, accidents, police activity,
 * building collapses) are detected. Maintains hazard state flags and emergency halt conditions to
 * enable safe flight operations under changing environmental conditions.
 * 
 * Key Responsibilities:
 * - Store flight plan components (pathID, origin, destination, waypoints)
 * - Maintain ordered waypoint list for sequential navigation
 * - Track active hazards by type (weather, accident, police, building collapse)
 * - Support detour activation with temporary waypoint substitution
 * - Enable flight halt with reason tracking for operational holds
 * - Resume normal path after hazard clearance or detour completion
 * - Provide hazard status queries for safety decision-making
 * 
 * Interactions:
 * - Created by AirTrafficController during flight plan approval
 * - Consumed by JetPackFlight for navigation and waypoint sequencing
 * - Modified by FlightHazardMonitor when hazards require route changes
 * - Referenced in radio communications for clearances and advisories
 * - Used in session snapshots for flight plan persistence
 * 
 * Patterns & Constraints:
 * - Mutable route: supports dynamic waypoint and detour modifications
 * - Immutable identity: pathID, origin, destination fixed after creation
 * - Maintains both normal and detour waypoint lists for efficient path switching
 * - Boolean flags for hazard types enable multi-hazard scenarios
 * - Emergency halt state separate from detour state for flexible control
 * 
 * @author Haisam Elkewidy
 */

package com.example.flight;
import java.util.ArrayList;
import java.util.List;

/**
 * FlightPath.java
 * 
 *  by Haisam Elkewidy
 * 
 *  This class represents a predefined flight path that a jetpack can follow.
 *  It contains attributes and methods relevant to the flight path's functionality as well as identification.
 *  
 *  NOTE: This class is used by AirTrafficController for non-GUI/backend operations.
 *  For the GUI application (AirTrafficControllerFrame), flight path logic is integrated
 *  into the JetPackFlight inner class. See ARCHITECTURE_NOTES.md for details.
 *  
 *  Attributes for identification:
 *  - String pathID: Unique identifier for the flight path.
 *  - String origin: Starting point of the flight path.
 *  - String destination: Ending point of the flight path.
 *  
 *  Methods for functionality:
 *  - setFlightPath() - sets the flight path for a jetpack
 *  - getFlightPath() - gets the flight path for a jetpack
 *  - detour() - alters the flightpath by adding a detour in the event of a weather hazard
 *  - halt() - puts the flight path to a complete stop and causes the jetpack to do an emergency landing
 * 
 *  
 */
public class FlightPath {
    
    // Identification attributes
    private String pathID;
    private String origin;
    private String destination;
    
    // Route information
    private List<String> waypoints;
    private List<String> detourWaypoints;
    private boolean isDetourActive;
    
    // Hazard flags
    private boolean inclementWeather;
    private boolean buildingCollapse;
    private boolean airAccident;
    private boolean policeActivity;
    private boolean emergencyHalt;
    
    // Status attributes
    private boolean isActive;
    private String currentStatus;
    
    /**
     * Default constructor creating an empty, inactive flight path.
     */
    public FlightPath() {
        // Initialize path ID to empty string
        this.pathID = "";
        // Initialize origin to empty string
        this.origin = "";
        // Initialize destination to empty string
        this.destination = "";
        // Create empty waypoint list
        this.waypoints = new ArrayList<>();
        // Create empty detour waypoint list
        this.detourWaypoints = new ArrayList<>();
        // Set detour inactive by default
        this.isDetourActive = false;
        // Set path inactive by default
        this.isActive = false;
        // Set initial status to inactive
        this.currentStatus = "INACTIVE";
        // Initialize all hazard flags to false
        initializeHazardFlags();
    }
    
    /**
     * Parameterized constructor
     * 
     * @param pathID Unique identifier for the flight path
     * @param origin Starting point of the flight path
     * @param destination Ending point of the flight path
     */
    public FlightPath(String pathID, String origin, String destination) {
        this.pathID = pathID;
        this.origin = origin;
        this.destination = destination;
        this.waypoints = new ArrayList<>();
        this.detourWaypoints = new ArrayList<>();
        this.isDetourActive = false;
        this.isActive = false;
        this.currentStatus = "READY";
        initializeHazardFlags();
    }
    
    /**
     * Parameterized constructor with waypoints
     * 
     * @param pathID Unique identifier for the flight path
     * @param origin Starting point of the flight path
     * @param destination Ending point of the flight path
     * @param waypoints List of waypoints along the route
     */
    public FlightPath(String pathID, String origin, String destination, List<String> waypoints) {
        this.pathID = pathID;
        this.origin = origin;
        this.destination = destination;
        this.waypoints = new ArrayList<>(waypoints);
        this.detourWaypoints = new ArrayList<>();
        this.isDetourActive = false;
        this.isActive = false;
        this.currentStatus = "READY";
        initializeHazardFlags();
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
     * Sets the flight path for a jetpack with origin and destination
     * 
     * @param origin Starting point of the flight path
     * @param destination Ending point of the flight path
     */
    public void setFlightPath(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
        this.waypoints.clear();
        this.isActive = true;
        this.currentStatus = "ACTIVE";
        // System.out.println("Flight path set from " + origin + " to " + destination);
    }
    
    /**
     * Sets the flight path for a jetpack with origin, destination, and waypoints
     * 
     * @param origin Starting point of the flight path
     * @param destination Ending point of the flight path
     * @param waypoints List of waypoints along the route
     */
    public void setFlightPath(String origin, String destination, List<String> waypoints) {
        this.origin = origin;
        this.destination = destination;
        this.waypoints = new ArrayList<>(waypoints);
        this.isActive = true;
        this.currentStatus = "ACTIVE";
        // System.out.println("Flight path set from " + origin + " to " + destination + 
        //                  " with " + waypoints.size() + " waypoints");
    }
    
    /**
     * Gets the current flight path information
     * 
     * @return String representation of the flight path
     */
    public String getFlightPath() {
        StringBuilder path = new StringBuilder();
        path.append("Flight Path ID: ").append(pathID).append("\n");
        path.append("Origin: ").append(origin).append("\n");
        path.append("Destination: ").append(destination).append("\n");
        path.append("Status: ").append(currentStatus).append("\n");
        
        if (isDetourActive) {
            path.append("DETOUR ACTIVE\n");
            path.append("Detour Waypoints: ").append(detourWaypoints).append("\n");
        } else if (!waypoints.isEmpty()) {
            path.append("Waypoints: ").append(waypoints).append("\n");
        }
        
        // Include active hazards
        List<String> activeHazards = getActiveHazards();
        if (!activeHazards.isEmpty()) {
            path.append("Active Hazards: ").append(activeHazards).append("\n");
        }
        
        return path.toString();
    }
    
    /**
     * Alters the flight path by adding a detour in the event of a hazard
     * 
     * @param detourPoints List of waypoints for the detour route
     * @param hazardType Type of hazard causing the detour
     */
    public void detour(List<String> detourPoints, String hazardType) {
        if (detourPoints == null || detourPoints.isEmpty()) {
            // System.out.println("ERROR: Cannot create detour with no waypoints");
            return;
        }
        
        this.detourWaypoints = new ArrayList<>(detourPoints);
        this.isDetourActive = true;
        this.currentStatus = "DETOUR";
        
        // System.out.println("DETOUR ACTIVATED due to: " + hazardType);
        // System.out.println("Original route diverted. New waypoints: " + detourWaypoints);
    }
    
    /**
     * Alters the flight path by adding a detour for weather hazards
     * 
     * @param detourPoints List of waypoints for the detour route
     */
    public void detour(List<String> detourPoints) {
        detour(detourPoints, "GENERAL HAZARD");
    }
    
    /**
     * Puts the flight path to a complete stop and causes the jetpack to do an emergency landing
     * 
     * @param reason Reason for the emergency halt
     */
    public void halt(String reason) {
        this.emergencyHalt = true;
        this.isActive = false;
        this.currentStatus = "EMERGENCY HALT";
        
        // System.out.println("!!! EMERGENCY HALT !!!");
        // System.out.println("Reason: " + reason);
        // System.out.println("Initiating emergency landing procedures...");
        // System.out.println("Flight path " + pathID + " terminated.");
    }
    
    /**
     * Resumes normal flight path after detour is cleared
     */
    public void resumeNormalPath() {
        if (isDetourActive) {
            this.isDetourActive = false;
            this.detourWaypoints.clear();
            this.currentStatus = "ACTIVE";
            // System.out.println("Detour cleared. Resuming normal flight path.");
        }
    }
    
    /**
     * Clears the emergency halt status
     */
    public void clearEmergencyHalt() {
        if (emergencyHalt) {
            this.emergencyHalt = false;
            this.currentStatus = "READY";
            // System.out.println("Emergency halt cleared. Flight path ready for activation.");
        }
    }
    
    /**
     * Gets a list of currently active hazards
     * 
     * @return List of active hazard names
     */
    public List<String> getActiveHazards() {
        List<String> hazards = new ArrayList<>();
        if (inclementWeather) hazards.add("INCLEMENT WEATHER");
        if (buildingCollapse) hazards.add("BUILDING COLLAPSE");
        if (airAccident) hazards.add("AIR ACCIDENT");
        if (policeActivity) hazards.add("POLICE ACTIVITY");
        if (emergencyHalt) hazards.add("EMERGENCY HALT");
        return hazards;
    }
    
    /**
     * Checks if any hazards are currently active
     * 
     * @return true if any hazard is active, false otherwise
     */
    public boolean hasActiveHazards() {
        return inclementWeather || buildingCollapse || airAccident || policeActivity || emergencyHalt;
    }
    
    // Getters and Setters for all attributes
    
    public String getPathID() {
        return pathID;
    }
    
    public void setPathID(String pathID) {
        this.pathID = pathID;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public List<String> getWaypoints() {
        return new ArrayList<>(waypoints);
    }
    
    public void setWaypoints(List<String> waypoints) {
        this.waypoints = new ArrayList<>(waypoints);
    }
    
    public void addWaypoint(String waypoint) {
        this.waypoints.add(waypoint);
    }
    
    public boolean isDetourActive() {
        return isDetourActive;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
        if (active) {
            this.currentStatus = "ACTIVE";
        } else {
            this.currentStatus = "INACTIVE";
        }
    }
    
    public String getCurrentStatus() {
        return currentStatus;
    }
    
    // Hazard flag getters and setters
    
    public boolean isInclementWeather() {
        return inclementWeather;
    }
    
    public void setInclementWeather(boolean inclementWeather) {
        this.inclementWeather = inclementWeather;
    }
    
    public boolean isBuildingCollapse() {
        return buildingCollapse;
    }
    
    public void setBuildingCollapse(boolean buildingCollapse) {
        this.buildingCollapse = buildingCollapse;
    }
    
    public boolean isAirAccident() {
        return airAccident;
    }
    
    public void setAirAccident(boolean airAccident) {
        this.airAccident = airAccident;
    }
    
    public boolean isPoliceActivity() {
        return policeActivity;
    }
    
    public void setPoliceActivity(boolean policeActivity) {
        this.policeActivity = policeActivity;
    }
    
    public boolean isEmergencyHalt() {
        return emergencyHalt;
    }
    
    @Override
    public String toString() {
        return "FlightPath{" +
                "pathID='" + pathID + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", status='" + currentStatus + '\'' +
                ", isActive=" + isActive +
                ", isDetourActive=" + isDetourActive +
                ", hasHazards=" + hasActiveHazards() +
                '}';
    }
}
