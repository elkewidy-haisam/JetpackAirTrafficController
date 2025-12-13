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
    public static java.time.ZoneId getTimezoneForCity(String city) {
        // Stub: Always return UTC for now
        return java.time.ZoneId.of("UTC");
    }
}