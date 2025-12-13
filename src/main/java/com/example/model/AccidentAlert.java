/**
 * Immutable alert record for accident incidents requiring flight detours and operator notification.
 * 
 * Purpose:
 * Represents a single accident alert with complete incident information including description, location,
 * involved jetpack, severity classification, and timestamp. Provides a standardized, immutable data
 * structure for accident reporting and distribution throughout the air traffic control system. Enables
 * graduated response based on severity level (MINOR, MAJOR, CRITICAL) and supports flight detour
 * coordination for affected airspace.
 * 
 * Key Responsibilities:
 * - Store immutable accident details (description, location, jetpackId, severity, timestamp)
 * - Classify incident severity using three-level system (MINOR/MAJOR/CRITICAL)
 * - Provide timestamp for incident tracking and alert freshness validation
 * - Enable location-based queries for determining affected flights
 * - Support identification of involved jetpack for incident response
 * - Maintain data integrity through immutability after creation
 * - Enable serialization for logging and persistence
 * 
 * Interactions:
 * - Created by AccidentReporter when incidents are detected or reported
 * - Consumed by FlightHazardMonitor to trigger flight detours
 * - Referenced by CollisionDetector when collisions result in accidents
 * - Logged to city-specific accident report files
 * - Broadcasted via Radio system to nearby aircraft
 * - Displayed in RadarTapeWindow for operator awareness
 * - Used in accident statistics and safety analysis
 * 
 * Patterns & Constraints:
 * - Immutable value object ensures alert integrity and thread safety
 * - Severity enum provides fixed classification levels for consistent response
 * - Timestamp captures creation time for alert age tracking
 * - Location stored as String for flexibility (coordinate or description format)
 * - JetpackId identifies involved aircraft (may be "UNKNOWN" for external incidents)
 * - Lightweight structure suitable for high-frequency alert generation
 * - No mutable state; alerts are create-once, read-many
 * - Thread-safe by virtue of immutability
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
