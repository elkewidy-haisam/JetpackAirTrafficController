/**
 * AccidentAlert.java
 * by Haisam Elkewidy
 *
 * This class handles AccidentAlert functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - description (String)
 *   - timestamp (LocalDateTime)
 *   - location (String)
 *   - jetpackId (String)
 *   - severity (Severity)
 *
 * Methods:
 *   - AccidentAlert(description, location, jetpackId, severity)
 *   - toString()
 *
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
