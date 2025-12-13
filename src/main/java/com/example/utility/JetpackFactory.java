/**
 * Factory class for creating jetpack instances with consistent initialization and default values.
 * 
 * Purpose:
 * Provides centralized jetpack creation with standardized initialization of position, altitude, and
 * speed to default values. Encapsulates jetpack construction logic to ensure consistent object state
 * across the application. Reduces code duplication and potential errors from manual jetpack
 * instantiation. Supports future enhancements like validation, configuration injection, or
 * specialized jetpack types.
 * 
 * Key Responsibilities:
 * - Create JetPack instances with provided identification and metadata
 * - Initialize position to origin point (0, 0) as default spawn location
 * - Set altitude and speed to zero for safe initial state
 * - Ensure consistent jetpack construction across application
 * - Provide single point of control for jetpack creation logic
 * - Support future validation or configuration injection
 * - Enable testing with controlled jetpack creation
 * 
 * Interactions:
 * - Used throughout application for jetpack instantiation
 * - May be called by city initialization code for fleet generation
 * - Referenced in test fixtures for controlled jetpack creation
 * - Potentially extended for city-specific jetpack configurations
 * - Coordinates with JetPack model for proper construction
 * - Supports future specialized factories for different jetpack types
 * - May integrate with configuration systems for default values
 * 
 * Patterns & Constraints:
 * - Factory pattern encapsulates object creation complexity
 * - Static utility method for stateless factory operation
 * - Default values: position (0,0), altitude 0.0, speed 0.0
 * - Thread-safe due to stateless design
 * - No validation in current implementation (add as needed)
 * - Lightweight factory suitable for high-frequency creation
 * - Extensible for future jetpack variants (cargo, passenger, emergency)
 * - Coordinates with JetPack constructor for actual instantiation
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
