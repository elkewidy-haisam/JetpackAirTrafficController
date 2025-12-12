/**
 * CityMapUpdater.java
 * by Haisam Elkewidy
 *
 * This class handles CityMapUpdater functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - city (String)
 *   - logManager (CityLogManager)
 *   - weatherBroadcastArea (JTextArea)
 *   - jetpackMovementArea (JTextArea)
 *   - radioInstructionsArea (JTextArea)
 *
 * Methods:
 *   - CityMapUpdater(city, logManager, weatherBroadcastArea, jetpackMovementArea, radioInstructionsArea)
 *   - updateWeatherBroadcast(currentWeather)
 *   - appendJetpackMovement(message)
 *   - appendRadioInstruction(message)
 *
 */

package com.example.ui.citymap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JTextArea;

import com.example.logging.CityLogManager;
import com.example.weather.Weather;

/**
 * CityMapUpdater - Handles all UI text area updates for the city map panel
 */
public class CityMapUpdater {
    private final String city;
    private final CityLogManager logManager;
    private final JTextArea weatherBroadcastArea;
    private final JTextArea jetpackMovementArea;
    private final JTextArea radioInstructionsArea;
    
    public CityMapUpdater(String city, CityLogManager logManager,
                         JTextArea weatherBroadcastArea, JTextArea jetpackMovementArea,
                         JTextArea radioInstructionsArea) {
        this.city = city;
        this.logManager = logManager;
        this.weatherBroadcastArea = weatherBroadcastArea;
        this.jetpackMovementArea = jetpackMovementArea;
        this.radioInstructionsArea = radioInstructionsArea;
    }
    
    /**
     * Updates the weather broadcast text area with current weather information
     */
    public void updateWeatherBroadcast(Weather currentWeather) {
        if (weatherBroadcastArea != null && currentWeather != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("═══ WEATHER BROADCAST ═══\n\n");
            sb.append("Location: ").append(city).append("\n");
            sb.append("Current: ").append(currentWeather.getCurrentWeather()).append("\n");
            sb.append("Severity: ").append(currentWeather.getCurrentSeverity()).append("/5\n");
            sb.append("Temp: ").append(String.format("%.1f", currentWeather.getTemperature())).append("°F\n");
            sb.append("Wind: ").append(currentWeather.getWindSpeed()).append(" mph\n");
            sb.append("Visibility: ").append(currentWeather.getVisibility()).append(" miles\n\n");
            sb.append("Flight Status:\n");
            if (currentWeather.isSafeToFly()) {
                sb.append("✓ SAFE TO FLY");
            } else {
                sb.append("✗ RESTRICTIONS IN PLACE");
            }
            weatherBroadcastArea.setText(sb.toString());
            
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            logManager.writeToWeatherLog(city, "\n[" + timestamp + "] " + city);
            logManager.writeToWeatherLog(city, sb.toString().replace("═", "="));
        }
    }
    
    /**
     * Appends a message to the jetpack movement log with filtering support
     */
    @SuppressWarnings("unchecked")
    public void appendJetpackMovement(String message) {
        if (jetpackMovementArea != null) {
            LocalDateTime now = LocalDateTime.now();
            String timestamp = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String logMessage = "[" + timestamp + "] " + message;
            
            // Check if filtering is enabled
            Object fullLogObj = jetpackMovementArea.getClientProperty("fullLog");
            Object applyFilterObj = jetpackMovementArea.getClientProperty("applyFilter");
            
            if (fullLogObj instanceof java.util.List && applyFilterObj instanceof Runnable) {
                // Add to full log and apply filter
                java.util.List<String> fullLog = (java.util.List<String>) fullLogObj;
                fullLog.add(logMessage);
                ((Runnable) applyFilterObj).run();
            } else {
                // Fallback: direct append without filtering
                jetpackMovementArea.append(logMessage + "\n");
                jetpackMovementArea.setCaretPosition(jetpackMovementArea.getDocument().getLength());
            }
            
            logManager.writeToJetpackLog(city, logMessage);
        }
    }
    
    /**
     * Appends a message to the radio instructions text area
     */
    public void appendRadioInstruction(String message) {
        if (radioInstructionsArea != null) {
            radioInstructionsArea.append(message);
            radioInstructionsArea.setCaretPosition(radioInstructionsArea.getDocument().getLength());
            
            logManager.writeToRadarLog(city, message);
        }
    }
}
