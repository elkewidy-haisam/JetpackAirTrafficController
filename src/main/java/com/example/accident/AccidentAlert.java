package com.example.accident;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.jetpack.JetPack;
import com.example.util.DebugConfig;

/**
 * AccidentAlert.java
 * by Haisam Elkewidy
 * 
 * This class alerts jetpack pilots of any accidents on the road, be it at intersections
 * or highways, or even of collisions between other jetpacks.
 * 
 * Known methods:
 * - alertJetpacksOfAccident() - alerts nearby jetpacks of an accident in a certain section of the city
 * - removeAlert() - removes the alert for the accident if it has been addressed already
 */
public class AccidentAlert {
    
    /**
     * Inner class to represent an accident
     */
    public static class Accident {
        private String accidentID;
        private int x;
        private int y;
        private String type;
        private String severity;
        private String description;
        private long timestamp;
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
            return String.format("Accident[ID=%s, Type=%s, Severity=%s, Location=(%d,%d), Active=%s]",
                accidentID, type, severity, x, y, isActive);
        }
    }
    
    private Map<String, Accident> activeAccidents;
    private List<String> alertHistory;
    private String alertSystemID;
    
    /**
     * Default constructor
     */
    public AccidentAlert() {
        this.activeAccidents = new HashMap<>();
        this.alertHistory = new ArrayList<>();
        this.alertSystemID = "ALERT-SYSTEM-01";
    }
    
    /**
     * Parameterized constructor
     * 
     * @param alertSystemID Unique identifier for the alert system
     */
    public AccidentAlert(String alertSystemID) {
        this.activeAccidents = new HashMap<>();
        this.alertHistory = new ArrayList<>();
        this.alertSystemID = alertSystemID;
    }
    
    /**
     * Reports a new accident to the system
     * 
     * @param accidentID Unique identifier for the accident
     * @param x X coordinate of accident location
     * @param y Y coordinate of accident location
     * @param type Type of accident (e.g., "COLLISION", "GROUND_ACCIDENT", "JETPACK_CRASH")
     * @param severity Severity level (e.g., "MINOR", "MODERATE", "SEVERE", "CRITICAL")
     * @param description Description of the accident
     */
    public void reportAccident(String accidentID, int x, int y, String type, String severity, String description) {
        Accident accident = new Accident(accidentID, x, y, type, severity, description);
        activeAccidents.put(accidentID, accident);
        
        String alertMessage = String.format(
            "*** ACCIDENT REPORTED *** ID: %s | Type: %s | Severity: %s | Location: (%d,%d) | %s",
            accidentID, type, severity, x, y, description
        );
        
        alertHistory.add(alertMessage);
        if (DebugConfig.VERBOSE) {
            System.out.println("\n" + alertMessage + "\n");
        }
    }
    
    /**
     * Alerts nearby jetpacks of an accident in a certain section of the city
     * 
     * @param accidentID ID of the accident
     * @param jetpacks List of jetpacks to check
     * @param alertRadius Radius within which jetpacks should be alerted
     * @return List of alerted jetpacks
     */
    public List<JetPack> alertJetpacksOfAccident(String accidentID, List<JetPack> jetpacks, double alertRadius) {
        if (!activeAccidents.containsKey(accidentID)) {
            if (DebugConfig.VERBOSE) System.out.println("ERROR: Accident ID " + accidentID + " not found.");
            return new ArrayList<>();
        }
        
        Accident accident = activeAccidents.get(accidentID);
        List<JetPack> alertedJetpacks = new ArrayList<>();
        
        if (DebugConfig.VERBOSE) {
            System.out.println("\n=== ALERTING JETPACKS ===");
            System.out.println("Accident: " + accident);
            System.out.println("Alert Radius: " + alertRadius + " units\n");
        }
        
        // In a real implementation, you would calculate distance from jetpack positions
        // For now, we'll alert all jetpacks in the list
        for (JetPack jetpack : jetpacks) {
            String alertMessage = String.format(
                "ALERT to %s: %s accident at location (%d,%d). Severity: %s. %s. Avoid area!",
                jetpack.getCallsign(), accident.getType(), accident.getX(), accident.getY(),
                accident.getSeverity(), accident.getDescription()
            );
            
            if (DebugConfig.VERBOSE) System.out.println("[ALERT] " + alertMessage);
            alertedJetpacks.add(jetpack);
            alertHistory.add(alertMessage);
        }
        
        if (DebugConfig.VERBOSE) {
            System.out.println("\nTotal jetpacks alerted: " + alertedJetpacks.size());
            System.out.println("========================\n");
        }
        
        return alertedJetpacks;
    }
    
    /**
     * Alerts jetpacks near a specific location
     * 
     * @param accidentID ID of the accident
     * @param jetpackPositions Map of jetpacks and their positions
     * @param alertRadius Radius within which jetpacks should be alerted
     * @return List of alerted jetpacks
     */
    public List<JetPack> alertJetpacksOfAccident(String accidentID, 
                                                   Map<JetPack, int[]> jetpackPositions, 
                                                   double alertRadius) {
        if (!activeAccidents.containsKey(accidentID)) {
            // Accident not found
            return new ArrayList<>();
        }
        
        Accident accident = activeAccidents.get(accidentID);
        List<JetPack> alertedJetpacks = new ArrayList<>();
        
        // Alerting jetpacks
        
        for (Map.Entry<JetPack, int[]> entry : jetpackPositions.entrySet()) {
            JetPack jetpack = entry.getKey();
            int[] position = entry.getValue(); // [x, y]
            
            double distance = Math.sqrt(
                Math.pow(position[0] - accident.getX(), 2) + 
                Math.pow(position[1] - accident.getY(), 2)
            );
            
            if (distance <= alertRadius) {
                String alertMessage = String.format(
                    "ALERT to %s: %s accident %.1f units away at location (%d,%d). Severity: %s. %s. Avoid area!",
                    jetpack.getCallsign(), accident.getType(), distance, accident.getX(), accident.getY(),
                    accident.getSeverity(), accident.getDescription()
                );
                
                // Alert sent
                alertedJetpacks.add(jetpack);
                alertHistory.add(alertMessage);
            }
        }
        
        // Alerts completed
        
        return alertedJetpacks;
    }
    
    /**
     * Removes the alert for the accident if it has been addressed already
     * 
     * @param accidentID ID of the accident to remove
     * @return true if successfully removed, false otherwise
     */
    public boolean removeAlert(String accidentID) {
        if (!activeAccidents.containsKey(accidentID)) {
            // Accident not found
            return false;
        }
        
        Accident accident = activeAccidents.get(accidentID);
        accident.setActive(false);
        activeAccidents.remove(accidentID);
        
        String message = String.format(
            "*** ACCIDENT CLEARED *** ID: %s | Location: (%d,%d) | Area is now safe for transit.",
            accidentID, accident.getX(), accident.getY()
        );
        
        // Accident cleared
        alertHistory.add(message);
        
        return true;
    }
    
    /**
     * Updates the severity of an existing accident
     * 
     * @param accidentID ID of the accident
     * @param newSeverity New severity level
     */
    public void updateAccidentSeverity(String accidentID, String newSeverity) {
        if (!activeAccidents.containsKey(accidentID)) {
            // Accident not found
            return;
        }
        
        Accident accident = activeAccidents.get(accidentID);
        String oldSeverity = accident.severity;
        accident.severity = newSeverity;
        
        String message = String.format(
            "Accident %s severity updated: %s -> %s",
            accidentID, oldSeverity, newSeverity
        );
        
        // Severity updated
        alertHistory.add(message);
    }
    
    /**
     * Gets all active accidents
     * 
     * @return Map of active accidents
     */
    public Map<String, Accident> getActiveAccidents() {
        return new HashMap<>(activeAccidents);
    }
    
    /**
     * Gets accidents near a specific location
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @param radius Search radius
     * @return List of nearby accidents
     */
    public List<Accident> getAccidentsNearLocation(int x, int y, double radius) {
        List<Accident> nearbyAccidents = new ArrayList<>();
        
        for (Accident accident : activeAccidents.values()) {
            double distance = Math.sqrt(
                Math.pow(accident.getX() - x, 2) + 
                Math.pow(accident.getY() - y, 2)
            );
            
            if (distance <= radius) {
                nearbyAccidents.add(accident);
            }
        }
        
        return nearbyAccidents;
    }
    
    /**
     * Checks if a location is safe (no accidents nearby)
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @param safeRadius Safe distance from any accident
     * @return true if location is safe, false otherwise
     */
    public boolean isLocationSafe(int x, int y, double safeRadius) {
        for (Accident accident : activeAccidents.values()) {
            double distance = Math.sqrt(
                Math.pow(accident.getX() - x, 2) + 
                Math.pow(accident.getY() - y, 2)
            );
            
            if (distance <= safeRadius) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Prints all active accidents
     */
    public void printActiveAccidents() {
        // Print method disabled for performance
    }
    
    /**
     * Prints alert history
     */
    public void printAlertHistory() {
        // Print method disabled for performance
    }
    
    /**
     * Clears all alert history
     */
    public void clearAlertHistory() {
        alertHistory.clear();
        // History cleared
    }
    
    // Getters
    
    public String getAlertSystemID() {
        return alertSystemID;
    }
    
    public int getActiveAccidentCount() {
        return activeAccidents.size();
    }
    
    public List<String> getAlertHistory() {
        return new ArrayList<>(alertHistory);
    }
    
    @Override
    public String toString() {
        return "AccidentAlert{" +
                "alertSystemID='" + alertSystemID + '\'' +
                ", activeAccidents=" + activeAccidents.size() +
                ", totalAlerts=" + alertHistory.size() +
                '}';
    }
}
