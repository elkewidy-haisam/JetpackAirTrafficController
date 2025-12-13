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
    // List to store all radio transmission log entries chronologically
    private final List<String> transmissions;

    /**
     * Constructs a new RadioTransmissionLogger with empty log.
     */
    public RadioTransmissionLogger() {
        // Initialize empty list to hold transmission log entries
        this.transmissions = new ArrayList<>();
    }

    /**
     * Logs a radio transmission by adding it to the transmission list.
     * 
     * @param transmission Transmission message to log
     */
    public void logTransmission(String transmission) {
        // Append transmission to end of log list (chronological order)
        transmissions.add(transmission);
    }

    /**
     * Returns the list of all logged transmissions.
     * 
     * @return List of transmission strings in chronological order
     */
    public List<String> getTransmissions() {
        // Return reference to transmissions list
        return transmissions;
    }
}
