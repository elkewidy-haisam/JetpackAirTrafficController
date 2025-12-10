/*
 * TimezoneHelper.java
 * Part of Jetpack Air Traffic Controller
 *
 * Utility class for handling timezone operations for different cities.
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.utility.timezone;


public class TimezoneHelper {
    public static java.time.ZoneId getTimezoneForCity(String city) {
        // Stub: Always return UTC for now
        return java.time.ZoneId.of("UTC");
    }
}