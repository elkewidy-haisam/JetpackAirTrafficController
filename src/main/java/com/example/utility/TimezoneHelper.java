/**
 * TimezoneHelper.java
 * by Haisam Elkewidy
 *
 * Utility for handling time zone conversions and formatting.
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
