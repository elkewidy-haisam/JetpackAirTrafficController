/**
 * DebugConfig component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides debugconfig functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core debugconfig operations
 * - Maintain necessary state for debugconfig functionality
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
        // Check master verbose flag first - if false, all logging disabled
        if (!VERBOSE) return false;

        // Convert system name to lowercase for case-insensitive matching
        switch (system.toLowerCase()) {
            // Return weather logging flag if weather system requested
            case "weather": return LOG_WEATHER;
            // Return radar logging flag if radar system requested
            case "radar": return LOG_RADAR;
            // Return accidents logging flag if accidents system requested
            case "accidents": return LOG_ACCIDENTS;
            // Return radio logging flag if radio system requested
            case "radio": return LOG_RADIO;
            // Return ATC logging flag if ATC system requested
            case "atc": return LOG_ATC;
            // Return false for unknown system names
            default: return false;
        }
    }
}