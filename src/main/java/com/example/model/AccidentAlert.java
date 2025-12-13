/**
 * AccidentAlert component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides accidentalert functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core accidentalert operations
 * - Maintain necessary state for accidentalert functionality
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

import java.time.LocalDateTime;

public class AccidentAlert {
    // Detailed description of the accident
    private final String description;
    // Time when accident alert was created
    private final LocalDateTime timestamp;
    // Location description where accident occurred
    private final String location;
    // Identifier of jetpack involved in accident
    private final String jetpackId;
    // Severity classification of the accident
    private final Severity severity;

    /**
     * Enumeration of accident severity levels.
     * MINOR: Low impact, no significant damage
     * MAJOR: Significant impact, requires attention
     * CRITICAL: Severe impact, emergency response needed
     */
    public enum Severity {
        MINOR, MAJOR, CRITICAL
    }

    /**
     * Constructs a new AccidentAlert with specified details.
     * Timestamp is automatically set to current time.
     * 
     * @param description Detailed description of the accident
     * @param location Location description where accident occurred
     * @param jetpackId Identifier of jetpack involved
     * @param severity Severity classification
     */
    public AccidentAlert(String description, String location, String jetpackId, Severity severity) {
        // Store accident description for reporting
        this.description = description;
        // Store location where accident occurred
        this.location = location;
        // Store jetpack ID for tracking involved aircraft
        this.jetpackId = jetpackId;
        // Store severity level for priority assessment
        this.severity = severity;
        // Record current time as accident timestamp
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Returns the accident description.
     * 
     * @return Accident description string
     */
    public String getDescription() {
        // Return stored description
        return description;
    }

    /**
     * Returns the accident timestamp.
     * 
     * @return LocalDateTime when alert was created
     */
    public LocalDateTime getTimestamp() {
        // Return stored timestamp
        return timestamp;
    }

    /**
     * Returns the accident location.
     * 
     * @return Location description string
     */
    public String getLocation() {
        // Return stored location
        return location;
    }

    /**
     * Returns the jetpack identifier.
     * 
     * @return Jetpack ID string
     */
    public String getJetpackId() {
        // Return stored jetpack ID
        return jetpackId;
    }

    /**
     * Returns the accident severity.
     * 
     * @return Severity enum value
     */
    public Severity getSeverity() {
        // Return stored severity classification
        return severity;
    }

    /**
     * Returns formatted string representation of accident alert.
     * 
     * @return String with timestamp, description, jetpack, location, and severity
     */
    @Override
    public String toString() {
        // Format alert details as readable string for logging/display
        return String.format("[%s] Accident: %s | Jetpack: %s | Location: %s | Severity: %s", 
            timestamp, description, jetpackId, location, severity);
    }
}
