package com.example.ui.citymap;

import java.awt.Component;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.parking.ParkingSpace;
import com.example.radio.Radio;
import com.example.ui.frames.RadarTapeWindow;
import com.example.weather.Weather;

/**
 * CityMapWeatherManager - Manages weather-related flight operations and alerts
 */
public class CityMapWeatherManager {
    private final String city;
    private final Weather currentWeather;
    private final List<JetPackFlight> jetpackFlights;
    private final Map<JetPackFlight, JetPackFlightState> flightStates;
    private final List<ParkingSpace> parkingSpaces;
    private final Radio cityRadio;
    private final RadarTapeWindow radarTapeWindow;
    private final Component parentComponent;
    private final CityMapUpdater updater;
    
    private javax.swing.Timer weatherTimer;
    private JLabel weatherLabel;
    
    public CityMapWeatherManager(String city, Weather currentWeather,
                                List<JetPackFlight> jetpackFlights,
                                Map<JetPackFlight, JetPackFlightState> flightStates,
                                List<ParkingSpace> parkingSpaces,
                                Radio cityRadio,
                                RadarTapeWindow radarTapeWindow,
                                Component parentComponent,
                                CityMapUpdater updater) {
        this.city = city;
        this.currentWeather = currentWeather;
        this.jetpackFlights = jetpackFlights;
        this.flightStates = flightStates;
        this.parkingSpaces = parkingSpaces;
        this.cityRadio = cityRadio;
        this.radarTapeWindow = radarTapeWindow;
        this.parentComponent = parentComponent;
        this.updater = updater;
    }
    
    public void setWeatherLabel(JLabel weatherLabel) {
        this.weatherLabel = weatherLabel;
    }
    
    /**
     * Starts the weather update timer
     */
    public void startWeatherTimer(Runnable weatherDisplayUpdater) {
        weatherTimer = new javax.swing.Timer(30000, e -> {
            if (currentWeather != null) {
                int oldSeverity = currentWeather.getCurrentSeverity();
                currentWeather.changeWeatherRandomly();
                int newSeverity = currentWeather.getCurrentSeverity();
                
                weatherDisplayUpdater.run();
                updater.updateWeatherBroadcast(currentWeather);
                
                // Show severe weather alert if severity is 4 or 5
                if (newSeverity >= 4 && newSeverity > oldSeverity) {
                    showSevereWeatherAlert(currentWeather.getCurrentWeather(), newSeverity);
                }
                
                // Resume flights when weather improves from severe conditions
                if (oldSeverity >= 4 && newSeverity < 4) {
                    resumeFlightsAfterWeather();
                }
            }
        });
        weatherTimer.start();
    }
    
    /**
     * Stops the weather timer
     */
    public void stopWeatherTimer() {
        if (weatherTimer != null) {
            weatherTimer.stop();
        }
    }
    
    /**
     * Updates the weather display label
     */
    public void updateWeatherDisplay() {
        if (weatherLabel != null && currentWeather != null) {
            weatherLabel.setText("Weather: " + currentWeather.getCurrentWeather() + 
                " (" + currentWeather.getCurrentSeverity() + "/5)");
        }
    }
    
    /**
     * Grounds all active flights immediately
     */
    public void groundAllFlights(String reason) {
        for (JetPackFlight flight : jetpackFlights) {
            JetPackFlightState state = flightStates.get(flight);
            if (state != null && !state.isParked() && !flight.isEmergencyHalt()) {
                flight.halt(reason);
            }
        }
        
        // Broadcast to radar
        if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
            radarTapeWindow.addMessage("🚨 ALL FLIGHTS GROUNDED - " + reason);
        }
        
        // Log to city radio
        cityRadio.broadcastToAll("EMERGENCY GROUNDING ORDER: " + reason + 
            " - ALL AIRCRAFT CEASE OPERATIONS IMMEDIATELY");
    }
    
    /**
     * Initiates emergency landing procedures for all active flights
     */
    public void initiateEmergencyLandings(String reason) {
        int landingCount = 0;
        
        for (JetPackFlight flight : jetpackFlights) {
            JetPackFlightState state = flightStates.get(flight);
            if (state != null && !state.isParked() && !flight.isEmergencyHalt() && 
                !"EMERGENCY LANDING".equals(flight.getCurrentStatus())) {
                flight.emergencyLanding(parkingSpaces, reason);
                landingCount++;
            }
        }
        
        if (landingCount > 0) {
            // Broadcast to radar
            if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
                radarTapeWindow.addMessage("⚠️ EMERGENCY LANDING INITIATED FOR " + 
                    landingCount + " FLIGHTS - " + reason);
            }
            
            // Log to city radio
            cityRadio.broadcastToAll("EMERGENCY LANDING ORDER: " + reason + 
                " - ALL AIRCRAFT PROCEED TO NEAREST PARKING IMMEDIATELY");
        }
    }
    
    /**
     * Resumes flights after severe weather has cleared
     */
    private void resumeFlightsAfterWeather() {
        int resumedCount = 0;
        
        for (JetPackFlight flight : jetpackFlights) {
            if (flight.isEmergencyHalt() && 
                flight.getCurrentStatus().contains("WEATHER")) {
                flight.clearEmergencyHalt();
                resumedCount++;
            }
        }
        
        if (resumedCount > 0) {
            if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
                radarTapeWindow.addMessage("✅ WEATHER CLEARED - " + resumedCount + 
                    " FLIGHTS RESUMING OPERATIONS");
            }
            
            cityRadio.broadcastToAll("WEATHER CONDITIONS IMPROVED - " +
                "FLIGHT OPERATIONS MAY RESUME WITH CAUTION");
        }
    }
    
    /**
     * Shows a popup alert for severe weather conditions
     */
    private void showSevereWeatherAlert(String weatherType, int severity) {
        String severityText = severity == 5 ? "CRITICAL" : "SEVERE";
        String actionTaken = severity == 5 ? 
            "ALL FLIGHTS HAVE BEEN GROUNDED!" : 
            "EMERGENCY LANDING PROCEDURES INITIATED!";
        String message = String.format(
            "⚠️ %s WEATHER ALERT ⚠️\n\n" +
            "Weather: %s\n" +
            "Severity Level: %d - %s\n\n" +
            "%s\n\n" +
            "All jetpack flights are at risk!\n\n" +
            "ACTION: %s",
            severityText, weatherType, severity, severityText,
            severity == 5 ? "IMMEDIATE GROUNDING REQUIRED!" : "Exercise extreme caution!",
            actionTaken
        );
        
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(parentComponent, message,
                severityText + " Weather Warning",
                JOptionPane.WARNING_MESSAGE);
        });
        
        // Log to radar tape
        if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
            radarTapeWindow.addMessage("🚨 " + severityText + " WEATHER: " + weatherType + 
                " (Level " + severity + ")");
        }
    }
}
