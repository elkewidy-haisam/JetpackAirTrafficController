/**
 * Provides timezone-related utility functions for city-specific time conversions and formatting.
 * 
 * Purpose:
 * Maps cities to their respective timezones and provides methods for formatting date/time values in
 * specific timezone contexts. Supports the multi-city air traffic control system where each city operates
 * in its own timezone (New York: America/New_York, Dallas: America/Chicago, etc.). Used primarily for
 * displaying local times in the UI and applying time-based visual effects.
 * 
 * Key Responsibilities:
 * - Map city names to ZoneId timezone identifiers
 * - Convert ZonedDateTime to city-specific local times
 * - Format datetime values with timezone information
 * - Provide current hour in city-specific timezone for day/night effects
 * - Support timezone-aware datetime calculations
 * 
 * Interactions:
 * - Used by DateTimeDisplayPanel to show city-local time
 * - Referenced by CityMapPanel for time-based shading effects
 * - Integrates with DayTime for period classification
 * - Supports AirTrafficControllerFrame for UI time display
 * 
 * Patterns & Constraints:
 * - Pure utility class with only static methods (stateless)
 * - Thread-safe due to lack of mutable state
 * - Uses Java 8+ java.time API (ZonedDateTime, ZoneId)
 * - City-to-timezone mapping hardcoded for supported cities
 * - ISO format output for standardized datetime representation
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimezoneHelper {
    public static String formatWithTimezone(ZonedDateTime dateTime, String zoneId) {
        ZonedDateTime zoned = dateTime.withZoneSameInstant(ZoneId.of(zoneId));
        return zoned.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
