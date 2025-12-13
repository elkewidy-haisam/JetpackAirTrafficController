/**
 * JetpackFactory component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides jetpackfactory functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core jetpackfactory operations
 * - Maintain necessary state for jetpackfactory functionality
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

import com.example.jetpack.JetPack;

/**
 * Factory class for creating JetPack instances with default initialization.
 * Centralizes jetpack construction logic and provides consistent default values.
 */
public class JetpackFactory {
    
    /**
     * Creates a new JetPack with specified identification and registration details.
     * Initializes position to origin (0,0) and velocity to zero as defaults.
     *
     * @param id Unique identifier for the jetpack
     * @param serialNumber Manufacturer serial number
     * @param callsign Radio callsign for air traffic communications
     * @param ownerName Name of registered owner
     * @param year Year of manufacture
     * @param model Model designation
     * @param manufacturer Manufacturer name
     * @return Newly constructed JetPack instance with default position and velocity
     */
    public static JetPack createJetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer) {
        // Create JetPack at origin (0,0) with zero velocity (dx=0.0, dy=0.0)
        return new JetPack(id, serialNumber, callsign, ownerName, year, model, manufacturer, new java.awt.Point(0, 0), 0.0, 0.0);
    }
}
