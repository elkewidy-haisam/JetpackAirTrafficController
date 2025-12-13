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

public class JetpackFactory {
    public static JetPack createJetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer) {
        return new JetPack(id, serialNumber, callsign, ownerName, year, model, manufacturer, new java.awt.Point(0, 0), 0.0, 0.0);
    }
}
