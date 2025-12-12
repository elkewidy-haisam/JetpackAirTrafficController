/**
 * TimezoneHelper.java
 * by Haisam Elkewidy
 *
 * This class handles TimezoneHelper functionality in the Air Traffic Controller system.
 *
 */

package com.example.utility.timezone;


public class TimezoneHelper {
    public static java.time.ZoneId getTimezoneForCity(String city) {
        // Stub: Always return UTC for now
        return java.time.ZoneId.of("UTC");
    }
}