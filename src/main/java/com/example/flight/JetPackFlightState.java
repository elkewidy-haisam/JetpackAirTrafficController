/*
 * JetPackFlightState.java
 * Part of Jetpack Air Traffic Controller
 *
 * Manages jetpack parking behavior and state transitions.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.flight;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.parking.ParkingSpace;
import com.example.ui.frames.RadarTapeWindow;

/**
 * JetPackFlightState manages jetpack parking behavior and state transitions.
 * It tracks parking status, available spaces, and integrates with the radar tape window.
 */
public class JetPackFlightState {
    private final JetPackFlight flight;
    private ParkingSpace targetParking;
    private boolean isParked;
    private int parkingTimeRemaining;
    private final Random random;
    private final List<ParkingSpace> availableParkingSpaces;
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

    /**
     * Updates the parking state each cycle
     */
    public void updateParkingState() {
        if (isParked) {
            parkingTimeRemaining--;
            if (parkingTimeRemaining <= 0) {
                departFromParking();
            }
        } else if (targetParking != null) {
            double distance = Math.sqrt(
                Math.pow(flight.getX() - targetParking.getX(), 2) +
                Math.pow(flight.getY() - targetParking.getY(), 2)
            );
            if (distance < 30 && random.nextDouble() < 0.5) {
                arriveAtParking();
            }
        } else if (random.nextDouble() < 0.15) {
            selectRandomParking();
        }
    }

    private void selectRandomParking() {
        List<ParkingSpace> available = new ArrayList<>();
        for (ParkingSpace ps : availableParkingSpaces) {
            if (!ps.isOccupied()) {
                available.add(ps);
            }
        }
        if (!available.isEmpty()) {
            targetParking = available.get(random.nextInt(available.size()));
            flight.setNewDestination(new Point((int)targetParking.getX(), (int)targetParking.getY()));
            if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
                radarTapeWindow.addMessage(flight.getJetpack().getCallsign() +
                    " heading to parking " + targetParking.getId());
            }
            if (movementLogger != null) {
                movementLogger.appendJetpackMovement(flight.getJetpack().getCallsign() +
                    " 🚀 heading to parking " + targetParking.getId());
            }
        }
    }

    private void arriveAtParking() {
        isParked = true;
        targetParking.occupy();
        parkingTimeRemaining = 15 + random.nextInt(31);
        if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
            radarTapeWindow.addMessage(flight.getJetpack().getCallsign() +
                " landed at parking " + targetParking.getId());
        }
        if (movementLogger != null) {
            int parkingSeconds = (parkingTimeRemaining * 2);
            movementLogger.appendJetpackMovement(flight.getJetpack().getCallsign() +
                " ✓ landed at " + targetParking.getId() + " (parking for ~" + parkingSeconds + "s)");
        }
    }

    private void departFromParking() {
        isParked = false;
        if (targetParking != null) {
            targetParking.vacate();
            if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
                radarTapeWindow.addMessage(flight.getJetpack().getCallsign() +
                    " departing from parking " + targetParking.getId());
            }
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

    public void update() {
        // Stub implementation
    }
}
