/**
 * DayTime component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides daytime functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core daytime operations
 * - Maintain necessary state for daytime functionality
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

package com.example.weather;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DayTime {
    // Current time representation for day/time tracking
    private LocalTime currentTime;
    // Current time period classification (e.g., "DAY", "NIGHT", "DAWN")
    private String timeOfDay;
    // Map of time period names to hour ranges [start, end)
    private final Map<String, int[]> timeRanges;

    /**
     * Default constructor using current system time.
     */
    public DayTime() {
        // Initialize with current system time
        this.currentTime = LocalTime.now();
        // Create empty map for time period ranges
        this.timeRanges = new HashMap<>();
        // Populate time ranges with standard periods
        initializeTimeRanges();
        // Determine current time of day period
        setTime();
    }

    /**
     * Constructor with specific time.
     * 
     * @param hour Hour of day (0-23)
     * @param minute Minute of hour (0-59)
     */
    public DayTime(int hour, int minute) {
        // Initialize with specified hour and minute
        this.currentTime = LocalTime.of(hour, minute);
        // Create empty map for time period ranges
        this.timeRanges = new HashMap<>();
        // Populate time ranges with standard periods
        initializeTimeRanges();
        // Determine time of day period for specified time
        setTime();
    }

    /**
     * Initializes the time period ranges defining when each period occurs.
     */
    private void initializeTimeRanges() {
        // Define NIGHT period: 12:00 AM - 5:00 AM
        timeRanges.put("NIGHT", new int[]{0, 5});
        // Define DAWN period: 5:00 AM - 7:00 AM
        timeRanges.put("DAWN", new int[]{5, 7});
        // Define SUNRISE period: 7:00 AM - 9:00 AM
        timeRanges.put("SUNRISE", new int[]{7, 9});
        // Define DAY period: 9:00 AM - 5:00 PM
        timeRanges.put("DAY", new int[]{9, 17});
        // Define SUNSET period: 5:00 PM - 7:00 PM
        timeRanges.put("SUNSET", new int[]{17, 19});
        // Define DUSK period: 7:00 PM - 9:00 PM
        timeRanges.put("DUSK", new int[]{19, 21});
        // Define NIGHTTIME period: 9:00 PM - 12:00 AM
        timeRanges.put("NIGHTTIME", new int[]{21, 24});
    }

    /**
     * Determines and sets the time of day period based on current time.
     */
    public void setTime() {
        // Extract hour from current time
        int hour = currentTime.getHour();
        // Check if hour falls in NIGHT range (0-5)
        if (isInRange(hour, "NIGHT")) {
            timeOfDay = "NIGHT";
        // Check if hour falls in DAWN range (5-7)
        } else if (isInRange(hour, "DAWN")) {
            timeOfDay = "DAWN";
        // Check if hour falls in SUNRISE range (7-9)
        } else if (isInRange(hour, "SUNRISE")) {
            timeOfDay = "SUNRISE";
        // Check if hour falls in DAY range (9-17)
        } else if (isInRange(hour, "DAY")) {
            timeOfDay = "DAY";
        // Check if hour falls in SUNSET range (17-19)
        } else if (isInRange(hour, "SUNSET")) {
            timeOfDay = "SUNSET";
        // Check if hour falls in DUSK range (19-21)
        } else if (isInRange(hour, "DUSK")) {
            timeOfDay = "DUSK";
        // Check if hour falls in NIGHTTIME range (21-24)
        } else if (isInRange(hour, "NIGHTTIME")) {
            timeOfDay = "NIGHTTIME";
        }
    }

    /**
     * Checks if an hour falls within a specified time period range.
     * 
     * @param hour Hour to check (0-23)
     * @param period Time period name (e.g., "DAY", "NIGHT")
     * @return true if hour is within period range, false otherwise
     */
    private boolean isInRange(int hour, String period) {
        // Get hour range array for specified period
        int[] range = timeRanges.get(period);
        // Check if hour is >= start and < end (half-open interval)
        return hour >= range[0] && hour < range[1];
    }

    /**
     * Updates time to current system time and recalculates time of day.
     */
    public void updateToCurrentTime() {
        // Set current time to system time
        this.currentTime = LocalTime.now();
        // Recalculate time of day period
        setTime();
    }

    /**
     * Updates time to specified hour and minute and recalculates time of day.
     * 
     * @param hour Hour to set (0-23)
     * @param minute Minute to set (0-59)
     */
    public void updateTime(int hour, int minute) {
        // Set current time to specified values
        this.currentTime = LocalTime.of(hour, minute);
        // Recalculate time of day period
        setTime();
    }

    /**
     * Returns RGB color scheme array for current time of day.
     * Used for sky color rendering in visualization.
     * 
     * @return int array with RGB values [red, green, blue] (0-255)
     */
    public int[] getColorScheme() {
        // Switch on time of day period to return appropriate colors
        switch (timeOfDay) {
            // NIGHT: very dark blue (nearly black)
            case "NIGHT": return new int[]{10, 10, 40};
            // DAWN: medium gray-blue (early morning light)
            case "DAWN": return new int[]{135, 135, 180};
            // SUNRISE: warm peach/orange (sunrise glow)
            case "SUNRISE": return new int[]{255, 200, 150};
            // DAY: bright sky blue (clear day)
            case "DAY": return new int[]{135, 206, 250};
            // SUNSET: warm orange (sunset glow)
            case "SUNSET": return new int[]{255, 140, 100};
            // DUSK: purple-gray (twilight)
            case "DUSK": return new int[]{100, 80, 130};
            // NIGHTTIME: dark blue (evening sky)
            case "NIGHTTIME": return new int[]{30, 30, 60};
            // Default: bright sky blue if period unknown
            default: return new int[]{135, 206, 250};
        }
    }

    /**
     * Returns human-readable description of current time period.
     * Includes visibility information for flight safety assessment.
     * 
     * @return Descriptive string for current time of day
     */
    public String getTimeOfDayDescription() {
        // Switch on time of day period to return appropriate description
        switch (timeOfDay) {
            // NIGHT: minimal visibility warning
            case "NIGHT": return "Late night - minimal visibility";
            // DAWN: visibility improving message
            case "DAWN": return "Early morning - visibility improving";
            // SUNRISE: good visibility confirmation
            case "SUNRISE": return "Sunrise - good visibility";
            // DAY: excellent visibility confirmation
            case "DAY": return "Daytime - excellent visibility";
            // SUNSET: good visibility with changing light warning
            case "SUNSET": return "Sunset - good visibility, changing light";
            // DUSK: visibility decreasing warning
            case "DUSK": return "Evening - visibility decreasing";
            // NIGHTTIME: reduced visibility warning
            case "NIGHTTIME": return "Night - reduced visibility";
            // Default: unknown period message
            default: return "Unknown time period";
        }
    }

    /**
     * Returns the current LocalTime object.
     * 
     * @return Current time
     */
    public LocalTime getCurrentTime() {
        // Return stored current time
        return currentTime;
    }

    /**
     * Returns the current time of day period name.
     * 
     * @return Time period string (e.g., "DAY", "NIGHT")
     */
    public String getTimeOfDay() {
        // Return stored time of day classification
        return timeOfDay;
    }

    /**
     * Returns a copy of the time ranges map.
     * 
     * @return Map of period names to hour ranges
     */
    public Map<String, int[]> getTimeRanges() {
        // Return new HashMap copy to prevent external modification
        return new HashMap<>(timeRanges);
    }

    /**
     * Returns formatted time string in HH:MM format.
     * 
     * @return Time string with zero-padded hours and minutes
     */
    public String getFormattedTime() {
        // Format hour and minute with zero padding (e.g., "09:05")
        return String.format("%02d:%02d", currentTime.getHour(), currentTime.getMinute());
    }

    /**
     * Returns string representation of DayTime object.
     * 
     * @return String with time, period, and description
     */
    @Override
    public String toString() {
        // Format DayTime details as readable string
        return "DayTime{" +
                "currentTime=" + getFormattedTime() +
                ", timeOfDay='" + timeOfDay + '\'' +
                ", description='" + getTimeOfDayDescription() + '\'' +
                '}';
    }
}