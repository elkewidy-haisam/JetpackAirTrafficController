/**
 * Coordinates UI updates for city display components.
 * 
 * Purpose:
 * Manages synchronized updates to city map display elements including jetpack positions, parking spaces,
 * and visual overlays. Ensures UI components refresh properly when flight data changes.
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
    
    /** JLabel for displaying weather information */
    private JLabel weatherLabel;
    /** JLabel for displaying date/time with timezone */
    private JLabel dateTimeLabel;
    
    /**
     * Sets the weather label to be updated.
     * Stores reference to the label for future weather updates.
     * 
     * @param label The JLabel to display weather information
     */
    public void setWeatherLabel(JLabel label) {
        this.weatherLabel = label;  // Store weather label reference
    }
    
    /**
     * Sets the date/time label to be updated.
     * Stores reference to the label for future date/time updates.
     * 
     * @param label The JLabel to display date/time information
     */
    public void setDateTimeLabel(JLabel label) {
        this.dateTimeLabel = label;  // Store date/time label reference
    }
    
    /**
     * Updates the weather display label with current conditions.
     * Formats and displays weather using Weather object's toString().
     * 
     * @param weather The current Weather object with conditions
     */
    public void updateWeatherDisplay(Weather weather) {
        if (weatherLabel != null && weather != null) {  // Check both label and weather exist
            weatherLabel.setText("Weather: " + weather.toString());  // Update label with weather info
        }
    }
    
    /**
     * Updates the date/time display label with timezone-aware time.
     * Shows current time in city's timezone with day/night indicator.
     * 
     * @param city The current city name for timezone lookup
     * @param dayTime The current DayTime object for day/night classification
     */
    public void updateDateTimeDisplay(String city, DayTime dayTime) {
        if (dateTimeLabel != null && city != null && dayTime != null) {  // Check all parameters exist
            ZoneId timezone = TimezoneHelper.getTimezoneForCity(city);  // Get city's timezone
            LocalDateTime now = LocalDateTime.now(timezone);  // Get current time in city's timezone
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss z");  // Create formatter
            String timeStr = now.format(formatter);  // Format datetime string
            
            // Add day/night indicator with emoji
            String timeOfDay = dayTime.getTimeOfDay();  // Get period classification
            String dayNight = timeOfDay.equals("Day") ? "☀️ Day" : "🌙 Night";  // Select emoji and text
            dateTimeLabel.setText(dayNight + " | " + timeStr);  // Update label with indicator and time
        }
    }
    
    /**
     * Gets the weather label reference.
     * 
     * @return The JLabel displaying weather information
     */
    public JLabel getWeatherLabel() {
        return weatherLabel;  // Return stored weather label
    }
    
    /**
     * Gets the date/time label reference.
     * 
     * @return The JLabel displaying date/time information
     */
    public JLabel getDateTimeLabel() {
        return dateTimeLabel;  // Return stored date/time label
    }
}
