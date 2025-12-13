/**
 * Generates and reports aviation accidents with randomized or specified parameters to simulate incidents.
 * 
 * Purpose:
 * Responsible for creating realistic accident scenarios within the air traffic control system,
 * generating random accidents with appropriate types, severities, and locations, and broadcasting
 * these incidents through the radio system. Integrates logging and UI updates to ensure all
 * stakeholders are notified of hazardous conditions.
 * 
 * Key Responsibilities:
 * - Generate random accidents with realistic types (COLLISION, BIRD_STRIKE, etc.)
 * - Assign severity levels (MINOR to CRITICAL) to accidents
 * - Report accidents through the city radio communication system
 * - Update UI components with accident information
 * - Log accident reports to city-specific log files
 * - Create unique accident IDs with city-specific prefixes
 * 
 * Interactions:
 * - Uses Radio to broadcast accident alerts to all jetpacks
 * - Integrates with CityLogManager for persistent accident logging
 * - Updates RadioInstructionsPanel with accident broadcast messages
 * - Notifies JetpackMovementPanel via callback interface
 * - Works with AccidentAlert to trigger jetpack avoidance behaviors
 * 
 * Patterns & Constraints:
 * - Factory-like pattern for generating randomized accidents
 * - Dependency injection for Radio, CityLogManager, and UI components
 * - Callback interface (JetpackMovementLogger) for loose coupling with UI
 * - Consistent message formatting for logs and broadcasts
 * - Thread-safe accident ID generation using timestamp
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
