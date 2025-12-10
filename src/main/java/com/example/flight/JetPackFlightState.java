package com.example.flight;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.parking.ParkingSpace;
import com.example.ui.frames.RadarTapeWindow;

/**
 * JetPackFlightState class - manages jetpack parking behavior
 */
public class JetPackFlightState {
    private JetPackFlight flight;
    private ParkingSpace targetParking;
    private boolean isParked;
    private int parkingTimeRemaining;
    private Random random;
    private List<ParkingSpace> availableParkingSpaces;
    private RadarTapeWindow radarTapeWindow;
    private MovementLogger movementLogger;
    
    public interface MovementLogger {
        void appendJetpackMovement(String message);
    }
    
    public JetPackFlightState(JetPackFlight flight, List<ParkingSpace> parkingSpaces) {
        this.flight = flight;
        this.availableParkingSpaces = parkingSpaces;
        this.random = new Random();
        this.isParked = false;
        this.parkingTimeRemaining = 0;
    }
    
    public void setRadarTapeWindow(RadarTapeWindow window) {
        this.radarTapeWindow = window;
    }
    
    public void setMovementLogger(MovementLogger logger) {
        this.movementLogger = logger;
    }
    
    public void update() {
        if (isParked) {
            // Count down parking time
            parkingTimeRemaining--;
            if (parkingTimeRemaining <= 0) {
                // Depart from parking
                departFromParking();
            }
        } else if (targetParking != null) {
            // Check if we've arrived at parking
            double distance = Math.sqrt(
                Math.pow(flight.getX() - targetParking.getX(), 2) + 
                Math.pow(flight.getY() - targetParking.getY(), 2)
            );
            
            // 50% chance of arrival each update cycle when close (increased from 30%)
            if (distance < 30 && random.nextDouble() < 0.5) {
                arriveAtParking();
            }
        } else if (random.nextDouble() < 0.15) {
            // 15% chance each update to select a parking space (increased from 10%)
            selectRandomParking();
        }
    }
    
    private void selectRandomParking() {
        // Find available parking spaces
        List<ParkingSpace> available = new ArrayList<>();
        for (ParkingSpace ps : availableParkingSpaces) {
            if (!ps.isOccupied()) {
                available.add(ps);
            }
        }
        
        if (!available.isEmpty()) {
            targetParking = available.get(random.nextInt(available.size()));
            flight.setNewDestination(new Point((int)targetParking.getX(), (int)targetParking.getY()));
            
            // Log to radar
            if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
                radarTapeWindow.addMessage(flight.getJetpack().getCallsign() + 
                    " heading to parking " + targetParking.getId());
            }
            
            // Log to movement panel
            if (movementLogger != null) {
                movementLogger.appendJetpackMovement(flight.getJetpack().getCallsign() + 
                    " 🚀 heading to parking " + targetParking.getId());
            }
        }
    }
    
    private void arriveAtParking() {
        isParked = true;
        targetParking.occupy();
        parkingTimeRemaining = 15 + random.nextInt(31); // 15-45 update cycles (30-90 seconds)
        
        // Log to radar
        if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
            radarTapeWindow.addMessage(flight.getJetpack().getCallsign() + 
                " landed at parking " + targetParking.getId());
        }
        
        // Log to movement panel
        if (movementLogger != null) {
            int parkingSeconds = (parkingTimeRemaining * 2); // Each update is ~2 seconds
            movementLogger.appendJetpackMovement(flight.getJetpack().getCallsign() + 
                " ✓ landed at " + targetParking.getId() + " (parking for ~" + parkingSeconds + "s)");
        }
    }
    
    private void departFromParking() {
        isParked = false;
        if (targetParking != null) {
            targetParking.vacate();
            
            // Log to radar
            if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
                radarTapeWindow.addMessage(flight.getJetpack().getCallsign() + 
                    " departing from parking " + targetParking.getId());
            }
            
            // Log to movement panel
            if (movementLogger != null) {
                movementLogger.appendJetpackMovement(flight.getJetpack().getCallsign() + 
                    " 🚀 departing from " + targetParking.getId());
            }
            
            targetParking = null;
        }
    }
    
    public boolean isParked() {
        return isParked;
    }
    
    /**
     * Gets the available parking spaces for this flight
     */
    public List<ParkingSpace> getAvailableParkingSpaces() {
        return availableParkingSpaces;
    }
}
