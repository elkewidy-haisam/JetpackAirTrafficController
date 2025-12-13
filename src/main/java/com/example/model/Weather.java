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
    private String currentWeather;
    private String currentSeverity;

    public Weather(String currentWeather, String currentSeverity) {
        this.currentWeather = currentWeather;
        this.currentSeverity = currentSeverity;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(String currentWeather) {
        this.currentWeather = currentWeather;
    }

    public String getCurrentSeverity() {
        return currentSeverity;
    }

    public void setCurrentSeverity(String currentSeverity) {
        this.currentSeverity = currentSeverity;
    }

    @Override
    public String toString() {
        return String.format("Weather[weather=%s, severity=%s]", currentWeather, currentSeverity);
    }
}
