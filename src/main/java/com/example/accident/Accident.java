/**
 * Represents an accident incident in the airspace with location, classification, and status tracking.
 * 
 * Purpose:
 * Models an aviation accident or hazardous event that occurs within the city airspace, capturing
 * critical details such as location, type, severity, and active status. This is used by the air
 * traffic control system to alert pilots, reroute flights, and log incident information.
 * 
 * Key Responsibilities:
 * - Store immutable accident identification and location data
 * - Classify accidents by type (COLLISION, GROUND_ACCIDENT, JETPACK_MALFUNCTION, etc.)
 * - Track severity levels (MINOR, MODERATE, SEVERE, CRITICAL)
 * - Maintain active status to indicate if the incident is ongoing or resolved
 * - Provide timestamp for incident tracking and historical analysis
 * 
 * Interactions:
 * - Created by AccidentReporter when incidents occur
 * - Referenced by Radio to broadcast accident alerts
 * - Used by AccidentAlert to determine avoidance zones
 * - Logged by CityLogManager for incident records
 * - Displayed in UI components (RadioInstructionsPanel, JetpackMovementPanel)
 * 
 * Patterns & Constraints:
 * - Immutable core properties (ID, location, type, severity, description, timestamp)
 * - Single mutable field (isActive) for lifecycle management
 * - Value object with comprehensive toString() for logging and debugging
 * - Thread-safe for reads; writes to isActive should be synchronized at caller level
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
