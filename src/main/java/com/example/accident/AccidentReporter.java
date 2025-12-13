/**
 * Generates and reports random accident incidents for simulation realism and operator training.
 * 
 * Purpose:
 * Creates simulated accident reports with random types, locations, severities, and descriptions to
 * populate the air traffic control system with realistic incident scenarios. Logs accidents to
 * city-specific files, broadcasts via radio channels, and displays in UI panels for operator
 * awareness. Supports training scenarios, system demonstrations, and incident response testing
 * without requiring actual accidents to occur.
 * 
 * Key Responsibilities:
 * - Generate random accident reports with varied types and severities
 * - Create realistic incident descriptions and timestamps
 * - Select random map locations for accident placement
 * - Log accidents to city-specific accident report files
 * - Broadcast accident notifications via Radio system
 * - Display accident information in RadioInstructionsPanel
 * - Support incident response training and system demonstrations
 * - Enable realistic traffic management scenarios
 * 
 * Interactions:
 * - Used by managers and timers for periodic random accident generation
 * - Logs to CityLogManager for city-specific accident reports
 * - Broadcasts via Radio for pilot notifications
 * - Updates RadioInstructionsPanel for operator display
 * - Coordinates with AccidentAlert for flight detour triggers
 * - May integrate with JetpackMovementPanel for movement logging
 * - Supports testing scenarios with controlled accident injection
 * 
 * Patterns & Constraints:
 * - Random accident generation using predefined type/severity/description templates
 * - Accident types: COLLISION, GROUND_ACCIDENT, JETPACK_MALFUNCTION, BIRD_STRIKE, EMERGENCY_LANDING
 * - Severity levels: MINOR, MODERATE, SEVERE, CRITICAL
 * - Random location within map boundaries (50-750 x, 50-550 y typical ranges)
 * - Formatted messages with timestamp, type, location, severity, description
 * - Logs to [city]_accident_reports_log.txt files
 * - Thread-safe Random instance for concurrent accident generation
 * - Callback-based architecture for UI updates and logging
 * 
 * @author Haisam Elkewidy
 */

package com.example.accident;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.swing.JTextArea;

import com.example.logging.CityLogManager;
import com.example.radio.Radio;

/**
 * AccidentReporter.java
 * 
 * Handles accident reporting for air traffic control.
 * Generates random accident reports and logs them appropriately.
 */
public class AccidentReporter {
    
    private static final String[] ACCIDENT_TYPES = {
        "COLLISION", "GROUND_ACCIDENT", "JETPACK_MALFUNCTION", 
        "BIRD_STRIKE", "EMERGENCY_LANDING"
    };
    
    private static final String[] SEVERITIES = {
        "MINOR", "MODERATE", "SEVERE", "CRITICAL"
    };
    
    private static final String[] DESCRIPTIONS = {
        "Multiple vehicles involved",
        "Debris on flight path",
        "Fire reported at scene",
        "Medical assistance required",
        "Traffic stopped in area",
        "Hazardous materials present",
        "Power lines down",
        "Building damage reported"
    };
    
    private Radio cityRadio;
    private String city;
    private Random random;
    private CityLogManager logManager;
    private JTextArea radioInstructionsArea;
    private JetpackMovementLogger movementLogger;
    
    /**
     * Interface for logging jetpack movement messages
     */
    public interface JetpackMovementLogger {
        void appendJetpackMovement(String message);
    }
    
    /**
     * Constructor
     * 
     * @param cityRadio The radio for the city
     * @param city The city name
     * @param logManager The log manager
     */
    public AccidentReporter(Radio cityRadio, String city, CityLogManager logManager) {
        this.cityRadio = cityRadio;
        this.city = city;
        this.logManager = logManager;
        this.random = new Random();
    }
    
    /**
     * Sets the radio instructions text area
     * 
     * @param textArea The text area for displaying accident reports
     */
    public void setRadioInstructionsArea(JTextArea textArea) {
        this.radioInstructionsArea = textArea;
    }
    
    /**
     * Sets the movement logger
     * 
     * @param logger The logger for jetpack movements
     */
    public void setMovementLogger(JetpackMovementLogger logger) {
        this.movementLogger = logger;
    }
    
    /**
     * Reports a random accident with random type, severity, and location
     */
    public void reportRandomAccident() {
        if (radioInstructionsArea == null) return;
        
        String accidentType = ACCIDENT_TYPES[random.nextInt(ACCIDENT_TYPES.length)];
        String severity = SEVERITIES[random.nextInt(SEVERITIES.length)];
        int accidentX = random.nextInt(800) + 100;
        int accidentY = random.nextInt(600) + 100;
        String accidentID = generateAccidentID();
        String description = DESCRIPTIONS[random.nextInt(DESCRIPTIONS.length)];
        
        reportAccident(accidentID, accidentX, accidentY, accidentType, severity, description);
    }
    
    /**
     * Reports a specific accident
     * 
     * @param accidentID The accident ID
     * @param x The x coordinate
     * @param y The y coordinate
     * @param accidentType The type of accident
     * @param severity The severity level
     * @param description The description
     */
    public void reportAccident(String accidentID, int x, int y, String accidentType, 
                              String severity, String description) {
        cityRadio.reportAccident(accidentID, x, y, accidentType, severity, description);
        
        String radioMsg = formatAccidentMessage(accidentID, x, y, accidentType, severity, description);
        
        if (radioInstructionsArea != null) {
            radioInstructionsArea.append(radioMsg);
            radioInstructionsArea.setCaretPosition(radioInstructionsArea.getDocument().getLength());
        }
        
        if (logManager != null) {
            logManager.writeToRadarLog(city, radioMsg);
            logManager.writeToAccidentLog(city, "\n" + radioMsg);
        }
        
        if (movementLogger != null) {
            movementLogger.appendJetpackMovement("🚨 ACCIDENT REPORTED: " + accidentType + 
                " at (" + x + "," + y + ") - Severity: " + severity);
        }
    }
    
    /**
     * Generates a unique accident ID
     * 
     * @return The accident ID
     */
    private String generateAccidentID() {
        String cityCode = city.length() >= 3 ? city.substring(0, 3).toUpperCase() : city.toUpperCase();
        return "ACC-" + cityCode + "-" + System.currentTimeMillis();
    }
    
    /**
     * Formats an accident message for logging
     * 
     * @param accidentID The accident ID
     * @param x The x coordinate
     * @param y The y coordinate
     * @param accidentType The type of accident
     * @param severity The severity level
     * @param description The description
     * @return Formatted message
     */
    private String formatAccidentMessage(String accidentID, int x, int y, 
                                        String accidentType, String severity, String description) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        return "[" + timestamp + "] *** ACCIDENT REPORT ***\n" +
               "🚨 ID: " + accidentID + "\n" +
               "Type: " + accidentType + "\n" +
               "Severity: " + severity + "\n" +
               "Location: (" + x + "," + y + ")\n" +
               "Details: " + description + "\n" +
               "⚠️ ALL AIRCRAFT AVOID AREA\n" +
               "---\n";
    }
}
