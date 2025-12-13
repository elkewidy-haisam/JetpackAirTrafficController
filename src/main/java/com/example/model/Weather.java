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
    /** Current weather condition description (e.g., "Clear", "Rain", "Fog", "Storm") */
    private String currentWeather;
    /** Severity level of current weather (e.g., "Low", "Moderate", "High", "Severe") */
    private String currentSeverity;

    /**
     * Constructs a new Weather object with specified conditions.
     * 
     * @param currentWeather description of the weather condition
     * @param currentSeverity severity level classification
     */
    public Weather(String currentWeather, String currentSeverity) {
        this.currentWeather = currentWeather;    // Store the weather condition
        this.currentSeverity = currentSeverity;  // Store the severity level
    }

    /**
     * Returns the current weather condition.
     * @return weather condition string
     */
    public String getCurrentWeather() {
        return currentWeather;  // Return the stored weather condition
    }

    /**
     * Updates the current weather condition.
     * @param currentWeather new weather condition description
     */
    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;  // Update the weather condition
    }

    /**
     * Returns the current weather severity level.
     * @return severity level string
     */
    public String getCurrentSeverity() {
        return currentSeverity;  // Return the stored severity level
    }

    /**
     * Updates the weather severity level.
     * @param currentSeverity new severity level classification
     */
    public void setCurrentSeverity(String currentSeverity) {
        this.currentSeverity = currentSeverity;  // Update the severity level
    }

    /**
     * Returns a string representation of this weather object.
     * @return formatted string with weather condition and severity
     */
    @Override
    public String toString() {
        return String.format("Weather[weather=%s, severity=%s]", currentWeather, currentSeverity);  // Format and return weather info
    }
}
