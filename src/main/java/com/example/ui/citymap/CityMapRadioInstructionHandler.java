/**
 * CityMapRadioInstructionHandler component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides citymapradioinstructionhandler functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core citymapradioinstructionhandler operations
 * - Maintain necessary state for citymapradioinstructionhandler functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.ui.citymap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.logging.CityLogManager;
import com.example.radio.Radio;
import com.example.weather.Weather;

/**
 * CityMapRadioInstructionHandler - Handles random radio instruction generation
 */
public class CityMapRadioInstructionHandler {
    private final String city;
    private final Radio cityRadio;
    private final Weather currentWeather;
    private final CityLogManager logManager;
    private final CityMapUpdater updater;
    private final Random random;
    
    public CityMapRadioInstructionHandler(String city, Radio cityRadio, Weather currentWeather,
                                         CityLogManager logManager, CityMapUpdater updater) {
        this.city = city;
        this.cityRadio = cityRadio;
        this.currentWeather = currentWeather;
        this.logManager = logManager;
        this.updater = updater;
        this.random = new Random();
    }
    
    /**
     * Issues a random radio instruction to a random active flight
     */
    public void issueRandomRadioInstruction(List<JetPackFlight> jetpackFlights, 
                                           Map<JetPackFlight, JetPackFlightState> flightStates) {
        if (jetpackFlights.isEmpty()) return;
        
        JetPackFlight flight = jetpackFlights.get(random.nextInt(jetpackFlights.size()));
        JetPackFlightState state = flightStates.get(flight);
        
        if (state != null && state.isParked()) return;
        
        String instruction;
        String action = "";
        
        int instructionType = random.nextInt(100);
        
        // 2% chance of accident report
        if (instructionType < 2) {
            reportRandomAccident();
            return;
        }
        
        // Determine instruction based on weather and random chance
        if (currentWeather != null && !currentWeather.isSafeToFly()) {
            // Critical weather - emergency instructions
            if (instructionType < 40) {
                instruction = "EMERGENCY LANDING REQUIRED";
                action = "Land immediately at nearest parking";
                cityRadio.issueEmergencyDirective(flight.getJetpack(), 
                    "Weather conditions critical - " + currentWeather.getCurrentWeather() + 
                    ". Emergency landing authorized");
            } else if (instructionType < 70) {
                instruction = "DETOUR AROUND WEATHER";
                action = "Alter course to avoid storm";
                cityRadio.giveNewCoordinates(flight.getJetpack(), 
                    (int)flight.getX() + random.nextInt(200) - 100, 
                    (int)flight.getY() + random.nextInt(200) - 100, 
                    "Weather avoidance - " + currentWeather.getCurrentWeather());
            } else {
                instruction = "ALTITUDE CHANGE";
                action = "Climb to avoid weather";
                cityRadio.giveNewAltitude(flight.getJetpack(), 
                    3000 + random.nextInt(2000), 
                    "Weather avoidance");
            }
        } else if (currentWeather != null && currentWeather.getCurrentSeverity() >= 3) {
            // Severe weather advisory
            if (instructionType < 30) {
                instruction = "RETURN TO BASE";
                action = "Return to origin point";
                cityRadio.giveNewCoordinates(flight.getJetpack(), 
                    (int)flight.getDestination().x, (int)flight.getDestination().y, 
                    "Weather deteriorating");
            } else if (instructionType < 60) {
                instruction = "COURSE ADJUSTMENT";
                action = "Detour for safety";
                cityRadio.giveNewCoordinates(flight.getJetpack(), 
                    (int)flight.getX() + random.nextInt(150) - 75, 
                    (int)flight.getY() + random.nextInt(150) - 75, 
                    "Precautionary route change");
            } else {
                instruction = "ALTITUDE ADVISORY";
                action = "Adjust altitude";
                cityRadio.giveNewAltitude(flight.getJetpack(), 
                    2000 + random.nextInt(1500), 
                    "Weather advisory");
            }
        } else {
            // Normal operations
            if (instructionType < 20) {
                instruction = "COLLISION AVOIDANCE";
                action = "Traffic alert - change course";
                cityRadio.giveNewCoordinates(flight.getJetpack(), 
                    (int)flight.getX() + random.nextInt(100) - 50, 
                    (int)flight.getY() + random.nextInt(100) - 50, 
                    "Traffic separation");
            } else if (instructionType < 40) {
                instruction = "ALTITUDE CLEARANCE";
                action = "Cleared to new altitude";
                cityRadio.giveNewAltitude(flight.getJetpack(), 
                    1500 + random.nextInt(2500), 
                    "Routine altitude change");
            } else if (instructionType < 60) {
                instruction = "ROUTE OPTIMIZATION";
                action = "Direct route cleared";
                cityRadio.giveNewCoordinates(flight.getJetpack(), 
                    (int)flight.getDestination().x, (int)flight.getDestination().y, 
                    "Direct routing");
            } else if (instructionType < 80) {
                instruction = "POSITION REPORT REQUEST";
                action = "Report position";
                cityRadio.requestPositionReport(flight.getJetpack());
            } else {
                instruction = "WEATHER UPDATE";
                action = "Current conditions advisory";
                if (currentWeather != null) {
                    cityRadio.provideWeatherInfo(flight.getJetpack(), 
                        currentWeather.getCurrentWeather() + " - Visibility " + 
                        currentWeather.getVisibility() + " miles");
                }
            }
        }
        
        // Format and display radio message
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String radioMsg = "[" + timestamp + "] " + cityRadio.getControllerCallSign() + 
                          " → " + flight.getJetpack().getCallsign() + "\n" +
                          "📡 " + instruction + "\n" +
                          "➤ " + action + "\n" +
                          "---\n";
        
        updater.appendRadioInstruction(radioMsg);
    }
    
    /**
     * Reports a random accident in the airspace
     */
    private void reportRandomAccident() {
        String[] accidentTypes = {"COLLISION", "GROUND_ACCIDENT", "JETPACK_MALFUNCTION", "BIRD_STRIKE", "EMERGENCY_LANDING"};
        String[] severities = {"MINOR", "MODERATE", "SEVERE", "CRITICAL"};
        
        String accidentType = accidentTypes[random.nextInt(accidentTypes.length)];
        String severity = severities[random.nextInt(severities.length)];
        int accidentX = random.nextInt(800) + 100;
        int accidentY = random.nextInt(600) + 100;
        String accidentID = "ACC-" + city.substring(0, 3).toUpperCase() + "-" + System.currentTimeMillis();
        
        String[] descriptions = {
            "Multiple vehicles involved",
            "Debris on flight path",
            "Fire reported at scene",
            "Medical assistance required",
            "Traffic stopped in area",
            "Hazardous materials present",
            "Power lines down",
            "Building damage reported"
        };
        String description = descriptions[random.nextInt(descriptions.length)];
        
        cityRadio.reportAccident(accidentID, accidentX, accidentY, accidentType, severity, description);
        
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String radioMsg = "[" + timestamp + "] *** ACCIDENT REPORT ***\n" +
                          "🚨 ID: " + accidentID + "\n" +
                          "Type: " + accidentType + "\n" +
                          "Severity: " + severity + "\n" +
                          "Location: (" + accidentX + "," + accidentY + ")\n" +
                          "Details: " + description + "\n" +
                          "⚠️ ALL AIRCRAFT AVOID AREA\n" +
                          "---\n";
        
        updater.appendRadioInstruction(radioMsg);
        logManager.writeToAccidentLog(city, "\n" + radioMsg);
        updater.appendJetpackMovement("🚨 ACCIDENT REPORTED: " + accidentType + " at (" + accidentX + "," + accidentY + ") - Severity: " + severity);
    }
}
