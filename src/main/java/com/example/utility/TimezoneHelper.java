/**
 * TimezoneHelper component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides timezonehelper functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core timezonehelper operations
 * - Maintain necessary state for timezonehelper functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for timezone-aware date/time formatting.
 * Provides static helper methods to convert and format timestamps across different timezones.
 */
public class TimezoneHelper {
    
    /**
     * Formats a ZonedDateTime in a specific timezone using ISO 8601 format.
     * Converts the input datetime to the target timezone and returns formatted string.
     *
     * @param dateTime The datetime to format (can be in any timezone)
     * @param zoneId Target timezone ID string (e.g., "America/New_York", "UTC")
     * @return ISO 8601 formatted datetime string in the target timezone
     */
    public static String formatWithTimezone(ZonedDateTime dateTime, String zoneId) {
        // Convert the input datetime to the specified timezone (same instant, different zone)
        ZonedDateTime zoned = dateTime.withZoneSameInstant(ZoneId.of(zoneId));
        // Format the converted datetime using ISO 8601 standard format
        return zoned.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
