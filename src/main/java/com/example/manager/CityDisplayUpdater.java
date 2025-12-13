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
/**
 * Manager for updating city-specific UI display components.
 * Handles weather and timezone-aware date/time label updates.
 */
public class CityDisplayUpdater {
    
    // JLabel reference for displaying weather information
    private JLabel weatherLabel;
    // JLabel reference for displaying date/time with timezone
    private JLabel dateTimeLabel;
    
    /**
     * Sets the weather label to be updated by this manager.
     * 
     * @param label The JLabel to display weather information
     */
    public void setWeatherLabel(JLabel label) {
        // Store reference to weather label for updates
        this.weatherLabel = label;
    }
    
    /**
     * Sets the date/time label to be updated by this manager.
     * 
     * @param label The JLabel to display date/time information
     */
    public void setDateTimeLabel(JLabel label) {
        // Store reference to date/time label for updates
        this.dateTimeLabel = label;
    }
    
    /**
     * Updates the weather display label with current conditions.
     * 
     * @param weather The current weather object with condition data
     */
    public void updateWeatherDisplay(Weather weather) {
        // Validate both label and weather object exist before updating
        if (weatherLabel != null && weather != null) {
            // Update label text with weather condition string
            weatherLabel.setText("Weather: " + weather.toString());
        }
    }
    
    /**
     * Updates the date/time display label with timezone-aware formatting.
     * Includes day/night indicator based on time of day.
     * 
     * @param city The current city name for timezone lookup
     * @param dayTime The current day/time object with time-of-day state
     */
    public void updateDateTimeDisplay(String city, DayTime dayTime) {
        // Validate all required parameters exist before updating
        if (dateTimeLabel != null && city != null && dayTime != null) {
            // Look up timezone for the specified city
            ZoneId timezone = TimezoneHelper.getTimezoneForCity(city);
            // Get current date/time in city's timezone
            LocalDateTime now = LocalDateTime.now(timezone);
            // Create formatter for readable date/time string (e.g., "Dec 13, 2025 01:09:48 UTC")
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss z");
            // Format current time using formatter
            String timeStr = now.format(formatter);
            
            // Determine day or night and select appropriate emoji icon
            String timeOfDay = dayTime.getTimeOfDay();
            // Use sun emoji for day, moon emoji for night
            String dayNight = timeOfDay.equals("Day") ? "☀️ Day" : "🌙 Night";
            // Update label with day/night indicator and formatted time
            dateTimeLabel.setText(dayNight + " | " + timeStr);
        }
    }
    
    /**
     * Gets the weather label reference.
     * 
     * @return The JLabel displaying weather information
     */
    public JLabel getWeatherLabel() {
        return weatherLabel;
    }
    
    /**
     * Gets the date/time label reference.
     * 
     * @return The JLabel displaying date/time information
     */
    public JLabel getDateTimeLabel() {
        return dateTimeLabel;
    }
}
