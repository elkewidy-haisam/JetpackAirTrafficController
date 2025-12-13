/**
 * Manages time-of-day classification and provides period-specific descriptions for daylight cycle simulation.
 * 
 * Purpose:
 * Tracks the current time and categorizes it into distinct periods of the day (NIGHT, DAWN, SUNRISE, DAY,
 * SUNSET, DUSK, NIGHTTIME). Used by the UI rendering system to apply appropriate visual effects such as
 * darkening the map during night hours and brightening during daytime. Supports both real-time and
 * simulated time configurations.
 * 
 * Key Responsibilities:
 * - Classify current time into named day periods (DAWN, DAY, SUNSET, etc.)
 * - Maintain configurable time ranges for each period
 * - Provide human-readable descriptions of the current time of day
 * - Support both system time and manual time setting for testing
 * - Generate formatted time strings with emoji indicators (☀️, 🌙, etc.)
 * 
 * Interactions:
 * - Used by CityMapPanel to apply time-based shading to map display
 * - Referenced by DateTimeDisplayPanel for time-of-day indicators
 * - Supports Weather system for realistic environmental conditions
 * - Provides visual feedback through UI components
 * 
 * Patterns & Constraints:
 * - Uses Java LocalTime for time representation
 * - Immutable time range configuration after initialization
 * - Hour-based period classification (no minute granularity in ranges)
 * - Thread-safe for reads; synchronization needed if updating time frequently
 * - Simple state machine for transitioning between day periods
 * 
 * @author Haisam Elkewidy
 */

package com.example.weather;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DayTime {
    /** The current time being tracked and classified */
    private LocalTime currentTime;
    /** Current period classification (NIGHT, DAWN, SUNRISE, DAY, SUNSET, DUSK, NIGHTTIME) */
    private String timeOfDay;
    /** Maps time period names to their hour ranges [startHour, endHour] */
    private final Map<String, int[]> timeRanges;

    /**
     * Default constructor using current system time.
     * Initializes time ranges and classifies current time.
     */
    public DayTime() {
        this.currentTime = LocalTime.now();       // Get current system time
        this.timeRanges = new HashMap<>();       // Initialize empty time range map
        initializeTimeRanges();                  // Populate time ranges
        setTime();                               // Classify current time
    }

    /**
     * Constructor with specific time.
     * 
     * @param hour the hour of day (0-23)
     * @param minute the minute of hour (0-59)
     */
    public DayTime(int hour, int minute) {
        this.currentTime = LocalTime.of(hour, minute);  // Set specific time
        this.timeRanges = new HashMap<>();
        initializeTimeRanges();                  // Populate time ranges
        setTime();
    }

    /**
     * Initializes the time ranges for each period of the day.
     * Maps period names to hour ranges (start and end hours).
     */
    private void initializeTimeRanges() {
        timeRanges.put("NIGHT", new int[]{0, 5});       // Midnight to 5 AM
        timeRanges.put("DAWN", new int[]{5, 7});        // 5 AM to 7 AM
        timeRanges.put("SUNRISE", new int[]{7, 9});     // 7 AM to 9 AM
        timeRanges.put("DAY", new int[]{9, 17});        // 9 AM to 5 PM
        timeRanges.put("SUNSET", new int[]{17, 19});    // 5 PM to 7 PM
        timeRanges.put("DUSK", new int[]{19, 21});      // 7 PM to 9 PM
        timeRanges.put("NIGHTTIME", new int[]{21, 24}); // 9 PM to midnight
    }

    /**
     * Classifies the current time into a period of day.
     * Checks hour against all time ranges and sets timeOfDay.
     */
    public void setTime() {
        int hour = currentTime.getHour();  // Extract hour from current time
        if (isInRange(hour, "NIGHT")) {           // Check if in night range
            timeOfDay = "NIGHT";
        } else if (isInRange(hour, "DAWN")) {
            timeOfDay = "DAWN";
        } else if (isInRange(hour, "SUNRISE")) {
            timeOfDay = "SUNRISE";
        } else if (isInRange(hour, "DAY")) {
            timeOfDay = "DAY";
        } else if (isInRange(hour, "SUNSET")) {
            timeOfDay = "SUNSET";
        } else if (isInRange(hour, "DUSK")) {
            timeOfDay = "DUSK";
        } else if (isInRange(hour, "NIGHTTIME")) {
            timeOfDay = "NIGHTTIME";
        }
    }

    /**
     * Checks if given hour falls within specified period range.
     * 
     * @param hour the hour to check (0-23)
     * @param period the period name to check against
     * @return true if hour is in period range, false otherwise
     */
    private boolean isInRange(int hour, String period) {
        int[] range = timeRanges.get(period);  // Get start/end hours for period
        return hour >= range[0] && hour < range[1];  // Check if in range
    }

    /**
     * Updates to current system time and reclassifies.
     */
    public void updateToCurrentTime() {
        this.currentTime = LocalTime.now();  // Get fresh system time
        setTime();
    }

    /**
     * Updates to specified time and reclassifies.
     * 
     * @param hour the hour to set (0-23)
     * @param minute the minute to set (0-59)
     */
    public void updateTime(int hour, int minute) {
        this.currentTime = LocalTime.of(hour, minute);  // Set specific time
        setTime();
    }

    public int[] getColorScheme() {
        switch (timeOfDay) {
            case "NIGHT": return new int[]{10, 10, 40};
            case "DAWN": return new int[]{135, 135, 180};
            case "SUNRISE": return new int[]{255, 200, 150};
            case "DAY": return new int[]{135, 206, 250};
            case "SUNSET": return new int[]{255, 140, 100};
            case "DUSK": return new int[]{100, 80, 130};
            case "NIGHTTIME": return new int[]{30, 30, 60};
            default: return new int[]{135, 206, 250};
        }
    }

    public String getTimeOfDayDescription() {
        switch (timeOfDay) {
            case "NIGHT": return "Late night - minimal visibility";
            case "DAWN": return "Early morning - visibility improving";
            case "SUNRISE": return "Sunrise - good visibility";
            case "DAY": return "Daytime - excellent visibility";
            case "SUNSET": return "Sunset - good visibility, changing light";
            case "DUSK": return "Evening - visibility decreasing";
            case "NIGHTTIME": return "Night - reduced visibility";
            default: return "Unknown time period";
        }
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public Map<String, int[]> getTimeRanges() {
        return new HashMap<>(timeRanges);
    }

    public String getFormattedTime() {
        return String.format("%02d:%02d", currentTime.getHour(), currentTime.getMinute());
    }

    @Override
    public String toString() {
        return "DayTime{" +
                "currentTime=" + getFormattedTime() +
                ", timeOfDay='" + timeOfDay + '\'' +
                ", description='" + getTimeOfDayDescription() + '\'' +
                '}';
    }
}