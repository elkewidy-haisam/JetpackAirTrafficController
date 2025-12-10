package com.example.weather;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * DayTime.java
 * by Haisam Elkewidy
 * 
 * This class reveals the date and time on the air traffic controller,
 * thus setting the UI to either day, night, sunset, dusk.
 * 
 * Known properties:
 * - Map for different times of the day and their corresponding hours
 * 
 * Known methods:
 * - setTime() - this sets the UI display to different time of the day based on the current datetime,
 *               which is then compared to the values in the different times of the day property
 */
public class DayTime {
    
    private LocalTime currentTime;
    private String timeOfDay;
    private Map<String, int[]> timeRanges;
    
    /**
     * Constructor - initializes time ranges for different periods of the day
     */
    public DayTime() {
        this.currentTime = LocalTime.now();
        this.timeRanges = new HashMap<>();
        initializeTimeRanges();
        setTime();
    }
    
    /**
     * Parameterized constructor with specific time
     * 
     * @param hour Hour of the day (0-23)
     * @param minute Minute of the hour (0-59)
     */
    public DayTime(int hour, int minute) {
        this.currentTime = LocalTime.of(hour, minute);
        this.timeRanges = new HashMap<>();
        initializeTimeRanges();
        setTime();
    }
    
    /**
     * Initializes the time ranges for different periods of the day
     * Format: {startHour, endHour}
     */
    private void initializeTimeRanges() {
        timeRanges.put("NIGHT", new int[]{0, 5});          // 12:00 AM - 5:59 AM
        timeRanges.put("DAWN", new int[]{5, 7});           // 5:00 AM - 6:59 AM
        timeRanges.put("SUNRISE", new int[]{7, 9});        // 7:00 AM - 8:59 AM
        timeRanges.put("DAY", new int[]{9, 17});           // 9:00 AM - 4:59 PM
        timeRanges.put("SUNSET", new int[]{17, 19});       // 5:00 PM - 6:59 PM
        timeRanges.put("DUSK", new int[]{19, 21});         // 7:00 PM - 8:59 PM
        timeRanges.put("NIGHTTIME", new int[]{21, 24});    // 9:00 PM - 11:59 PM
    }
    
    /**
     * Sets the UI display to different time of the day based on the current datetime
     * Compares current time to the time ranges and determines the time of day
     */
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
        
        // Time of day updated
    }
    
    /**
     * Checks if the given hour is within the specified time range
     * 
     * @param hour Current hour
     * @param period Time period to check
     * @return true if hour is in range, false otherwise
     */
    private boolean isInRange(int hour, String period) {
        int[] range = timeRanges.get(period);
        return hour >= range[0] && hour < range[1];
    }
    
    /**
     * Updates the current time to now
     */
    public void updateToCurrentTime() {
        this.currentTime = LocalTime.now();
        setTime();
    }
    
    /**
     * Updates the current time to a specific time
     * 
     * @param hour Hour of the day (0-23)
     * @param minute Minute of the hour (0-59)
     */
    public void updateTime(int hour, int minute) {
        this.currentTime = LocalTime.of(hour, minute);
        setTime();
    }
    
    /**
     * Gets the color scheme for the current time of day
     * Returns RGB values as an array [R, G, B]
     * 
     * @return int array with RGB values
     */
    public int[] getColorScheme() {
        switch (timeOfDay) {
            case "NIGHT":
                return new int[]{10, 10, 40};          // Dark blue/black
            case "DAWN":
                return new int[]{135, 135, 180};       // Light purple/blue
            case "SUNRISE":
                return new int[]{255, 200, 150};       // Orange/pink
            case "DAY":
                return new int[]{135, 206, 250};       // Light sky blue
            case "SUNSET":
                return new int[]{255, 140, 100};       // Orange/red
            case "DUSK":
                return new int[]{100, 80, 130};        // Purple/violet
            case "NIGHTTIME":
                return new int[]{30, 30, 60};          // Dark blue
            default:
                return new int[]{135, 206, 250};       // Default to day
        }
    }
    
    /**
     * Gets a description of the current time of day
     * 
     * @return String description
     */
    public String getTimeOfDayDescription() {
        switch (timeOfDay) {
            case "NIGHT":
                return "Late night - minimal visibility";
            case "DAWN":
                return "Early morning - visibility improving";
            case "SUNRISE":
                return "Sunrise - good visibility";
            case "DAY":
                return "Daytime - excellent visibility";
            case "SUNSET":
                return "Sunset - good visibility, changing light";
            case "DUSK":
                return "Evening - visibility decreasing";
            case "NIGHTTIME":
                return "Night - reduced visibility";
            default:
                return "Unknown time period";
        }
    }
    
    // Getters
    
    public LocalTime getCurrentTime() {
        return currentTime;
    }
    
    public String getTimeOfDay() {
        return timeOfDay;
    }
    
    public Map<String, int[]> getTimeRanges() {
        return new HashMap<>(timeRanges);
    }
    
    /**
     * Gets formatted time string
     * 
     * @return Formatted time string (HH:MM)
     */
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
