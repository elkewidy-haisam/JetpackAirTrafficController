/**
 * DebugConfig.java
 * by Haisam Elkewidy
 *
 * Global debug configuration for the application. Set VERBOSE to false to disable console logging and improve performance.
 */

package com.example.util;

/**
 * Global debug configuration for the application.
 * Set VERBOSE to false to disable all debug output and improve performance.
 * Individual system flags can be toggled for targeted debugging.
 */
public class DebugConfig {
    /**
     * Master verbose flag - controls all debug output across the application.
     * Set to false for production/normal use, true for debugging.
     */
    public static final boolean VERBOSE = false;

    /**
     * Individual system flags (all respect VERBOSE - if VERBOSE is false, these are ignored).
     */
    public static final boolean LOG_WEATHER = false;
    public static final boolean LOG_RADAR = false;
    public static final boolean LOG_ACCIDENTS = false;
    public static final boolean LOG_RADIO = false;
    public static final boolean LOG_ATC = false;

    /**
     * Checks if logging is enabled for a specific system.
     * @param system The system name (weather, radar, accidents, radio, atc)
     * @return true if logging is enabled for the system, false otherwise
     */
    public static boolean isEnabled(String system) {
        if (!VERBOSE) return false;

        switch (system.toLowerCase()) {
            case "weather": return LOG_WEATHER;
            case "radar": return LOG_RADAR;
            case "accidents": return LOG_ACCIDENTS;
            case "radio": return LOG_RADIO;
            case "atc": return LOG_ATC;
            default: return false;
        }
    }
}