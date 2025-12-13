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

public class TimezoneHelper {
    /**
     * Formats a ZonedDateTime to a specific timezone and returns ISO formatted string.
     * 
     * @param dateTime ZonedDateTime to format
     * @param zoneId Target timezone ID string (e.g., "America/New_York")
     * @return ISO formatted datetime string in target timezone
     */
    public static String formatWithTimezone(ZonedDateTime dateTime, String zoneId) {
        // Convert datetime to target timezone while keeping the same instant
        ZonedDateTime zoned = dateTime.withZoneSameInstant(ZoneId.of(zoneId));
        // Format the zoned datetime as ISO-8601 string and return
        return zoned.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
