/**
 * CityDisplayUpdater component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides citydisplayupdater functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core citydisplayupdater operations
 * - Maintain necessary state for citydisplayupdater functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.manager;

import com.example.weather.Weather;
import com.example.weather.DayTime;
import com.example.utility.timezone.TimezoneHelper;
import javax.swing.JLabel;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * CityDisplayUpdater.java
 * 
 * Manages display updates for weather and date/time information.
 * Updates UI labels with current weather conditions and time-zone aware date/time.
 */
public class CityDisplayUpdater {
    
    // Swing label component for displaying current weather conditions
    private JLabel weatherLabel;
    
    // Swing label component for displaying current date/time with timezone
    private JLabel dateTimeLabel;
    
    /**
     * Sets the weather label that will be updated with weather information.
     * Must be called before updateWeatherDisplay() to enable weather updates.
     * 
     * @param label The JLabel component to display weather text
     */
    public void setWeatherLabel(JLabel label) {
        // Store reference to weather label for future updates
        this.weatherLabel = label;
    }
    
    /**
     * Sets the date/time label that will be updated with temporal information.
     * Must be called before updateDateTimeDisplay() to enable time updates.
     * 
     * @param label The JLabel component to display date/time text
     */
    public void setDateTimeLabel(JLabel label) {
        // Store reference to date/time label for future updates
        this.dateTimeLabel = label;
    }
    
    /**
     * Updates the weather label with current conditions from the Weather object.
     * Safely handles null label or weather by checking before update.
     * Formats weather using Weather.toString() for consistent presentation.
     * 
     * @param weather The current Weather object containing condition data
     */
    public void updateWeatherDisplay(Weather weather) {
        // Null-safety check prevents NullPointerException on uninitialized label or weather
        if (weatherLabel != null && weather != null) {
            // Set label text with "Weather: " prefix followed by weather string representation
            // Weather.toString() typically includes condition and severity information
            weatherLabel.setText("Weather: " + weather.toString());
        }
    }
    
    /**
     * Updates the date/time label with timezone-aware current time for the specified city.
     * Includes day/night indicator emoji for quick visual reference.
     * Formats time according to city's timezone using locale-specific patterns.
     * 
     * @param city The current city name for timezone lookup (e.g., "New York")
     * @param dayTime The DayTime object containing day/night status
     */
    public void updateDateTimeDisplay(String city, DayTime dayTime) {
        // Null-safety check ensures all required components are available
        if (dateTimeLabel != null && city != null && dayTime != null) {
            // Lookup city-specific timezone (e.g., America/New_York for New York)
            ZoneId timezone = TimezoneHelper.getTimezoneForCity(city);
            
            // Get current time in the city's timezone for accurate display
            LocalDateTime now = LocalDateTime.now(timezone);
            
            // Create formatter for human-readable date/time: "Jan 15, 2024 14:30:45 EST"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss z");
            
            // Format the current time using the specified pattern
            String timeStr = now.format(formatter);
            
            // Retrieve day/night status from DayTime object
            String timeOfDay = dayTime.getTimeOfDay();
            
            // Select emoji and text based on time of day for visual clarity
            // ☀️ indicates daytime operations, 🌙 indicates nighttime operations
            String dayNight = timeOfDay.equals("Day") ? "☀️ Day" : "🌙 Night";
            
            // Combine day/night indicator with formatted timestamp
            dateTimeLabel.setText(dayNight + " | " + timeStr);
        }
    }
    
    /**
     * Returns the currently configured weather label.
     * May be null if setWeatherLabel() has not been called.
     * 
     * @return The weather JLabel component, or null if not set
     */
    public JLabel getWeatherLabel() {
        // Return reference to weather label - may be null
        return weatherLabel;
    }
    
    /**
     * Returns the currently configured date/time label.
     * May be null if setDateTimeLabel() has not been called.
     * 
     * @return The date/time JLabel component, or null if not set
     */
    public JLabel getDateTimeLabel() {
        // Return reference to date/time label - may be null
        return dateTimeLabel;
    }
}
