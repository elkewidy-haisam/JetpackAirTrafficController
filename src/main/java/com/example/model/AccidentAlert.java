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
    private final String description;
    private final LocalDateTime timestamp;
    private final String location;
    private final String jetpackId;
    private final Severity severity;

    public enum Severity {
        MINOR, MAJOR, CRITICAL
    }

    public AccidentAlert(String description, String location, String jetpackId, Severity severity) {
        this.description = description;
        this.location = location;
        this.jetpackId = jetpackId;
        this.severity = severity;
        this.timestamp = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLocation() {
        return location;
    }

    public String getJetpackId() {
        return jetpackId;
    }

    public Severity getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return String.format("[%s] Accident: %s | Jetpack: %s | Location: %s | Severity: %s", 
            timestamp, description, jetpackId, location, severity);
    }
}
