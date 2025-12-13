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
     * When false, no debug output is produced regardless of individual system flags.
     * Set to false for production/normal use, true for debugging and troubleshooting.
     * Performance impact: Disabling this flag improves performance by eliminating debug checks.
     */
    public static final boolean VERBOSE = false;

    /**
     * Individual system-specific debug flags.
     * These flags only have effect when VERBOSE is true (master switch).
     * Use these for targeted debugging of specific subsystems without flooding logs.
     */
    
    // Weather system debug flag - logs weather changes, severity updates, and safety checks
    public static final boolean LOG_WEATHER = false;
    
    // Radar system debug flag - logs position updates, tracking events, and collision detection
    public static final boolean LOG_RADAR = false;
    
    // Accident system debug flag - logs accident reports, alerts, and clearance operations
    public static final boolean LOG_ACCIDENTS = false;
    
    // Radio system debug flag - logs transmissions, broadcasts, and communication events
    public static final boolean LOG_RADIO = false;
    
    // Air Traffic Controller debug flag - logs ATC operations, approvals, and system checks
    public static final boolean LOG_ATC = false;

    /**
     * Checks if logging is enabled for a specific subsystem.
     * Performs a two-level check: first the master VERBOSE flag, then the system-specific flag.
     * This allows centralized control via VERBOSE while maintaining granular subsystem toggles.
     * 
     * @param system The subsystem name (case-insensitive: "weather", "radar", "accidents", "radio", "atc")
     * @return true if both VERBOSE and the system-specific flag are true, false otherwise
     */
    public static boolean isEnabled(String system) {
        // First check: master VERBOSE flag acts as kill switch for all debug output
        if (!VERBOSE) return false;

        // Second check: evaluate system-specific flag based on subsystem name
        switch (system.toLowerCase()) {
            // Weather subsystem logging control
            case "weather": return LOG_WEATHER;
            
            // Radar subsystem logging control
            case "radar": return LOG_RADAR;
            
            // Accident subsystem logging control
            case "accidents": return LOG_ACCIDENTS;
            
            // Radio subsystem logging control
            case "radio": return LOG_RADIO;
            
            // Air Traffic Controller subsystem logging control
            case "atc": return LOG_ATC;
            
            // Unknown subsystem names default to disabled
            default: return false;
        }
    }
}