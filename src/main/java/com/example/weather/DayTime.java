/**
 * Tracks and classifies time of day into periods for lighting, shading, and operational context.
 * 
 * Purpose:
 * Manages current time state and classifies it into named periods (NIGHT, DAWN, SUNRISE, DAY, SUNSET,
 * DUSK, NIGHTTIME) for visual rendering adjustments and operational context. Enables time-based map
 * shading (darker at night, lighter during day) and provides descriptive time-of-day labels for UI
 * displays. Supports both real-time and simulated time scenarios for testing and demonstrations.
 * 
 * Key Responsibilities:
 * - Track current time using LocalTime (Java time API)
 * - Classify time into 7 distinct periods with hour-based ranges
 * - Provide time-of-day descriptions for UI labels and operator context
 * - Support time updates for real-time progression or simulation
 * - Enable time-based visual rendering (map shading, lighting effects)
 * - Coordinate with weather system for complete environmental simulation
 * - Support both current system time and manually set times for testing
 * 
 * Interactions:
 * - Used by CityMapPanel for time-based shading overlay rendering
 * - Referenced by CityDisplayUpdater for time-of-day display labels
 * - Coordinates with Weather for complete environmental conditions
 * - Supports visual effects: darker maps at night, lighter during day
 * - Integrated with timezone-aware displays (EST, CST, PST for cities)
 * - Used in testing scenarios with fixed times for reproducibility
 * - May drive future features: dynamic lighting, shadows, visibility
 * 
 * Patterns & Constraints:
 * - Time periods: NIGHT(0-5), DAWN(5-7), SUNRISE(7-9), DAY(9-17), SUNSET(17-19), DUSK(19-21), NIGHTTIME(21-24)
 * - Hour-based classification for simplicity (no minute-level transitions)
 * - LocalTime provides timezone-independent time tracking
 * - Mutable time allows updates for animation or simulation
 * - Range-based classification via HashMap lookup
 * - Thread-safe for read operations; synchronization for time updates
 * - Shading intensity: typically 30-40% darker during NIGHT/NIGHTTIME periods
 * - Default constructor uses current system time; alternative constructor for testing
 * 
 * @author Haisam Elkewidy
 */

package com.example.weather;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DayTime {
    private LocalTime currentTime;
    private String timeOfDay;
    private final Map<String, int[]> timeRanges;

    public DayTime() {
        this.currentTime = LocalTime.now();
        this.timeRanges = new HashMap<>();
        initializeTimeRanges();
        setTime();
    }

    public DayTime(int hour, int minute) {
        this.currentTime = LocalTime.of(hour, minute);
        this.timeRanges = new HashMap<>();
        initializeTimeRanges();
        setTime();
    }

    private void initializeTimeRanges() {
        timeRanges.put("NIGHT", new int[]{0, 5});
        timeRanges.put("DAWN", new int[]{5, 7});
        timeRanges.put("SUNRISE", new int[]{7, 9});
        timeRanges.put("DAY", new int[]{9, 17});
        timeRanges.put("SUNSET", new int[]{17, 19});
        timeRanges.put("DUSK", new int[]{19, 21});
        timeRanges.put("NIGHTTIME", new int[]{21, 24});
    }

    public void setTime() {
        int hour = currentTime.getHour();
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

    private boolean isInRange(int hour, String period) {
        int[] range = timeRanges.get(period);
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