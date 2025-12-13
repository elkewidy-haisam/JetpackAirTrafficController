/**
 * Duplicate/alias of com.example.utility.TimezoneHelper providing timezone utilities.
 * 
 * Purpose:
 * Subpackage version of TimezoneHelper with identical functionality. Maps cities to timezones and provides
 * datetime formatting methods. May be consolidated with parent package version in future refactoring.
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility.timezone;


public class TimezoneHelper {
    /**
     * Returns the timezone ZoneId for a specified city.
     * Currently returns UTC for all cities (stub implementation).
     * Future enhancement: Map cities to actual timezones (EST for New York, CST for Dallas, etc.)
     * 
     * @param city the city name to get timezone for
     * @return ZoneId for the city's timezone (currently always UTC)
     */
    public static java.time.ZoneId getTimezoneForCity(String city) {
        // Stub implementation: Always return UTC regardless of city
        // TODO: Implement city-to-timezone mapping (NY->EST, Dallas->CST, etc.)
        return java.time.ZoneId.of("UTC");  // Return UTC ZoneId
    }
}