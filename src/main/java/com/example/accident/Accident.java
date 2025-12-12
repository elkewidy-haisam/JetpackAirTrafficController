/**
 * Accident.java
 * by Haisam Elkewidy
 *
 * Represents a single accident event in the city.
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
