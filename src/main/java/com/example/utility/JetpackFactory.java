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
    public static JetPack createJetPack(String id, String serialNumber, String callsign, String ownerName, String year, String model, String manufacturer) {
        return new JetPack(id, serialNumber, callsign, ownerName, year, model, manufacturer, new java.awt.Point(0, 0), 0.0, 0.0);
    }
}
