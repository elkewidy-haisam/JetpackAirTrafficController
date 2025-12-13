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

/**
 * Simple weather model with condition and severity.
 * Mutable value object for weather state tracking.
 */
public class Weather {
    // Current weather condition description (e.g., "Clear/Sunny", "Rain", "Fog")
    private String currentWeather;
    // Severity level description (e.g., "Low", "Moderate", "Severe")
    private String currentSeverity;

    /**
     * Constructs a new Weather with specified condition and severity.
     *
     * @param currentWeather Weather condition description
     * @param currentSeverity Severity level description
     */
    public Weather(String currentWeather, String currentSeverity) {
        // Set initial weather condition
        this.currentWeather = currentWeather;
        // Set initial severity level
        this.currentSeverity = currentSeverity;
    }

    /**
     * Gets the current weather condition.
     *
     * @return Weather condition string
     */
    public String getCurrentWeather() {
        return currentWeather;
    }

    /**
     * Sets the current weather condition.
     *
     * @param currentWeather New weather condition
     */
    public void setCurrentWeather(String currentWeather) {
        // Update weather condition
        this.currentWeather = currentWeather;
    }

    /**
     * Gets the current severity level.
     *
     * @return Severity level string
     */
    public String getCurrentSeverity() {
        return currentSeverity;
    }

    /**
     * Sets the current severity level.
     *
     * @param currentSeverity New severity level
     */
    public void setCurrentSeverity(String currentSeverity) {
        // Update severity level
        this.currentSeverity = currentSeverity;
    }

    /**
     * Returns string representation of weather state.
     *
     * @return Formatted string with weather and severity
     */
    @Override
    public String toString() {
        // Format as: Weather[weather=X, severity=Y]
        return String.format("Weather[weather=%s, severity=%s]", currentWeather, currentSeverity);
    }
}
