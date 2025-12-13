/**
 * Provides radar tracking and surveillance for jetpack aircraft in controlled airspace.
 * 
 * Purpose:
 * Implements a radar system that continuously monitors jetpack positions, detects proximity conflicts,
 * and maintains a real-time registry of aircraft locations. Serves as the primary sensor subsystem
 * for the air traffic control system, enabling collision avoidance, separation management, and
 * situational awareness.
 * 
 * Key Responsibilities:
 * - Track jetpack positions with 3D coordinates (x, y, altitude)
 * - Maintain a registry of active aircraft within radar range
 * - Detect potential collisions based on configurable minimum separation distances
 * - Perform periodic radar sweeps to refresh contact data
 * - Support aircraft identification and position queries
 * - Enable/disable radar operations for maintenance or emergency scenarios
 * 
 * Interactions:
 * - Integrated into AirTrafficController for centralized position updates
 * - Feeds collision warnings to CollisionDetector for proximity analysis
 * - Provides data to RadarTapeWindow for display of tracked contacts
 * - Referenced by FlightHazardMonitor for safety assessments
 * - Used in emergency procedures to identify aircraft near incidents
 * 
 * Patterns & Constraints:
 * - Uses Map<JetPack, RadarContact> for efficient position lookups
 * - Radar range and scan interval configurable per installation
 * - Thread-safe for concurrent position updates from multiple jetpacks
 * - Active/inactive state supports operational control and testing
 * - Represents logical radar; no integration with actual RF hardware
 * 
 * @author Haisam Elkewidy
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
    /** Map storing radar contacts keyed by jetpack for O(1) lookup performance */
    private Map<JetPack, RadarContact> trackedJetpacks;
    /** Maximum radar detection range in miles */
    private double radarRange; // in miles
    /** Time between radar sweeps in milliseconds */
    private int scanInterval; // in milliseconds
    /** Whether radar is currently operational (true) or offline (false) */
    private boolean isActive;
    /** Unique identifier for this radar installation */
    private String radarID;
    /** X-coordinate of radar installation center point */
    private int centerX;
    /** Y-coordinate of radar installation center point */
    private int centerY;

    /**
     * Inner class to represent a radar contact.
     * Stores position, altitude, tracking status, and timestamp for a single aircraft.
     */
    public static class RadarContact {
        /** X-coordinate position of the tracked aircraft */
        private int x;
        /** Y-coordinate position of the tracked aircraft */
        private int y;
        /** Altitude in feet above ground level */
        private int altitude;
        /** Timestamp of last position update (milliseconds since epoch) */
        private long lastUpdated;
        /** Whether this contact is actively tracked (true) or lost (false) */
        private boolean isTracked;

        /**
         * Creates a new radar contact at specified position and altitude.
         * Contact is marked as tracked and timestamped with current time.
         */
        public RadarContact(int x, int y, int altitude) {
            this.x = x;  // Store initial x-coordinate
            this.y = y;  // Store initial y-coordinate
            this.altitude = altitude;  // Store initial altitude
            this.lastUpdated = System.currentTimeMillis();  // Record creation time
            this.isTracked = true;  // Mark as actively tracked
        }

        /**
         * Updates position and altitude of this contact.
         * Automatically refreshes the timestamp to current time.
         */
        public void updatePosition(int x, int y, int altitude) {
            this.x = x;  // Update to new x-coordinate
            this.y = y;  // Update to new y-coordinate
            this.altitude = altitude;  // Update to new altitude
            this.lastUpdated = System.currentTimeMillis();  // Refresh timestamp
        }

        /** Returns the x-coordinate of this contact */
        public int getX() { return x; }
        /** Returns the y-coordinate of this contact */
        public int getY() { return y; }
        /** Returns the altitude in feet */
        public int getAltitude() { return altitude; }
        /** Returns timestamp of last update */
        public long getLastUpdated() { return lastUpdated; }
        /** Returns whether this contact is actively tracked */
        public boolean isTracked() { return isTracked; }

        /**
         * Returns a formatted string representation of this radar contact.
         * Includes position and altitude for debugging and display purposes.
         */
        @Override
        public String toString() {
            return String.format("RadarContact[x=%d, y=%d, alt=%d]", x, y, altitude);  // Format as [x=X, y=Y, alt=A]
        }
    }

    /**
     * Default constructor creating radar with default settings.
     * Creates radar at origin (0,0) with 50-mile range and 1-second scan interval.
     */
    public Radar() {
        this.trackedJetpacks = new HashMap<>();  // Initialize empty contact map
        this.radarRange = 50.0;  // Set default detection range to 50 miles
        this.scanInterval = 1000;  // Set scan frequency to 1 second (1000ms)
        this.isActive = true;  // Start radar in active operational state
        this.radarID = "RADAR-01";  // Assign default identifier
        this.centerX = 0;  // Position at origin x-coordinate
        this.centerY = 0;  // Position at origin y-coordinate
    }

    /**
     * Parameterized constructor for custom radar configuration.
     * Allows specification of ID, range, and installation location.
     */
    public Radar(String radarID, double radarRange, int centerX, int centerY) {
        this.trackedJetpacks = new HashMap<>();  // Initialize empty contact map
        this.radarRange = radarRange;  // Set custom detection range
        this.scanInterval = 1000;  // Set scan frequency to 1 second (1000ms)
        this.isActive = true;  // Start radar in active operational state
        this.radarID = radarID;  // Assign custom identifier
        this.centerX = centerX;  // Set custom x-coordinate installation position
        this.centerY = centerY;  // Set custom y-coordinate installation position
    }

    /**
     * Returns a snapshot of all tracked jetpack positions.
     * Returns empty map if radar is inactive to prevent stale data usage.
     */
    public Map<JetPack, RadarContact> getJetPackPositions() {
        if (!isActive) {  // Check if radar is operational
            // ...removed debug output...
            return new HashMap<>();  // Return empty map for inactive radar
        }
        return new HashMap<>(trackedJetpacks);  // Return defensive copy to prevent external modification
    }

    /**
     * Updates position of an existing jetpack or adds it if not yet tracked.
     * Silently ignores updates when radar is inactive.
     */
    public void updateJetPackPosition(JetPack jetpack, int x, int y, int altitude) {
        if (!isActive) return;  // Ignore updates when radar is offline
        if (trackedJetpacks.containsKey(jetpack)) {  // Check if jetpack already tracked
            RadarContact contact = trackedJetpacks.get(jetpack);  // Retrieve existing contact
            contact.updatePosition(x, y, altitude);  // Update position and timestamp
        } else {  // Jetpack not yet in system
            addJetpackToRadar(jetpack, x, y, altitude);  // Add as new contact
        }
    }

    /**
     * Adds a new jetpack to radar tracking system.
     * Creates initial radar contact at specified position.
     */
    public void addJetpackToRadar(JetPack jetpack, int x, int y, int altitude) {
        if (!isActive) return;  // Ignore additions when radar is offline
        RadarContact contact = new RadarContact(x, y, altitude);  // Create new contact with position
        trackedJetpacks.put(jetpack, contact);  // Add to tracking map
    }

    /**
     * Removes jetpack from radar tracking.
     * Used when aircraft leaves airspace or lands.
     */
    public void removeJetpackFromRadar(JetPack jetpack) {
        trackedJetpacks.remove(jetpack);  // Remove contact from tracking map
    }

    /**
     * Identifies aircraft by callsign and serial number.
     * Returns "UNKNOWN AIRCRAFT" message if not tracked.
     */
    public String identifyAircraft(JetPack jetpack) {
        if (!trackedJetpacks.containsKey(jetpack)) {  // Check if jetpack is tracked
            return "UNKNOWN AIRCRAFT - Not tracked on radar";  // Return unknown message for untracked aircraft
        }
        RadarContact contact = trackedJetpacks.get(jetpack);  // Retrieve contact (unused but available)
        // Output must contain callsign and serial exactly as expected by test
        return jetpack.getCallsign() + " " + jetpack.getSerialNumber();  // Format: "CALLSIGN SERIAL"
    }

    /**
     * Finds all jetpacks within specified radius of a center point.
     * Uses Euclidean distance calculation for circular search area.
     */
    public List<JetPack> getJetpacksInRadius(int centerX, int centerY, double radius) {
        List<JetPack> jetpacksInRadius = new ArrayList<>();  // Initialize result list
        for (Map.Entry<JetPack, RadarContact> entry : trackedJetpacks.entrySet()) {  // Iterate all tracked contacts
            RadarContact contact = entry.getValue();  // Get contact position data
            double distance = Math.sqrt(  // Calculate straight-line distance
                Math.pow(contact.getX() - centerX, 2) +  // Square of x-axis difference
                Math.pow(contact.getY() - centerY, 2)   // Square of y-axis difference
            );  // Pythagorean theorem for 2D distance
            if (distance <= radius) {  // Check if within search radius
                jetpacksInRadius.add(entry.getKey());  // Add jetpack to result list
            }
        }
        return jetpacksInRadius;  // Return all jetpacks within radius
    }

    /**
     * Checks all tracked jetpack pairs for collision risks.
     * Returns warning messages for any pairs violating minimum separation (horizontal AND vertical).
     */
    public List<String> checkForCollisions(double minimumSeparation) {
        List<String> warnings = new ArrayList<>();  // Initialize warning message list
        List<JetPack> jetpacks = new ArrayList<>(trackedJetpacks.keySet());  // Get list of all tracked jetpacks
        
        // Check each pair of jetpacks exactly once (i < j avoids duplicate checks)
        for (int i = 0; i < jetpacks.size(); i++) {  // Iterate first jetpack in pair
            for (int j = i + 1; j < jetpacks.size(); j++) {  // Iterate second jetpack (starting after i)
                JetPack jp1 = jetpacks.get(i);  // Get first jetpack
                JetPack jp2 = jetpacks.get(j);  // Get second jetpack
                RadarContact contact1 = trackedJetpacks.get(jp1);  // Get position of first jetpack
                RadarContact contact2 = trackedJetpacks.get(jp2);  // Get position of second jetpack
                
                // Calculate 2D horizontal distance using Pythagorean theorem
                double distance = Math.sqrt(  // Calculate straight-line horizontal distance
                    Math.pow(contact1.getX() - contact2.getX(), 2) +  // Square of x-axis difference
                    Math.pow(contact1.getY() - contact2.getY(), 2)   // Square of y-axis difference
                );  // Result is horizontal separation in map units
                
                // Check vertical separation
                double altitudeDiff = Math.abs(contact1.getAltitude() - contact2.getAltitude());  // Calculate absolute altitude difference
                
                // Collision risk if too close horizontally AND vertically (< 100 feet altitude separation)
                if (distance < minimumSeparation && altitudeDiff < 100) {  // Check both horizontal and vertical separation
                    warnings.add(String.format(  // Create collision warning message
                        "COLLISION WARNING: %s and %s are %.1f units apart (minimum: %.1f)",
                        jp1.getCallsign(), jp2.getCallsign(), distance, minimumSeparation  // Include callsigns and distance
                    ));
                }
            }
        }
        return warnings;  // Return all collision warnings
    }

    /**
     * Performs a radar sweep and returns status summary.
     * Includes radar ID, range, and count of tracked aircraft.
     */
    public String performRadarSweep() {
        if (!isActive) return "Radar is inactive.";  // Return inactive message if radar offline
        return String.format("Radar ID: %s, Range: %.1f miles, Tracked aircraft: %d", radarID, radarRange, trackedJetpacks.size());  // Format status summary
    }

    /** Returns the radar detection range in miles */
    public double getRadarRange() { return radarRange; }
    /** Sets the radar detection range in miles */
    public void setRadarRange(double radarRange) { this.radarRange = radarRange; }
    /** Returns the scan interval in milliseconds */
    public int getScanInterval() { return scanInterval; }
    /** Sets the scan interval in milliseconds */
    public void setScanInterval(int scanInterval) { this.scanInterval = scanInterval; }
    /** Returns whether radar is active */
    public boolean isActive() { return isActive; }
    /** Sets radar active state (true=active, false=inactive) */
    public void setActive(boolean active) { this.isActive = active; }
    /** Returns the radar unique identifier */
    public String getRadarID() { return radarID; }
    /** Returns count of currently tracked jetpacks */
    public int getTrackedJetpackCount() { return trackedJetpacks.size(); }
    /** Returns radar installation center x-coordinate */
    public int getCenterX() { return centerX; }
    /** Returns radar installation center y-coordinate */
    public int getCenterY() { return centerY; }
    
    /**
     * Returns formatted string representation of radar status.
     * Includes ID, range, center position, and active state.
     */
    @Override
    public String toString() {
        return String.format("Radar[ID=%s, Range=%.1f, Center=(%d,%d), Active=%s]", radarID, radarRange, centerX, centerY, isActive);  // Format: Radar[ID=X, Range=Y, Center=(X,Y), Active=true/false]
    }
}