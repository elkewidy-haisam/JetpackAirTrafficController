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
    // Current weather condition descriptor (e.g., "Clear/Sunny", "Rain", "Fog")
    private String currentWeather;
    // Current weather severity level (e.g., "Low", "Moderate", "High", "Severe")
    private String currentSeverity;

    /**
     * Constructs a new Weather instance with specified conditions.
     * 
     * @param currentWeather Weather condition descriptor
     * @param currentSeverity Severity level indicator
     */
    public Weather(String currentWeather, String currentSeverity) {
        // Store weather condition descriptor
        this.currentWeather = currentWeather;
        // Store severity level indicator
        this.currentSeverity = currentSeverity;
    }

    /**
     * Returns the current weather condition descriptor.
     * 
     * @return Weather condition string
     */
    public String getCurrentWeather() {
        // Return stored weather condition
        return currentWeather;
    }

    /**
     * Updates the current weather condition.
     * 
     * @param currentWeather New weather condition descriptor
     */
    public void setCurrentWeather(String currentWeather) {
        // Update weather condition to new value
        this.currentWeather = currentWeather;
    }

    /**
     * Returns the current weather severity level.
     * 
     * @return Severity level string
     */
    public String getCurrentSeverity() {
        // Return stored severity level
        return currentSeverity;
    }

    /**
     * Updates the current weather severity level.
     * 
     * @param currentSeverity New severity level indicator
     */
    public void setCurrentSeverity(String currentSeverity) {
        // Update severity level to new value
        this.currentSeverity = currentSeverity;
    }

    /**
     * Returns formatted string representation of weather conditions.
     * 
     * @return String with weather and severity information
     */
    @Override
    public String toString() {
        // Format weather details as readable string for logging/display
        return String.format("Weather[weather=%s, severity=%s]", currentWeather, currentSeverity);
    }
}
