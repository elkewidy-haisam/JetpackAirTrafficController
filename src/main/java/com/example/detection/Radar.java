package com.example.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.jetpack.JetPack;
import com.example.util.DebugConfig;

/**
 * Radar.java
 * by Haisam Elkewidy
 * 
 * The radar class is meant for the AirTrafficController to have positional awareness
 * of every jetpack within the locale's airspace.
 * 
 * Known methods:
 * - getJetPackPositions() - gets the positions of every jetpack on the radar map and monitors them
 * - updateJetPackPositions() - frequently update jetpack's positions on the radar,
 *                               intended to help them detour to a certain direction and/or altitude
 * - identifyAircraft() - used to fetch jetpack's identifiers and display them on the map
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
     * Inner class to represent a radar contact
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
        
        // Getters
        public int getX() { return x; }
        public int getY() { return y; }
        public int getAltitude() { return altitude; }
        public long getLastUpdated() { return lastUpdated; }
        public boolean isTracked() { return isTracked; }
        public void setTracked(boolean tracked) { isTracked = tracked; }
        
        @Override
        public String toString() {
            return String.format("Position(X=%d, Y=%d, Alt=%d ft)", x, y, altitude);
        }
    }
    
    /**
     * Default constructor
     */
    public Radar() {
        this.trackedJetpacks = new HashMap<>();
        this.radarRange = 50.0; // 50 miles
        this.scanInterval = 1000; // 1 second
        this.isActive = true;
        this.radarID = "RADAR-01";
        this.centerX = 0;
        this.centerY = 0;
    }
    
    /**
     * Parameterized constructor
     * 
     * @param radarID Unique identifier for the radar
     * @param radarRange Range of the radar in miles
     * @param centerX Center X coordinate
     * @param centerY Center Y coordinate
     */
    public Radar(String radarID, double radarRange, int centerX, int centerY) {
        this.trackedJetpacks = new HashMap<>();
        this.radarRange = radarRange;
        this.scanInterval = 1000;
        this.isActive = true;
        this.radarID = radarID;
        this.centerX = centerX;
        this.centerY = centerY;
    }
    
    /**
     * Gets the positions of every jetpack on the radar map and monitors them
     * 
     * @return Map of jetpacks and their radar contacts
     */
    public Map<JetPack, RadarContact> getJetPackPositions() {
        if (!isActive) {
            if (DebugConfig.VERBOSE) System.out.println("WARNING: Radar is not active!");
            return new HashMap<>();
        }
        
        if (DebugConfig.VERBOSE) {
            System.out.println("\n=== RADAR SCAN - " + radarID + " ===");
            System.out.println("Tracked aircraft: " + trackedJetpacks.size());
            
            for (Map.Entry<JetPack, RadarContact> entry : trackedJetpacks.entrySet()) {
                JetPack jp = entry.getKey();
                RadarContact contact = entry.getValue();
                System.out.println(String.format("  %s: %s", jp.getCallsign(), contact));
            }
            System.out.println("========================\n");
        }
        
        return new HashMap<>(trackedJetpacks);
    }
    
    /**
     * Frequently updates jetpack's positions on the radar
     * 
     * @param jetpack Jetpack to update
     * @param x New X coordinate
     * @param y New Y coordinate
     * @param altitude New altitude
     */
    public void updateJetPackPosition(JetPack jetpack, int x, int y, int altitude) {
        if (!isActive) {
            if (DebugConfig.VERBOSE) System.out.println("WARNING: Cannot update position - Radar is not active!");
            return;
        }
        
        if (trackedJetpacks.containsKey(jetpack)) {
            RadarContact contact = trackedJetpacks.get(jetpack);
            contact.updatePosition(x, y, altitude);
            if (DebugConfig.VERBOSE) {
                System.out.println(String.format("Radar update: %s moved to %s", 
                    jetpack.getCallsign(), contact));
            }
        } else {
            if (DebugConfig.VERBOSE) System.out.println("WARNING: Jetpack " + jetpack.getCallsign() + " not tracked. Adding to radar...");
            addJetpackToRadar(jetpack, x, y, altitude);
        }
    }
    
    /**
     * Adds a jetpack to radar tracking
     * 
     * @param jetpack Jetpack to add
     * @param x Initial X coordinate
     * @param y Initial Y coordinate
     * @param altitude Initial altitude
     */
    public void addJetpackToRadar(JetPack jetpack, int x, int y, int altitude) {
        if (!isActive) {
            if (DebugConfig.VERBOSE) System.out.println("WARNING: Cannot add jetpack - Radar is not active!");
            return;
        }
        
        RadarContact contact = new RadarContact(x, y, altitude);
        trackedJetpacks.put(jetpack, contact);
        if (DebugConfig.VERBOSE) {
            System.out.println(String.format("Radar: Now tracking %s at %s", 
                jetpack.getCallsign(), contact));
        }
    }
    
    /**
     * Removes a jetpack from radar tracking
     * 
     * @param jetpack Jetpack to remove
     */
    public void removeJetpackFromRadar(JetPack jetpack) {
        if (trackedJetpacks.remove(jetpack) != null) {
            if (DebugConfig.VERBOSE) System.out.println("Radar: Stopped tracking " + jetpack.getCallsign());
        } else {
            if (DebugConfig.VERBOSE) System.out.println("WARNING: Jetpack " + jetpack.getCallsign() + " not found on radar");
        }
    }
    
    /**
     * Used to fetch jetpack's identifiers and display them on the map
     * 
     * @param jetpack Jetpack to identify
     * @return String containing jetpack identification information
     */
    public String identifyAircraft(JetPack jetpack) {
        if (!trackedJetpacks.containsKey(jetpack)) {
            return "UNKNOWN AIRCRAFT - Not tracked on radar";
        }
        
        RadarContact contact = trackedJetpacks.get(jetpack);
        StringBuilder identification = new StringBuilder();
        
        identification.append("\n=== AIRCRAFT IDENTIFICATION ===\n");
        identification.append("Callsign: ").append(jetpack.getCallsign()).append("\n");
        identification.append("Serial Number: ").append(jetpack.getSerialNumber()).append("\n");
        identification.append("Owner: ").append(jetpack.getOwnerName()).append("\n");
        identification.append("Manufacturer: ").append(jetpack.getManufacturer()).append("\n");
        identification.append("Model: ").append(jetpack.getModel()).append("\n");
        identification.append("Year: ").append(jetpack.getYear()).append("\n");
        identification.append("Current Position: ").append(contact).append("\n");
        identification.append("Last Updated: ").append(
            new java.util.Date(contact.getLastUpdated())).append("\n");
        identification.append("==============================\n");
        
        String result = identification.toString();
        System.out.println(result);
        return result;
    }
    
    /**
     * Gets all jetpacks within a specific radius
     * 
     * @param centerX Center X coordinate
     * @param centerY Center Y coordinate
     * @param radius Search radius
     * @return List of jetpacks within the radius
     */
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
    
    /**
     * Checks for potential collisions between jetpacks
     * 
     * @param minimumSeparation Minimum safe separation distance
     * @return List of jetpack pairs that are too close
     */
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
                    String warning = String.format(
                        "COLLISION WARNING: %s and %s are %.1f units apart (minimum: %.1f)",
                        jp1.getCallsign(), jp2.getCallsign(), distance, minimumSeparation
                    );
                    warnings.add(warning);
                }
            }
        }
        
        return warnings;
    }
    
    /**
     * Performs a radar sweep and returns a summary
     * 
     * @return Summary string
     */
    public String performRadarSweep() {
        if (!isActive) {
            return "Radar is inactive.";
        }
        
        StringBuilder summary = new StringBuilder();
        summary.append("\n*** RADAR SWEEP ***\n");
        summary.append("Radar ID: ").append(radarID).append("\n");
        summary.append("Range: ").append(radarRange).append(" miles\n");
        summary.append("Tracked aircraft: ").append(trackedJetpacks.size()).append("\n");
        
        if (!trackedJetpacks.isEmpty()) {
            summary.append("\nAircraft in range:\n");
            for (Map.Entry<JetPack, RadarContact> entry : trackedJetpacks.entrySet()) {
                summary.append("  - ").append(entry.getKey().getCallsign())
                       .append(": ").append(entry.getValue()).append("\n");
            }
        }
        
        summary.append("******************\n");
        
        String result = summary.toString();
        System.out.println(result);
        return result;
    }
    
    // Getters and Setters
    
    public double getRadarRange() {
        return radarRange;
    }
    
    public void setRadarRange(double radarRange) {
        this.radarRange = radarRange;
        if (DebugConfig.VERBOSE) System.out.println("Radar range set to: " + radarRange + " miles");
    }
    
    public int getScanInterval() {
        return scanInterval;
    }
    
    public void setScanInterval(int scanInterval) {
        this.scanInterval = scanInterval;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
        if (DebugConfig.VERBOSE) System.out.println("Radar " + radarID + (active ? " activated" : " deactivated"));
    }
    
    public String getRadarID() {
        return radarID;
    }
    
    public int getTrackedJetpackCount() {
        return trackedJetpacks.size();
    }
    
    public int getCenterX() {
        return centerX;
    }
    
    public int getCenterY() {
        return centerY;
    }
    
    @Override
    public String toString() {
        return "Radar{" +
                "radarID='" + radarID + '\'' +
                ", radarRange=" + radarRange +
                ", isActive=" + isActive +
                ", trackedAircraft=" + trackedJetpacks.size() +
                ", center=(" + centerX + "," + centerY + ")" +
                '}';
    }
}
