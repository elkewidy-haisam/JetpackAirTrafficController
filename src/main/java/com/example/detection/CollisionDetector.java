/**
 * CollisionDetector.java
 * by Haisam Elkewidy
 *
 * This class detects potential collisions between jetpacks and issues warnings to prevent accidents.
 *
 * Variables:
 *   - radarTape (RadarTapeWindow)
 *   - accidentAlert (AccidentAlert)
 *   - accidentCounter (int)
 *
 * Methods:
 *   - CollisionDetector()
 *   - checkCollisions(jetpackFlights, Map<JetPackFlight, flightStates)
 *
 */

package com.example.detection;

import java.util.List;
import java.util.Map;

import com.example.accident.AccidentAlert;
import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.ui.frames.RadarTapeWindow;

/**
 * CollisionDetector detects and reports collisions between jetpack flights.
 * It monitors distances, issues warnings/alerts, and integrates with AccidentAlert for reporting.
 */
public class CollisionDetector {
    
    private static final double WARNING_DISTANCE = 100.0;
    private static final double CRITICAL_DISTANCE = 50.0;
    private static final double ACCIDENT_DISTANCE = 20.0; // Actual collision
    
    private RadarTapeWindow radarTape;
    private AccidentAlert accidentAlert;
    private int accidentCounter = 0;
    
    /**
     * Constructor with accident alert system
     */
    public CollisionDetector() {
        this.accidentAlert = new AccidentAlert();
    }
    
    /**
     * Sets the radar tape window for logging collision alerts
     * 
     * @param radarTape The radar tape window
     */
    public void setRadarTape(RadarTapeWindow radarTape) {
        this.radarTape = radarTape;
    }
    
    /**
     * Gets the accident alert system
     */
    public AccidentAlert getAccidentAlert() {
        return accidentAlert;
    }
    
    /**
     * Checks for collisions between all active jetpack flights
     * 
     * @param jetpackFlights List of all jetpack flights
     * @param flightStates Map of flight states
     */
    public void checkCollisions(List<JetPackFlight> jetpackFlights, 
                               Map<JetPackFlight, JetPackFlightState> flightStates) {
        for (int i = 0; i < jetpackFlights.size(); i++) {
            JetPackFlight flight1 = jetpackFlights.get(i);
            JetPackFlightState state1 = flightStates.get(flight1);
            if (state1 != null && state1.isParked()) continue;
            for (int j = i + 1; j < jetpackFlights.size(); j++) {
                JetPackFlight flight2 = jetpackFlights.get(j);
                JetPackFlightState state2 = flightStates.get(flight2);
                if (state2 != null && state2.isParked()) continue;
                checkCollisionBetween(flight1, flight2);
            }
        }
    }
    
    /**
     * Checks for collision between two specific flights
     * 
     * @param flight1 First flight
     * @param flight2 Second flight
     */
    private void checkCollisionBetween(JetPackFlight flight1, JetPackFlight flight2) {
        double distance = calculateDistance(flight1, flight2);
        
        if (distance < ACCIDENT_DISTANCE) {
            reportAccident(flight1, flight2, distance);
        } else if (distance < CRITICAL_DISTANCE) {
            reportCriticalProximity(flight1, flight2, distance);
        } else if (distance < WARNING_DISTANCE) {
            reportWarningProximity(flight1, flight2, distance);
        }
    }
    
    /**
     * Calculates distance between two flights
     * 
     * @param flight1 First flight
     * @param flight2 Second flight
     * @return Distance between flights
     */
    private double calculateDistance(JetPackFlight flight1, JetPackFlight flight2) {
        double dx = flight1.getX() - flight2.getX();
        double dy = flight1.getY() - flight2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Reports actual collision/accident
     * 
     * @param flight1 First flight
     * @param flight2 Second flight
     * @param distance Distance between flights
     */
    private void reportAccident(JetPackFlight flight1, JetPackFlight flight2, double distance) {
        accidentCounter++;
        String accidentID = "ACC-" + System.currentTimeMillis() + "-" + accidentCounter;
        
        int x = (int)((flight1.getX() + flight2.getX()) / 2);
        int y = (int)((flight1.getY() + flight2.getY()) / 2);
        
        String description = String.format("Mid-air collision between %s and %s at distance %.1f units",
            flight1.getJetpack().getCallsign(), flight2.getJetpack().getCallsign(), distance);
        
        // Report to accident alert system
        accidentAlert.reportAccident(accidentID, x, y, "COLLISION", "SEVERE", description);
        
        // Log to radar tape
        if (radarTape != null && radarTape.isVisible()) {
            radarTape.addMessage("🚨 ACCIDENT: " + flight1.getJetpack().getCallsign() + 
                " and " + flight2.getJetpack().getCallsign() + 
                " COLLIDED at " + String.format("%.1f", distance) + " units! ID: " + accidentID);
        }
        
        // Trigger emergency rerouting for nearby flights
        flight1.setEmergencyReroute(true);
        flight2.setEmergencyReroute(true);
    }
    
    /**
     * Reports critical proximity alert
     * 
     * @param flight1 First flight
     * @param flight2 Second flight
     * @param distance Distance between flights
     */
    private void reportCriticalProximity(JetPackFlight flight1, JetPackFlight flight2, double distance) {
        if (radarTape != null && radarTape.isVisible()) {
            radarTape.addMessage("⚠️ CRITICAL: " + flight1.getJetpack().getCallsign() + 
                " and " + flight2.getJetpack().getCallsign() + 
                " are " + String.format("%.1f", distance) + " units apart - COLLISION RISK!");
        }
    }
    
    /**
     * Reports warning proximity alert
     * 
     * @param flight1 First flight
     * @param flight2 Second flight
     * @param distance Distance between flights
     */
    private void reportWarningProximity(JetPackFlight flight1, JetPackFlight flight2, double distance) {
        if (radarTape != null && radarTape.isVisible()) {
            radarTape.addMessage("⚠️ Warning: " + flight1.getJetpack().getCallsign() + 
                " and " + flight2.getJetpack().getCallsign() + 
                " are " + String.format("%.1f", distance) + " units apart");
        }
    }
    
    /**
     * Gets the warning distance threshold
     * 
     * @return Warning distance
     */
    public static double getWarningDistance() {
        return WARNING_DISTANCE;
    }
    
    /**
     * Gets the critical distance threshold
     * 
     * @return Critical distance
     */
    public static double getCriticalDistance() {
        return CRITICAL_DISTANCE;
    }
}
