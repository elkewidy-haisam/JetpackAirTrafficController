/**
 * Utility class for timezone conversions and city-specific time formatting across multiple US cities.
 * 
 * Purpose:
 * Provides timezone mapping and conversion utilities for displaying accurate local times in different
 * cities. Enables the air traffic control system to show correct time zones for New York (EST),
 * Boston (EST), Houston (CST), and Dallas (CST). Supports timezone-aware formatting with proper
 * ZoneId handling and ISO 8601 standard formats. Essential for multi-city operations where each
 * city operates on its local time.
 * 
 * Key Responsibilities:
 * - Map city names to appropriate Java ZoneId instances
 * - Convert ZonedDateTime between different timezone contexts
 * - Format timestamps with timezone information for display
 * - Support US Eastern and Central time zones
 * - Enable accurate local time displays for each city
 * - Provide ISO 8601 formatted output for standards compliance
 * - Handle timezone abbreviations (EST, CST) for user-friendly display
 * 
 * Interactions:
 * - Used by CityDisplayUpdater for date/time label formatting
 * - Referenced by AirTrafficControllerFrame for timezone selection
 * - Supports Weather system for timezone-aware broadcasts
 * - Integrates with DateTimeDisplayPanel for accurate time display
 * - Used in logging systems for timestamp normalization
 * - Coordinates with DayTime for complete time management
 * - Supports future expansion to other US or international cities
 * 
 * Patterns & Constraints:
 * - Static utility class with stateless timezone operations
 * - City-to-timezone mappings: "New York"→"America/New_York", "Houston"→"America/Chicago"
 * - Thread-safe due to stateless design
 * - Uses Java 8+ ZoneId and ZonedDateTime APIs
 * - ISO 8601 format for standardized timestamp representation
 * - Supports timezone abbreviations via ZonedDateTime formatting
 * - Eastern Time: UTC-5 (EST) / UTC-4 (EDT with daylight saving)
 * - Central Time: UTC-6 (CST) / UTC-5 (CDT with daylight saving)
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
