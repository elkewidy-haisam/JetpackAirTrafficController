/**
 * AirTrafficController.java
 * by Haisam Elkewidy
 *
 * This class handles AirTrafficController functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - controllerID (String)
 *   - location (String)
 *   - managedJetpacks (List<JetPack>)
 *   - radio (Radio)
 *   - radar (Radar)
 *   - weather (Weather)
 *   - accidentAlert (AccidentAlert)
 *   - grid (Grid)
 *   - dayTime (DayTime)
 *   - activeFlightPaths (List<FlightPath>)
 *   - ... and 1 more
 *
 * Methods:
 *   - AirTrafficController(controllerID, location, gridWidth, gridHeight)
 *   - AirTrafficController()
 *   - registerJetpack(jetpack, x, y, altitude)
 *   - unregisterJetpack(jetpack)
 *   - createFlightPath(jetpack, origin, destination)
 *   - updateJetpackPosition(jetpack, x, y, altitude)
 *   - reportAccident(x, y, type, severity, description)
 *   - clearAccident(accidentID)
 *   - updateWeather(newWeather)
 *   - performSystemCheck()
 *   - checkForCollisions()
 *   - emergencyShutdown()
 *   - restart()
 *   - toString()
 *
 */

package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.accident.AccidentAlert;
import com.example.detection.Radar;
import com.example.flight.FlightPath;
import com.example.grid.Grid;
import com.example.jetpack.JetPack;
import com.example.radio.Radio;
import com.example.weather.DayTime;
import com.example.weather.Weather;

/**
 * AirTrafficController.java
 * by Haisam Elkewidy
 * 
 * This class represents the overall AirTrafficController tower for the locale.
 * For now, this would be for just one given airspace but can always be expanded
 * to accommodate any location in the United States, or even worldwide.
 * 
 * NOTE: This is a non-GUI backend implementation. The main GUI application uses
 * AirTrafficControllerFrame instead. This class is useful for command-line tools,
 * testing, batch processing, or service implementations. See ARCHITECTURE_NOTES.md
 * for details on the two parallel implementations.
 * 
 * This class integrates all the components of the air traffic control system:
 * - Radio communications
 * - Radar tracking
 * - Weather monitoring
 * - Accident alerts
 * - Grid/airspace management
 * - Flight path coordination
 */
public class AirTrafficController {
    
    private final String controllerID;
    private final String location;
    private final List<JetPack> managedJetpacks;
    private final Radio radio;
    private final Radar radar;
    private final Weather weather;
    private final AccidentAlert accidentAlert;
    private final Grid grid;
    private final DayTime dayTime;
    private final List<FlightPath> activeFlightPaths;
    private boolean isOperational;
    
    /**
     * Default constructor
     */
    public AirTrafficController() {
        this.controllerID = "ATC-001";
        this.location = "Default Locale";
        this.managedJetpacks = new ArrayList<>();
        this.radio = new Radio("121.5", controllerID);
        this.radar = new Radar(controllerID + "-RADAR", 50.0, 0, 0);
        this.weather = new Weather(controllerID + "-WEATHER", "Clear/Sunny");
        this.accidentAlert = new AccidentAlert(controllerID + "-ALERT");
        this.grid = new Grid(100, 100, "Cartesian", location, "Local");
        this.dayTime = new DayTime();
        this.activeFlightPaths = new ArrayList<>();
        this.isOperational = true;
        
        // ATC initialized
    }
    
    /**
     * Parameterized constructor
     * 
     * @param controllerID Unique identifier for this ATC
     * @param location Location name
     * @param gridWidth Width of the airspace grid
     * @param gridHeight Height of the airspace grid
     */
    public AirTrafficController(String controllerID, String location, int gridWidth, int gridHeight) {
        this.controllerID = controllerID;
        this.location = location;
        this.managedJetpacks = new ArrayList<>();
        this.radio = new Radio("121.5", controllerID);
        this.radar = new Radar(controllerID + "-RADAR", 50.0, gridWidth/2, gridHeight/2);
        this.weather = new Weather(controllerID + "-WEATHER", "Clear/Sunny");
        this.accidentAlert = new AccidentAlert(controllerID + "-ALERT");
        this.grid = new Grid(gridWidth, gridHeight, "Cartesian", location, "Local");
        this.dayTime = new DayTime();
        this.activeFlightPaths = new ArrayList<>();
        this.isOperational = true;
        
        // ATC initialized
    }
    
    /**
     * Registers a jetpack with the air traffic controller
     * 
     * @param jetpack Jetpack to register
     * @param x Initial X position
     * @param y Initial Y position
     * @param altitude Initial altitude
     */
    public void registerJetpack(JetPack jetpack, int x, int y, int altitude) {
        if (!isOperational) {
            // ATC not operational
            return;
        }
        
        managedJetpacks.add(jetpack);
        radar.addJetpackToRadar(jetpack, x, y, altitude);
        radio.broadcastToAll("New aircraft " + jetpack.getCallsign() + " has entered the airspace");
        
        // Jetpack registered
    }
    
    /**
     * Unregisters a jetpack from the air traffic controller
     * 
     * @param jetpack Jetpack to unregister
     */
    public void unregisterJetpack(JetPack jetpack) {
        if (managedJetpacks.remove(jetpack)) {
            radar.removeJetpackFromRadar(jetpack);
            radio.broadcastToAll("Aircraft " + jetpack.getCallsign() + " has left the airspace");
            // Jetpack unregistered
        } else {
            // Jetpack not found
        }
    }
    
    /**
     * Creates and approves a flight path for a jetpack
     * 
     * @param jetpack Jetpack requesting flight path
     * @param origin Starting point
     * @param destination Ending point
     * @return FlightPath object if approved, null if denied
     */
    public FlightPath createFlightPath(JetPack jetpack, String origin, String destination) {
        if (!isOperational) {
            // ATC not operational
            return null;
        }
        
        if (!managedJetpacks.contains(jetpack)) {
            // Jetpack not registered
            return null;
        }
        
        // Check weather conditions
        if (!weather.isSafeToFly()) {
            radio.broadcastToAll("Flight path request denied for " + jetpack.getCallsign() + 
                               " due to unsafe weather conditions");
            // Flight path denied
            return null;
        }
        
        // Create flight path
        String pathID = controllerID + "-FP-" + (activeFlightPaths.size() + 1);
        FlightPath flightPath = new FlightPath(pathID, origin, destination);
        activeFlightPaths.add(flightPath);
        
        radio.clearForTakeoff(jetpack, origin);
        // Flight path approved
        
        return flightPath;
    }
    
    /**
     * Updates jetpack position on radar
     * 
     * @param jetpack Jetpack to update
     * @param x New X position
     * @param y New Y position
     * @param altitude New altitude
     */
    public void updateJetpackPosition(JetPack jetpack, int x, int y, int altitude) {
        if (!isOperational) {
            return;
        }
        
        radar.updateJetPackPosition(jetpack, x, y, altitude);
        
        // Check for accidents near new position
        List<AccidentAlert.Accident> nearbyAccidents = 
            accidentAlert.getAccidentsNearLocation(x, y, 20.0);
        
        if (!nearbyAccidents.isEmpty()) {
            for (AccidentAlert.Accident accident : nearbyAccidents) {
                radio.issueEmergencyDirective(jetpack, 
                    "Accident at (" + accident.getX() + "," + accident.getY() + "). Avoid area immediately!");
            }
        }
    }
    
    /**
     * Reports an accident to the system
     * 
     * @param x X coordinate of accident
     * @param y Y coordinate of accident
     * @param type Type of accident
     * @param severity Severity level
     * @param description Description
     */
    public void reportAccident(int x, int y, String type, String severity, String description) {
        String accidentID = controllerID + "-ACC-" + System.currentTimeMillis();
        accidentAlert.reportAccident(accidentID, x, y, type, severity, description);
        
        // Alert all nearby jetpacks
        List<JetPack> nearbyJetpacks = radar.getJetpacksInRadius(x, y, 30.0);
        if (!nearbyJetpacks.isEmpty()) {
            accidentAlert.alertJetpacksOfAccident(accidentID, nearbyJetpacks, 30.0);
        }
        
        // Broadcast to all aircraft
        radio.broadcastToAll("ACCIDENT REPORTED at location (" + x + "," + y + "). " + 
                           "Type: " + type + ". Severity: " + severity + ". Avoid area!");
    }
    
    /**
     * Clears an accident alert
     * 
     * @param accidentID ID of the accident to clear
     */
    public void clearAccident(String accidentID) {
        if (accidentAlert.removeAlert(accidentID)) {
            radio.broadcastToAll("Accident " + accidentID + " has been cleared. Area is now safe.");
        }
    }
    
    /**
     * Changes weather conditions
     * 
     * @param newWeather New weather condition
     */
    public void updateWeather(String newWeather) {
        weather.changeWeather(newWeather);
        
        // Broadcast new weather to all aircraft
        String weatherInfo = weather.getCurrentWeather() + ", Severity: " + 
                           weather.getCurrentSeverity() + ", Visibility: " + 
                           weather.getVisibility() + " miles";
        
        for (JetPack jetpack : managedJetpacks) {
            radio.provideWeatherInfo(jetpack, weatherInfo);
        }
        
        // If weather becomes unsafe, issue emergency landing orders
        if (!weather.isSafeToFly()) {
            radio.broadcastToAll("EMERGENCY: Weather conditions unsafe for flight. All aircraft land immediately!");
            // Emergency weather conditions
        }
    }
    
    /**
     * Performs a comprehensive system check
     * 
     * @return Status report
     */
    public String performSystemCheck() {
        StringBuilder report = new StringBuilder();
        
        report.append("\n========================================\n");
        report.append("AIR TRAFFIC CONTROLLER SYSTEM CHECK\n");
        report.append("========================================\n");
        report.append("Controller ID: ").append(controllerID).append("\n");
        report.append("Location: ").append(location).append("\n");
        report.append("Operational Status: ").append(isOperational ? "OPERATIONAL" : "DOWN").append("\n");
        report.append("\n--- TIME & ENVIRONMENT ---\n");
        report.append("Time: ").append(dayTime.getFormattedTime()).append("\n");
        report.append("Time of Day: ").append(dayTime.getTimeOfDay()).append("\n");
        report.append("\n--- WEATHER ---\n");
        report.append("Current Weather: ").append(weather.getCurrentWeather()).append("\n");
        report.append("Severity: ").append(weather.getCurrentSeverity()).append("\n");
        report.append("Safe to Fly: ").append(weather.isSafeToFly() ? "YES" : "NO").append("\n");
        report.append("\n--- RADAR ---\n");
        report.append("Radar Status: ").append(radar.isActive() ? "ACTIVE" : "INACTIVE").append("\n");
        report.append("Tracked Aircraft: ").append(radar.getTrackedJetpackCount()).append("\n");
        report.append("\n--- RADIO ---\n");
        report.append("Frequency: ").append(radio.getFrequency()).append("\n");
        report.append("Transmissions Logged: ").append(radio.getTransmissionLog().size()).append("\n");
        report.append("\n--- ACCIDENTS ---\n");
        report.append("Active Accidents: ").append(accidentAlert.getActiveAccidentCount()).append("\n");
        report.append("\n--- FLIGHT OPERATIONS ---\n");
        report.append("Managed Jetpacks: ").append(managedJetpacks.size()).append("\n");
        report.append("Active Flight Paths: ").append(activeFlightPaths.size()).append("\n");
        report.append("========================================\n");
        
        String reportStr = report.toString();
        // System check complete
        return reportStr;
    }
    
    /**
     * Performs collision detection and alerts
     */
    public void checkForCollisions() {
        List<String> collisionWarnings = radar.checkForCollisions(15.0);
        
        if (!collisionWarnings.isEmpty()) {
            // Collision warnings detected
            for (String warning : collisionWarnings) {
                // Warning logged
                radio.broadcastToAll(warning);
            }
        }
    }
    
    /**
     * Emergency shutdown of all operations
     */
    public void emergencyShutdown() {
        // Emergency shutdown initiated
        radio.broadcastToAll("EMERGENCY SHUTDOWN - ALL AIRCRAFT LAND IMMEDIATELY!");
        isOperational = false;
        radar.setActive(false);
        // ATC offline
    }
    
    /**
     * Restarts ATC operations
     */
    public void restart() {
        // Restarting ATC
        isOperational = true;
        radar.setActive(true);
        radio.broadcastToAll("ATC is now operational. Normal operations resumed.");
        // ATC online
    }
    
    // Getters
    
    public String getControllerID() {
        return controllerID;
    }
    
    public String getLocation() {
        return location;
    }
    
    public List<JetPack> getManagedJetpacks() {
        return new ArrayList<>(managedJetpacks);
    }
    
    public Radio getRadio() {
        return radio;
    }
    
    public Radar getRadar() {
        return radar;
    }
    
    public Weather getWeather() {
        return weather;
    }
    
    public AccidentAlert getAccidentAlert() {
        return accidentAlert;
    }
    
    public Grid getGrid() {
        return grid;
    }
    
    public DayTime getDayTime() {
        return dayTime;
    }
    
    public boolean isOperational() {
        return isOperational;
    }
    
    public List<FlightPath> getActiveFlightPaths() {
        return new ArrayList<>(activeFlightPaths);
    }
    
    @Override
    public String toString() {
        return "AirTrafficController{" +
                "controllerID='" + controllerID + '\'' +
                ", location='" + location + '\'' +
                ", managedJetpacks=" + managedJetpacks.size() +
                ", isOperational=" + isOperational +
                ", activeFlightPaths=" + activeFlightPaths.size() +
                '}';
    }
}
