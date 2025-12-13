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
    
    private JLabel weatherLabel;
    private JLabel dateTimeLabel;
    
    /**
     * Sets the weather label to be updated
     * 
     * @param label The weather label
     */
    public void setWeatherLabel(JLabel label) {
        this.weatherLabel = label;
    }
    
    /**
     * Sets the date/time label to be updated
     * 
     * @param label The date/time label
     */
    public void setDateTimeLabel(JLabel label) {
        this.dateTimeLabel = label;
    }
    
    /**
     * Updates the weather display label
     * 
     * @param weather The current weather object
     */
    public void updateWeatherDisplay(Weather weather) {
        if (weatherLabel != null && weather != null) {
            weatherLabel.setText("Weather: " + weather.toString());
        }
    }
    
    /**
     * Updates the date/time display label with timezone
     * 
     * @param city The current city
     * @param dayTime The current day/time object
     */
    public void updateDateTimeDisplay(String city, DayTime dayTime) {
        if (dateTimeLabel != null && city != null && dayTime != null) {
            ZoneId timezone = TimezoneHelper.getTimezoneForCity(city);
            LocalDateTime now = LocalDateTime.now(timezone);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss z");
            String timeStr = now.format(formatter);
            
            // Add day/night indicator
            String timeOfDay = dayTime.getTimeOfDay();
            String dayNight = timeOfDay.equals("Day") ? "☀️ Day" : "🌙 Night";
            dateTimeLabel.setText(dayNight + " | " + timeStr);
        }
    }
    
    /**
     * Gets the weather label
     * 
     * @return The weather label
     */
    public JLabel getWeatherLabel() {
        return weatherLabel;
    }
    
    /**
     * Gets the date/time label
     * 
     * @return The date/time label
     */
    public JLabel getDateTimeLabel() {
        return dateTimeLabel;
    }
}
