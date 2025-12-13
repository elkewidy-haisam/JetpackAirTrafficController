/**
 * RadioTransmissionLogger component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides radiotransmissionlogger functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core radiotransmissionlogger operations
 * - Maintain necessary state for radiotransmissionlogger functionality
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

package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class RadioTransmissionLogger {
    private final List<String> transmissions;

    public RadioTransmissionLogger() {
        this.transmissions = new ArrayList<>();
    }

    public void logTransmission(String transmission) {
        transmissions.add(transmission);
    }

    public List<String> getTransmissions() {
        return transmissions;
    }
}
