/**
 * Manages real-time updates of weather and date/time displays with timezone-aware formatting.
 * 
 * Purpose:
 * Coordinates periodic updates of UI labels showing current weather conditions and local date/time
 * for selected cities. Integrates weather system state with timezone calculations to provide
 * accurate, city-specific displays including day/night indicators and formatted timestamps.
 * Decouples weather/time logic from UI rendering for clean separation of concerns.
 * 
 * Key Responsibilities:
 * - Update weather label with current conditions, temperature, and day/night status
 * - Update date/time label with timezone-aware local time for selected city
 * - Format weather information with emoji indicators (☀️ Day, 🌙 Night)
 * - Apply timezone conversions using TimezoneHelper for accurate local time
 * - Coordinate with Weather and DayTime objects for current state
 * - Trigger UI label repaints with formatted text
 * - Support label injection via setter methods for flexible UI binding
 * 
 * Interactions:
 * - Used by CityMapWeatherManager to update weather display on timer
 * - Integrates with Weather object for current condition queries
 * - Consults DayTime for time-of-day classification (morning/afternoon/evening/night)
 * - Uses TimezoneHelper to get city-specific timezone for accurate display
 * - Updates JLabel components in CityMapPanel or other UI containers
 * - Coordinates with AirTrafficControllerFrame for main display updates
 * - Supports multiple cities with proper timezone handling (EST, CST, PST, etc.)
 * 
 * Patterns & Constraints:
 * - Manager pattern encapsulates display update logic
 * - Label references injected via setters for flexible UI coupling
 * - Stateless updater; relies on Weather/DayTime objects for current state
 * - Thread-safe for EDT-based UI updates (Swing single-thread model)
 * - Emoji indicators: ☀️ (day), 🌙 (night) for visual clarity
 * - Date/time format: "MMM dd, yyyy HH:mm:ss z" (e.g., "Dec 13, 2025 14:30:45 EST")
 * - Null-safe: checks for null labels before updates
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
