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
    // Current time representation for day/night calculations
    private LocalTime currentTime;
    
    // Descriptive period (e.g., "DAY", "NIGHT", "DAWN", "DUSK")
    private String timeOfDay;
    
    // Immutable mapping of time periods to hour ranges [start, end)
    private final Map<String, int[]> timeRanges;

    /**
     * Constructs a DayTime object initialized to current system time.
     * Automatically determines time-of-day period based on current hour.
     */
    public DayTime() {
        // Capture current system time for initialization
        this.currentTime = LocalTime.now();
        
        // Initialize time ranges map
        this.timeRanges = new HashMap<>();
        
        // Populate time period definitions
        initializeTimeRanges();
        
        // Determine current time-of-day period
        setTime();
    }

    /**
     * Constructs a DayTime object with specified hour and minute.
     * Allows simulation of specific times for testing or scenarios.
     * 
     * @param hour Hour of day (0-23)
     * @param minute Minute of hour (0-59)
     */
    public DayTime(int hour, int minute) {
        // Create time from specified hour and minute
        this.currentTime = LocalTime.of(hour, minute);
        
        // Initialize time ranges map
        this.timeRanges = new HashMap<>();
        
        // Populate time period definitions
        initializeTimeRanges();
        
        // Determine time-of-day period for specified time
        setTime();
    }

    /**
     * Initializes time period definitions with hour range boundaries.
     * Defines seven distinct periods throughout a 24-hour cycle.
     * Each period has unique color schemes and visibility characteristics.
     */
    private void initializeTimeRanges() {
        // Late night period: 0:00 - 5:00 (minimal visibility)
        timeRanges.put("NIGHT", new int[]{0, 5});
        
        // Early morning period: 5:00 - 7:00 (improving visibility)
        timeRanges.put("DAWN", new int[]{5, 7});
        
        // Morning period: 7:00 - 9:00 (good visibility)
        timeRanges.put("SUNRISE", new int[]{7, 9});
        
        // Daytime period: 9:00 - 17:00 (excellent visibility)
        timeRanges.put("DAY", new int[]{9, 17});
        
        // Evening period: 17:00 - 19:00 (good but changing visibility)
        timeRanges.put("SUNSET", new int[]{17, 19});
        
        // Twilight period: 19:00 - 21:00 (decreasing visibility)
        timeRanges.put("DUSK", new int[]{19, 21});
        
        // Night period: 21:00 - 24:00 (reduced visibility)
        timeRanges.put("NIGHTTIME", new int[]{21, 24});
    }

    /**
     * Determines and sets the time-of-day period based on current hour.
     * Evaluates hour against defined time ranges to classify period.
     * Updates timeOfDay field with appropriate period descriptor.
     */
    public void setTime() {
        // Extract hour component from current time
        int hour = currentTime.getHour();
        
        // Check each period in sequence and set timeOfDay when match found
        if (isInRange(hour, "NIGHT")) {
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
     * Checks if an hour falls within a specified time period's range.
     * Uses half-open interval [start, end) for range comparison.
     * 
     * @param hour Hour to check (0-23)
     * @param period Time period name to check against
     * @return true if hour falls within period's range
     */
    private boolean isInRange(int hour, String period) {
        // Lookup hour range for specified period
        int[] range = timeRanges.get(period);
        
        // Check if hour is within range [start, end)
        return hour >= range[0] && hour < range[1];
    }

    public void updateToCurrentTime() {
        this.currentTime = LocalTime.now();
        setTime();
    }

    public void updateTime(int hour, int minute) {
        this.currentTime = LocalTime.of(hour, minute);
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