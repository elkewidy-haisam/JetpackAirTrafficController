/**
 * Weather.java
 * by Haisam Elkewidy
 *
 * This class represents weather conditions with varying severity levels that affect flight operations.
 *
 * Variables:
 *   - currentWeather (String)
 *   - currentSeverity (String)
 *
 * Methods:
 *   - Weather(currentWeather, currentSeverity)
 *   - toString()
 *
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
