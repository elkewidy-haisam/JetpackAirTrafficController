/**
 * Global configuration for controlling debug logging output across all system components.
 * 
 * Purpose:
 * Centralized flag management for enabling/disabling verbose logging output throughout the application.
 * Provides a master VERBOSE switch and individual subsystem toggles for targeted debugging of specific
 * components (weather, radar, accidents, radio, ATC). Designed to be set at compile-time for performance.
 * 
 * Key Responsibilities:
 * - Define master VERBOSE flag that controls all debug output
 * - Provide per-subsystem logging flags (LOG_WEATHER, LOG_RADAR, etc.)
 * - Offer isEnabled() method for programmatic debug state checks
 * - Enable easy toggling between production and debug modes
 * 
 * Interactions:
 * - Referenced by all major components for conditional logging
 * - Checked before expensive string formatting operations
 * - Used by Weather, Radar, Radio, and ATC systems
 * - Controls verbosity of log files and console output
 * 
 * Patterns & Constraints:
 * - Configuration class with only public static final constants
 * - Master-slave pattern: VERBOSE must be true for individual flags to work
 * - Compile-time constants allow JVM to optimize away dead code branches
 * - Set VERBOSE=false for production to eliminate performance overhead
 * - Thread-safe by virtue of being immutable constants
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