/**
 * Centralized management for instruction operations and lifecycle coordination.
 * 
 * Purpose:
 * Manages instruction instances across the Air Traffic Controller system, providing
 * factory methods, registry access, and coordination logic. Supports multi-city scenarios
 * and ensures consistent instruction state across operational contexts.
 * 
 * Key Responsibilities:
 * - Initialize and maintain instruction collections per city or system-wide
 * - Provide query methods for instruction retrieval and filtering
 * - Coordinate instruction state updates across subsystems
 * - Support instruction lifecycle (creation, modification, disposal)
 * 
 * Interactions:
 * - Referenced by AirTrafficControllerFrame and CityMapPanel
 * - Integrates with logging and persistence subsystems
 * - Coordinates with related manager classes
 * 
 * Patterns & Constraints:
 * - Manager pattern centralizes instruction concerns
 * - Thread-safe operations for concurrent access
 * - Per-city collections for multi-city support
 * 
 * @author Haisam Elkewidy
 */

package com.example.manager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.swing.JTextArea;

import com.example.accident.AccidentReporter;
import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.logging.CityLogManager;
import com.example.radio.Radio;
import com.example.weather.Weather;

/**
 * RadioInstructionManager.java
 * 
 * Manages radio instructions and communications for air traffic control.
 * Issues various types of instructions based on weather conditions,
 * traffic situations, and random events.
 */
public class RadioInstructionManager {
    
    private Radio cityRadio;
    private Weather currentWeather;
    private String city;
    private Random random;
    private CityLogManager logManager;
    private JTextArea radioInstructionsArea;
    
    /**
     * Constructor
     * 
     * @param cityRadio The radio for the city
     * @param city The city name
     * @param logManager The log manager
     */
    public RadioInstructionManager(Radio cityRadio, String city, CityLogManager logManager) {
        this.cityRadio = cityRadio;  // Store radio communication system
        this.city = city;  // Store city name for logging context
        this.logManager = logManager;  // Store log manager for instruction logging
        this.random = new Random();  // Initialize random number generator for probabilistic events
    }
    
    /**
     * Sets the current weather
     * 
     * @param weather The current weather object
     */
    public void setWeather(Weather weather) {
        this.currentWeather = weather;  // Update weather reference for instruction decision-making
    }
    
    /**
     * Sets the radio instructions text area
     * 
     * @param textArea The text area for displaying instructions
     */
    public void setRadioInstructionsArea(JTextArea textArea) {
        this.radioInstructionsArea = textArea;  // Store reference to UI component for displaying instructions
    }
    
    /**
     * Issues a random radio instruction to a random flight
     * 
     * @param jetpackFlights List of active flights
     * @param flightStates Map of flight states
     * @param accidentReporter The accident reporter for handling accidents
     */
    public void issueRandomRadioInstruction(java.util.List<JetPackFlight> jetpackFlights,
                                           java.util.Map<JetPackFlight, JetPackFlightState> flightStates,
                                           AccidentReporter accidentReporter) {
        if (jetpackFlights.isEmpty() || radioInstructionsArea == null) return;  // Exit if no flights or no UI to display
        
        JetPackFlight flight = jetpackFlights.get(random.nextInt(jetpackFlights.size()));  // Select random flight
        JetPackFlightState state = flightStates.get(flight);  // Get flight state
        
        if (state != null && state.isParked()) return;  // Skip parked flights (not actively flying)
        
        int instructionType = random.nextInt(100);  // Generate random number 0-99 for instruction type selection
        
        // 2% chance of accident report
        if (instructionType < 2 && accidentReporter != null) {  // Check for accident report (2% probability)
            accidentReporter.reportRandomAccident();  // Report a random accident
            return;  // Exit after accident report
        }
        
        String instruction;  // Declare variable for instruction text
        String action;  // Declare variable for action description
        
        if (currentWeather != null && !currentWeather.isSafeToFly()) {  // Check for critical weather conditions
            instruction = issueWeatherCriticalInstruction(flight, instructionType);  // Issue critical weather instruction
            action = getWeatherCriticalAction(instructionType);  // Get corresponding action description
        } else if (currentWeather != null && currentWeather.getCurrentSeverity() >= 3) {  // Check for moderate weather severity
            instruction = issueWeatherAdvisoryInstruction(flight, instructionType);  // Issue weather advisory
            action = getWeatherAdvisoryAction(instructionType);  // Get corresponding action description
        } else {  // Normal weather conditions
            instruction = issueRoutineInstruction(flight, instructionType);  // Issue routine instruction
            action = getRoutineAction(instructionType);  // Get corresponding action description
        }
        
        logInstruction(flight, instruction, action);  // Log the instruction to display and file
    }
    
    private String issueWeatherCriticalInstruction(JetPackFlight flight, int type) {
        if (type < 40) {
            cityRadio.issueEmergencyDirective(flight.getJetpack(), 
                "Weather conditions critical - " + currentWeather.getCurrentWeather() + 
                ". Emergency landing authorized");
            return "EMERGENCY LANDING REQUIRED";
        } else if (type < 70) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getX() + random.nextInt(200) - 100, 
                (int)flight.getY() + random.nextInt(200) - 100, 
                "Weather avoidance - " + currentWeather.getCurrentWeather());
            return "DETOUR AROUND WEATHER";
        } else {
            cityRadio.giveNewAltitude(flight.getJetpack(), 
                3000 + random.nextInt(2000), 
                "Weather avoidance");
            return "ALTITUDE CHANGE";
        }
    }
    
    private String getWeatherCriticalAction(int type) {
        if (type < 40) return "Land immediately at nearest parking";
        else if (type < 70) return "Alter course to avoid storm";
        else return "Climb to avoid weather";
    }
    
    private String issueWeatherAdvisoryInstruction(JetPackFlight flight, int type) {
        if (type < 30) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getDestination().x, (int)flight.getDestination().y, 
                "Weather deteriorating");
            return "RETURN TO BASE";
        } else if (type < 60) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getX() + random.nextInt(150) - 75, 
                (int)flight.getY() + random.nextInt(150) - 75, 
                "Precautionary route change");
            return "COURSE ADJUSTMENT";
        } else {
            cityRadio.giveNewAltitude(flight.getJetpack(), 
                2000 + random.nextInt(1500), 
                "Weather advisory");
            return "ALTITUDE ADVISORY";
        }
    }
    
    private String getWeatherAdvisoryAction(int type) {
        if (type < 30) return "Return to origin point";
        else if (type < 60) return "Detour for safety";
        else return "Adjust altitude";
    }
    
    private String issueRoutineInstruction(JetPackFlight flight, int type) {
        if (type < 20) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getX() + random.nextInt(100) - 50, 
                (int)flight.getY() + random.nextInt(100) - 50, 
                "Traffic separation");
            return "COLLISION AVOIDANCE";
        } else if (type < 40) {
            cityRadio.giveNewAltitude(flight.getJetpack(), 
                1500 + random.nextInt(2500), 
                "Routine altitude change");
            return "ALTITUDE CLEARANCE";
        } else if (type < 60) {
            cityRadio.giveNewCoordinates(flight.getJetpack(), 
                (int)flight.getDestination().x, (int)flight.getDestination().y, 
                "Direct routing");
            return "ROUTE OPTIMIZATION";
        } else if (type < 80) {
            cityRadio.requestPositionReport(flight.getJetpack());
            return "POSITION REPORT REQUEST";
        } else {
            if (currentWeather != null) {
                cityRadio.provideWeatherInfo(flight.getJetpack(), 
                    currentWeather.getCurrentWeather() + " - Visibility " + 
                    currentWeather.getVisibility() + " miles");
            }
            return "WEATHER UPDATE";
        }
    }
    
    private String getRoutineAction(int type) {
        if (type < 20) return "Traffic alert - change course";
        else if (type < 40) return "Cleared to new altitude";
        else if (type < 60) return "Direct route cleared";
        else if (type < 80) return "Report position";
        else return "Current conditions advisory";
    }
    
    private void logInstruction(JetPackFlight flight, String instruction, String action) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String radioMsg = "[" + timestamp + "] " + cityRadio.getControllerCallSign() + 
                          " → " + flight.getJetpack().getCallsign() + "\n" +
                          "📡 " + instruction + "\n" +
                          "➤ " + action + "\n" +
                          "---\n";
        
        if (radioInstructionsArea != null) {
            radioInstructionsArea.append(radioMsg);
            radioInstructionsArea.setCaretPosition(radioInstructionsArea.getDocument().getLength());
        }
        
        if (logManager != null) {
            logManager.writeToRadarLog(city, radioMsg);
        }
    }
}
