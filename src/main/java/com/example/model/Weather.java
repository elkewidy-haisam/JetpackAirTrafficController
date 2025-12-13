/**
 * Models current atmospheric conditions and their impact on flight safety.
 * 
 * Purpose:
 * Encapsulates weather state (e.g., Clear/Sunny, Rain, Fog, Storm) and severity classifications
 * that influence flight path approvals, emergency procedures, and pilot advisories. Provides a
 * simple yet essential data model for weather-aware decision-making in the air traffic control
 * system.
 * 
 * Key Responsibilities:
 * - Store and update current weather condition descriptors
 * - Maintain severity level indicators (e.g., Low, Moderate, High, Severe)
 * - Provide read/write access to weather attributes for subsystems
 * - Support formatted string output for logging and UI display
 * 
 * Interactions:
 * - Consumed by AirTrafficController for flight path approval logic
 * - Displayed in UI panels (WeatherBroadcastPanel) for operator awareness
 * - Referenced by FlightHazardMonitor and emergency systems
 * - Used in SessionManager for persistent state snapshots
 * - Influences radio broadcast messaging for weather advisories
 * 
 * Patterns & Constraints:
 * - Mutable value object allowing real-time weather updates
 * - Decouples weather data from complex meteorological systems
 * - No external API dependencies; suitable for simulation scenarios
 * - Minimal validation; consumers responsible for interpreting severity
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class Weather {
    // Current weather condition descriptor (e.g., "Clear/Sunny", "Rain", "Fog", "Storm")
    private String currentWeather;
    
    // Severity classification (e.g., "Low", "Moderate", "High", "Severe")
    private String currentSeverity;

    /**
     * Constructs a new Weather object with specified conditions.
     * Initializes both the weather condition and its severity level for
     * immediate use in flight safety assessments.
     * 
     * @param currentWeather Description of atmospheric conditions
     * @param currentSeverity Classification of weather severity
     */
    public Weather(String currentWeather, String currentSeverity) {
        // Store weather condition for flight safety evaluation
        this.currentWeather = currentWeather;
        
        // Store severity level to influence flight approval decisions
        this.currentSeverity = currentSeverity;
    }

    /**
     * Returns the current weather condition description.
     * Used by ATC systems and UI panels to assess and display conditions.
     * 
     * @return String describing current atmospheric conditions
     */
    public String getCurrentWeather() {
        return currentWeather;
    }

    /**
     * Updates the current weather condition.
     * Should be called when weather changes to reflect new atmospheric state.
     * Triggers re-evaluation of active flight safety and approval status.
     * 
     * @param currentWeather New weather condition description
     */
    public void setCurrentWeather(String currentWeather) {
        // Update weather state - may affect flight approvals and advisories
        this.currentWeather = currentWeather;
    }

    /**
     * Returns the current weather severity classification.
     * Used to determine if conditions warrant flight restrictions or warnings.
     * 
     * @return String describing severity level
     */
    public String getCurrentSeverity() {
        return currentSeverity;
    }

    /**
     * Updates the weather severity classification.
     * Changes to severity may trigger emergency procedures or flight restrictions.
     * 
     * @param currentSeverity New severity classification
     */
    public void setCurrentSeverity(String currentSeverity) {
        // Update severity level - may trigger alerts if threshold exceeded
        this.currentSeverity = currentSeverity;
    }

    /**
     * Returns formatted string representation of current weather state.
     * Combines condition and severity for comprehensive status display.
     * 
     * @return Human-readable weather summary
     */
    @Override
    public String toString() {
        // Format as: Weather[weather=Clear/Sunny, severity=Low]
        return String.format("Weather[weather=%s, severity=%s]", currentWeather, currentSeverity);
    }
}
