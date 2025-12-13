/**
 * Manages accident alert lifecycle and jetpack notification for hazard avoidance.
 * 
 * Purpose:
 * Creates and broadcasts accident alerts to notify jetpacks of hazardous areas requiring avoidance or
 * rerouting. Tracks alert status (active/resolved) and integrates with radio communication system to
 * ensure all aircraft are informed of incidents.
 * 
 * Key Responsibilities:
 * - Create accident alerts with unique IDs
 * - Broadcast alerts to jetpacks via radio
 * - Track alert active status
 * - Support alert resolution/removal
 * - Provide alert information for UI display
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

import java.time.LocalDateTime;

public class AccidentAlert {
    /** Description of the accident/incident that occurred */
    private final String description;
    
    /** Timestamp when this alert was created */
    private final LocalDateTime timestamp;
    
    /** Location description where the accident occurred */
    private final String location;
    
    /** ID of the jetpack involved in the accident */
    private final String jetpackId;
    
    /** Severity level classification of the accident */
    private final Severity severity;

    /**
     * Enumeration of accident severity levels.
     * Used to classify the seriousness of incidents.
     */
    public enum Severity {
        /** Minor incident with minimal impact */
        MINOR,
        /** Major incident requiring attention */
        MAJOR,
        /** Critical incident requiring immediate response */
        CRITICAL
    }

    /**
     * Constructs a new accident alert with specified details.
     * Timestamp is automatically set to current system time.
     * 
     * @param description text describing what happened
     * @param location where the accident occurred
     * @param jetpackId ID of jetpack involved
     * @param severity classification level of the incident
     */
    public AccidentAlert(String description, String location, String jetpackId, Severity severity) {
        this.description = description;      // Store accident description
        this.location = location;            // Store location
        this.jetpackId = jetpackId;          // Store involved jetpack ID
        this.severity = severity;            // Store severity level
        this.timestamp = LocalDateTime.now(); // Record current time
    }

    /**
     * Returns the accident description.
     * @return description text
     */
    public String getDescription() {
        return description;  // Return stored description
    }

    /**
     * Returns the timestamp when alert was created.
     * @return LocalDateTime of alert creation
     */
    public LocalDateTime getTimestamp() {
        return timestamp;  // Return stored timestamp
    }

    /**
     * Returns the location of the accident.
     * @return location description string
     */
    public String getLocation() {
        return location;  // Return stored location
    }

    /**
     * Returns the jetpack ID involved in accident.
     * @return jetpack identifier string
     */
    public String getJetpackId() {
        return jetpackId;  // Return stored jetpack ID
    }

    /**
     * Returns the severity level of the accident.
     * @return Severity enum value
     */
    public Severity getSeverity() {
        return severity;  // Return stored severity
    }

    /**
     * Returns formatted string representation of this alert.
     * Includes timestamp, description, jetpack ID, location, and severity.
     * 
     * @return formatted alert string for logging/display
     */
    @Override
    public String toString() {
        // Format and return all alert details in structured format
        return String.format("[%s] Accident: %s | Jetpack: %s | Location: %s | Severity: %s", 
            timestamp, description, jetpackId, location, severity);
    }
}
