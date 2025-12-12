/**
 * TimezoneHelper.java
 * by Haisam Elkewidy
 *
 * Utility class for handling timezone operations for different cities.
 */

package com.example.utility.timezone;


public class TimezoneHelper {
    public static java.time.ZoneId getTimezoneForCity(String city) {
        // Stub: Always return UTC for now
        return java.time.ZoneId.of("UTC");
    }
}