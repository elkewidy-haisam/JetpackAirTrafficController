package com.example.utility.timezone;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * TimezoneHelper.java
 * by Haisam Elkewidy
 * 
 * Utility class for handling timezone operations for different cities.
 */
public class TimezoneHelper {
    
    /**
     * Gets the timezone for a given city
     * 
     * @param city The city name
     * @return ZoneId for the city
     */
    public static ZoneId getTimezoneForCity(String city) {
        switch (city) {
            case "New York":
                return ZoneId.of("America/New_York");
            case "Boston":
                return ZoneId.of("America/New_York");
            case "Houston":
                return ZoneId.of("America/Chicago");
            case "Dallas":
                return ZoneId.of("America/Chicago");
            default:
                return ZoneId.systemDefault();
        }
    }
    
    /**
     * Gets the current hour for a city
     * 
     * @param city The city name
     * @return Current hour (0-23)
     */
    public static int getCurrentHourForCity(String city) {
        ZoneId timezone = getTimezoneForCity(city);
        LocalDateTime now = LocalDateTime.now(timezone);
        return now.getHour();
    }
}
