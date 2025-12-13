/**
 * Domain model representing an incident or hazardous event affecting jetpack flight operations.
 * 
 * Purpose:
 * Encapsulates all data for a single accident event including location, type classification,
 * severity level, and descriptive details. Provides a standardized structure for accident reporting,
 * tracking, and coordination with flight safety systems. Supports accident lifecycle management
 * from initial report through resolution and clearance.
 * 
 * Key Responsibilities:
 * - Store immutable accident identification (ID, timestamp) for incident tracking
 * - Maintain accident location coordinates for hazard zone mapping
 * - Classify incident type (collision, equipment failure, weather, building collapse)
 * - Track severity level (minor, moderate, severe, critical) for response prioritization
 * - Provide human-readable description for operator awareness
 * - Manage active/inactive status for lifecycle tracking and hazard clearance
 * - Support timestamp queries for incident age and freshness validation
 * 
 * Interactions:
 * - Created by AccidentReporter when incidents occur or are reported
 * - Referenced by AccidentAlert for flight detour coordination
 * - Logged to city-specific accident report files by CityLogManager
 * - Displayed in UI components for operator notification
 * - Consulted by FlightHazardMonitor to determine if detours required
 * - Used by Radio to broadcast accident notifications to affected aircraft
 * - Archived for post-incident analysis and safety reporting
 * 
 * Patterns & Constraints:
 * - Immutable identity fields (accidentID, location, type, severity, timestamp)
 * - Mutable active status for lifecycle management (reported → resolved → cleared)
 * - Lightweight data object suitable for high-frequency accident tracking
 * - Thread-safe for read operations; external synchronization for status updates
 * - Severity levels support graduated response (minor=advisory, critical=ground all flights)
 * - Timestamp enables time-based hazard expiration and staleness detection
 * 
 * @author Haisam Elkewidy
 */

package com.example.accident;

public class Accident {
    private final String accidentID;
    private final int x;
    private final int y;
    private final String type;
    private final String severity;
    private final String description;
    private final long timestamp;
    private boolean isActive;

    public Accident(String accidentID, int x, int y, String type, String severity, String description) {
        this.accidentID = accidentID;
        this.x = x;
        this.y = y;
        this.type = type;
        this.severity = severity;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
        this.isActive = true;
    }

    public String getAccidentID() { return accidentID; }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getType() { return type; }
    public String getSeverity() { return severity; }
    public String getDescription() { return description; }
    public long getTimestamp() { return timestamp; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return String.format("Accident[ID=%s, Type=%s, Severity=%s, Location=(%d,%d), Active=%s]", accidentID, type, severity, x, y, isActive);
    }
}
