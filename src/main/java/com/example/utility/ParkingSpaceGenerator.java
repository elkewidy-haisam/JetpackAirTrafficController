/**
 * ParkingSpaceGenerator component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides parkingspacegenerator functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core parkingspacegenerator operations
 * - Maintain necessary state for parkingspacegenerator functionality
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

import com.example.model.ParkingSpace;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator utility for creating parking space instances.
 * Provides procedural generation of parking locations for testing and development.
 */
public class ParkingSpaceGenerator {
    
    /**
     * Generates a list of parking spaces with automatically assigned IDs and positions.
     * Creates evenly spaced parking spots along a diagonal pattern.
     *
     * @param count Number of parking spaces to generate
     * @return List of newly created ParkingSpace instances
     */
    public List<ParkingSpace> generateSpaces(int count) {
        // Initialize empty list to hold generated parking spaces
        List<ParkingSpace> spaces = new ArrayList<>();
        // Generate specified number of parking spaces
        for (int i = 0; i < count; i++) {
            // Create parking space with generated ID and diagonal position (i*10, i*20)
            spaces.add(new ParkingSpace("GEN-P" + (i+1), i * 10.0, i * 20.0));
        }
        // Return complete list of generated parking spaces
        return spaces;
    }
}
