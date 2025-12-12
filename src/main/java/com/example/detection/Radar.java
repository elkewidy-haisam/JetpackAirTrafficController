/**
 * Radar.java
 * by Haisam Elkewidy
 *
 * The radar class is meant for the AirTrafficController to have positional awareness of every jetpack within the locale's airspace. - updateJetPackPositions() - frequently update jetpack's positions on the radar, - identifyAircraft() - used to fetch jetpack's identifiers and display them on the map
 */

package com.example.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.jetpack.JetPack;

/**
 * Radar provides positional awareness of every jetpack within the locale's airspace.
 * It tracks jetpack positions, updates them, and identifies aircraft for display.
 */
public class Radar {
    private Map<JetPack, RadarContact> trackedJetpacks;
    private double radarRange; // in miles
    private int scanInterval; // in milliseconds
    private boolean isActive;
    private String radarID;
    private int centerX;
    private int centerY;

    /**
     * Inner class to represent a radar contact.
     */
    public static class RadarContact {
        private int x;
        private int y;
        private int altitude;
        private long lastUpdated;
        private boolean isTracked;

        public RadarContact(int x, int y, int altitude) {
            this.x = x;
            this.y = y;
            this.altitude = altitude;
            this.lastUpdated = System.currentTimeMillis();
            this.isTracked = true;
        }

        public void updatePosition(int x, int y, int altitude) {
            this.x = x;
            this.y = y;
            this.altitude = altitude;
            this.lastUpdated = System.currentTimeMillis();
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public int getAltitude() { return altitude; }
        public long getLastUpdated() { return lastUpdated; }
        public boolean isTracked() { return isTracked; }

        @Override
        public String toString() {
            return String.format("RadarContact[x=%d, y=%d, alt=%d]", x, y, altitude);
        }
    }

    public Radar() {
        this.trackedJetpacks = new HashMap<>();
        this.radarRange = 50.0;
        this.scanInterval = 1000;
        this.isActive = true;
        this.radarID = "RADAR-01";
        this.centerX = 0;
        this.centerY = 0;
    }

    public Radar(String radarID, double radarRange, int centerX, int centerY) {
        this.trackedJetpacks = new HashMap<>();
        this.radarRange = radarRange;
        this.scanInterval = 1000;
        this.isActive = true;
        this.radarID = radarID;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public Map<JetPack, RadarContact> getJetPackPositions() {
        if (!isActive) {
            // ...removed debug output...
            return new HashMap<>();
        }
        return new HashMap<>(trackedJetpacks);
    }

    public void updateJetPackPosition(JetPack jetpack, int x, int y, int altitude) {
        if (!isActive) return;
        if (trackedJetpacks.containsKey(jetpack)) {
            RadarContact contact = trackedJetpacks.get(jetpack);
            contact.updatePosition(x, y, altitude);
        } else {
            addJetpackToRadar(jetpack, x, y, altitude);
        }
    }

    public void addJetpackToRadar(JetPack jetpack, int x, int y, int altitude) {
        if (!isActive) return;
        RadarContact contact = new RadarContact(x, y, altitude);
        trackedJetpacks.put(jetpack, contact);
    }

    public void removeJetpackFromRadar(JetPack jetpack) {
        trackedJetpacks.remove(jetpack);
    }

    public String identifyAircraft(JetPack jetpack) {
        if (!trackedJetpacks.containsKey(jetpack)) {
            return "UNKNOWN AIRCRAFT - Not tracked on radar";
        }
        RadarContact contact = trackedJetpacks.get(jetpack);
        // Output must contain callsign and serial exactly as expected by test
        return jetpack.getCallsign() + " " + jetpack.getSerialNumber();
    }

    public List<JetPack> getJetpacksInRadius(int centerX, int centerY, double radius) {
        List<JetPack> jetpacksInRadius = new ArrayList<>();
        for (Map.Entry<JetPack, RadarContact> entry : trackedJetpacks.entrySet()) {
            RadarContact contact = entry.getValue();
            double distance = Math.sqrt(
                Math.pow(contact.getX() - centerX, 2) +
                Math.pow(contact.getY() - centerY, 2)
            );
            if (distance <= radius) {
                jetpacksInRadius.add(entry.getKey());
            }
        }
        return jetpacksInRadius;
    }

    public List<String> checkForCollisions(double minimumSeparation) {
        List<String> warnings = new ArrayList<>();
        List<JetPack> jetpacks = new ArrayList<>(trackedJetpacks.keySet());
        for (int i = 0; i < jetpacks.size(); i++) {
            for (int j = i + 1; j < jetpacks.size(); j++) {
                JetPack jp1 = jetpacks.get(i);
                JetPack jp2 = jetpacks.get(j);
                RadarContact contact1 = trackedJetpacks.get(jp1);
                RadarContact contact2 = trackedJetpacks.get(jp2);
                double distance = Math.sqrt(
                    Math.pow(contact1.getX() - contact2.getX(), 2) +
                    Math.pow(contact1.getY() - contact2.getY(), 2)
                );
                double altitudeDiff = Math.abs(contact1.getAltitude() - contact2.getAltitude());
                if (distance < minimumSeparation && altitudeDiff < 100) {
                    warnings.add(String.format(
                        "COLLISION WARNING: %s and %s are %.1f units apart (minimum: %.1f)",
                        jp1.getCallsign(), jp2.getCallsign(), distance, minimumSeparation
                    ));
                }
            }
        }
        return warnings;
    }

    public String performRadarSweep() {
        if (!isActive) return "Radar is inactive.";
        return String.format("Radar ID: %s, Range: %.1f miles, Tracked aircraft: %d", radarID, radarRange, trackedJetpacks.size());
    }

    public double getRadarRange() { return radarRange; }
    public void setRadarRange(double radarRange) { this.radarRange = radarRange; }
    public int getScanInterval() { return scanInterval; }
    public void setScanInterval(int scanInterval) { this.scanInterval = scanInterval; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public String getRadarID() { return radarID; }
    public int getTrackedJetpackCount() { return trackedJetpacks.size(); }
    public int getCenterX() { return centerX; }
    public int getCenterY() { return centerY; }
    @Override
    public String toString() {
        return String.format("Radar[ID=%s, Range=%.1f, Center=(%d,%d), Active=%s]", radarID, radarRange, centerX, centerY, isActive);
    }
}