/*
 * AccidentAlert.java
 * Part of Jetpack Air Traffic Controller
 *
 * Alerts jetpack pilots of any accidents, collisions, or hazards in the city.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */

package com.example.accident;

import java.util.ArrayList;
import java.util.List;

public class AccidentAlert {
    /**
     * Inner class to represent an accident
     */
    public static class Accident {
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

        // Getters
        public String getAccidentID() {
            return accidentID;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getType() {
            return type;
        }

        public String getSeverity() {
            return severity;
        }

        public String getDescription() {
            return description;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        @Override
        public String toString() {
            return String.format("Accident[ID=%s, Type=%s, Severity=%s, Location=(%d,%d), Active=%s]",
                    accidentID, type, severity, x, y, isActive);
        }
    }

    private final List<Accident> accidents = new ArrayList<>();
    private String alertId;

    public AccidentAlert() {
        this.alertId = "";
    }

    public AccidentAlert(String alertId) {
        this.alertId = alertId;
    }

    /**
     * Reports a new accident and stores it in the list.
     */
    public void reportAccident(String accidentID, int x, int y, String type, String severity, String description) {
        Accident accident = new Accident(accidentID, x, y, type, severity, description);
        accidents.add(accident);
        // Optionally, log or notify here
    }

    /**
     * Returns all reported accidents.
     */
    public List<Accident> getAccidents() {
        return new ArrayList<>(accidents);
    }

    // Stub for getAccidentsNearLocation
    public List<Accident> getAccidentsNearLocation(int x, int y, double radius) {
        return new ArrayList<>();
    }

    // Stub for alertJetpacksOfAccident
    public void alertJetpacksOfAccident(String accidentID, List<?> nearbyJetpacks, double radius) {
        // Stub implementation
    }

    // Stub for removeAlert
    public boolean removeAlert(String accidentID) {
        return false;
    }

    // Stub for getActiveAccidentCount
    public int getActiveAccidentCount() {
        return 0;
    }
}
