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

public class ParkingSpaceGenerator {
    /**
     * Generates a specified number of parking spaces with automatic positioning.
     * Creates parking spaces with sequential IDs and evenly distributed positions
     * using a simple geometric pattern. Useful for initializing city parking infrastructure.
     * 
     * @param count Number of parking spaces to generate
     * @return List of newly created ParkingSpace objects with unique IDs and positions
     */
    public List<ParkingSpace> generateSpaces(int count) {
        // Initialize empty list to accumulate generated parking spaces
        List<ParkingSpace> spaces = new ArrayList<>();
        
        // Generate parking spaces iteratively with sequential numbering
        for (int i = 0; i < count; i++) {
            // Create unique parking space ID: "GEN-P1", "GEN-P2", etc.
            // The "GEN-" prefix indicates generated (vs. manually configured) spaces
            
            // Calculate position using linear distribution pattern:
            //   X coordinate: i * 10.0 (spaces 10 units apart horizontally)
            //   Y coordinate: i * 20.0 (spaces 20 units apart vertically)
            // This creates a diagonal arrangement across the city grid
            spaces.add(new ParkingSpace("GEN-P" + (i+1), i * 10.0, i * 20.0));
        }
        
        // Return the complete list of generated parking spaces
        return spaces;
    }
}
