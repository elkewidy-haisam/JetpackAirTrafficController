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
    public List<ParkingSpace> generateSpaces(int count) {
        List<ParkingSpace> spaces = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            spaces.add(new ParkingSpace("GEN-P" + (i+1), i * 10.0, i * 20.0));
        }
        return spaces;
    }
}
