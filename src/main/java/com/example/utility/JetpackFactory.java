/**
 * Factory for creating JetPack objects with consistent initialization and default parameters.
 * 
 * Purpose:
 * Centralizes JetPack object creation to ensure consistent initialization across the application. Provides
 * a factory method that creates fully-initialized jetpack instances with all required parameters,
 * eliminating constructor call duplication and providing a single point for jetpack instantiation logic.
 * 
 * Key Responsibilities:
 * - Create JetPack objects with full parameter set
 * - Provide default initial position (0,0) and heading (0.0) for new jetpacks
 * - Ensure consistent jetpack initialization across different city map viewers
 * - Encapsulate JetPack constructor complexity
 * 
 * Interactions:
 * - Used by CityMapPanel to generate jetpack fleets for each city
 * - Supports jetpack generation methods that create 300 jetpacks per city
 * - Provides created JetPack objects to JetPackFlight for animation
 * - May be extended to support different jetpack types/configurations
 * 
 * Patterns & Constraints:
 * - Factory pattern: encapsulates object creation logic
 * - Static factory method for stateless operation
 * - Thread-safe due to lack of mutable state
 * - Returns fully-initialized, ready-to-fly JetPack objects
 * - Default position/heading assumes immediate re-positioning by caller
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility;

import com.example.jetpack.JetPack;

public class JetpackFactory {
    /**
     * Creates a new JetPack with specified identification and specifications.
     * Initializes jetpack at origin (0,0) with zero heading and altitude.
     * 
     * @param id unique identifier for the jetpack
     * @param serialNumber manufacturer serial number
     * @param callsign radio callsign for ATC communications
     * @param ownerName name of the jetpack owner/pilot
     * @param year manufacturing year
     * @param model jetpack model designation
     * @param manufacturer company that manufactured the jetpack
     * @return newly created JetPack object with all parameters set
     */
    public static JetPack createJetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer) {
        // Create and return new JetPack with all parameters
        // Initial position: (0,0), heading: 0.0, altitude: 0.0
        // Caller is expected to set actual position after creation
        return new JetPack(id,              // Set unique jetpack identifier
                serialNumber,                // Set manufacturer serial number
                callsign,                    // Set radio callsign for ATC
                ownerName,                   // Set owner/pilot name
                year,                        // Set manufacturing year
                model,                       // Set jetpack model designation
                manufacturer,                // Set manufacturer name
                new java.awt.Point(0, 0),   // Initialize at origin (0,0) - temporary position
                0.0,                         // Initialize heading at 0.0 degrees (north)
                0.0);                        // Initialize altitude at 0.0 (ground level)
    }
}
