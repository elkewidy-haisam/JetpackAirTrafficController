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

/**
 * Logger for radio transmission history and audit trail.
 * Maintains chronological record of all radio communications.
 */
public class RadioTransmissionLogger {
    // List storing all logged radio transmission strings
    private final List<String> transmissions;

    /**
     * Constructs a new RadioTransmissionLogger.
     * Initializes with empty transmission log.
     */
    public RadioTransmissionLogger() {
        // Create empty list to store transmission history
        this.transmissions = new ArrayList<>();
    }

    /**
     * Logs a radio transmission to the history.
     * Appends transmission to chronological log.
     *
     * @param transmission Transmission text to log
     */
    public void logTransmission(String transmission) {
        // Add transmission to log list
        transmissions.add(transmission);
    }

    /**
     * Gets the complete list of logged transmissions.
     * Returns mutable list for external access.
     *
     * @return List of all transmission strings
     */
    public List<String> getTransmissions() {
        return transmissions;
    }
}
