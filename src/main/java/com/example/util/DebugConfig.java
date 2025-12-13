/**
 * Global debug configuration constants controlling verbose logging and diagnostic output across the application.
 * 
 * Purpose:
 * Provides centralized debug flag management for enabling/disabling diagnostic logging throughout
 * the air traffic control system. Controls master verbose flag and subsystem-specific logging flags
 * for weather, radar, accidents, radio, and ATC operations. Enables performance optimization by
 * disabling logging in production while supporting detailed diagnostics during development and
 * troubleshooting.
 * 
 * Key Responsibilities:
 * - Define master VERBOSE flag controlling all debug output
 * - Provide subsystem-specific logging flags (weather, radar, accidents, radio, ATC)
 * - Enable performance optimization by disabling logging overhead
 * - Support targeted debugging with individual system flags
 * - Provide compile-time constants for efficient code optimization
 * - Facilitate production/development mode switching
 * - Document debug flag usage and conventions
 * 
 * Interactions:
 * - Referenced throughout application for conditional debug logging
 * - Consulted by Weather, Radar, AccidentReporter, Radio, ATC components
 * - Used in if-statements to guard expensive debug operations
 * - Enables JVM compiler optimizations for false constant conditions
 * - Supports performance profiling with selective logging
 * - Used in testing scenarios with debug output enabled
 * - Coordinates with logging frameworks and console output
 * 
 * Patterns & Constraints:
 * - Static final constants for compile-time optimization
 * - Master VERBOSE flag gates all subsystem flags
 * - Individual flags ignored when VERBOSE is false (performance)
 * - Boolean flags enable simple if-guard patterns: if (DebugConfig.LOG_WEATHER) { ... }
 * - Set VERBOSE = false for production/normal use (default)
 * - Set VERBOSE = true for development/debugging scenarios
 * - Subsystem flags allow targeted diagnostics without full verbose mode
 * - No runtime configuration; flags set at compile time for performance
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