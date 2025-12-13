/**
 * Manages parking behavior and state transitions for individual jetpack flights including space allocation and timing.
 * 
 * Purpose:
 * Coordinates the complete parking lifecycle for a jetpack including space selection, approach navigation,
 * parking timer management, and departure sequencing. Integrates with parking infrastructure to find
 * available spaces, manages timed parking durations (60-180 seconds), and handles state transitions
 * between flying, approaching parking, parked, and departing states. Provides movement logging and
 * UI repaint coordination for visual feedback.
 * 
 * Key Responsibilities:
 * - Select nearest available parking space when jetpack initiates parking sequence
 * - Navigate jetpack to target parking location with precision landing
 * - Manage parking timer countdown (random 60-180 second duration per park event)
 * - Occupy parking space upon landing and vacate upon departure
 * - Log parking events (approaching, landed, departing) to movement logger
 * - Trigger UI repaints for visual state updates during parking transitions
 * - Handle parking unavailability scenarios (all spaces occupied)
 * - Coordinate with RadarTapeWindow for parking status broadcasts
 * 
 * Interactions:
 * - Paired one-to-one with JetPackFlight for parking behavior management
 * - Queries ParkingSpaceManager for available spaces and nearest space calculations
 * - Updates ParkingSpace occupancy status during land/depart operations
 * - Logs to MovementLogger interface (typically JetpackMovementPanel)
 * - Broadcasts to RadarTapeWindow for operator awareness
 * - Triggers repaint callbacks to update CityMapPanel rendering
 * - Used by CityMapPanel during animation timer updates
 * 
 * Patterns & Constraints:
 * - State pattern manages parking lifecycle (selecting → approaching → parked → departing)
 * - One FlightState instance per JetPackFlight for encapsulated parking logic
 * - Random parking duration: 60-180 seconds provides realistic variability
 * - Parking timer decremented on each animation frame (50ms intervals)
 * - Nearest space selection via Euclidean distance calculation
 * - Occupies parking space atomically to prevent race conditions
 * - Callback-based architecture for logging and UI updates
 * - Handles edge case: no available parking (logs warning, continues flying)
 * 
 * @author Haisam Elkewidy
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
        public void setRepaintCallback(Runnable repaintCallback) {
            this.repaintCallback = repaintCallback;
        }
    private final JetPackFlight flight;
    private ParkingSpace targetParking;
    private boolean isParked;
    private int parkingTimeRemaining;
    private final Random random;
    private final List<ParkingSpace> availableParkingSpaces;
    private RadarTapeWindow radarTapeWindow;
    private MovementLogger movementLogger;
    private Runnable repaintCallback;

    public interface MovementLogger {
        void appendJetpackMovement(String message);
    }

    public JetPackFlightState(JetPackFlight flight, List<ParkingSpace> parkingSpaces) {
        this.flight = flight;
        this.availableParkingSpaces = parkingSpaces;
        this.random = new Random();
        this.isParked = false;
        this.parkingTimeRemaining = 0;
        this.repaintCallback = null;
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
            if (distance < 30 && random.nextDouble() < 0.95) { // Increased probability
                arriveAtParking();
            }
        } else if (random.nextDouble() < 0.95) { // Further increased probability
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
        if (repaintCallback != null) repaintCallback.run();
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
            if (repaintCallback != null) repaintCallback.run();
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
        updateParkingState();
    }
}
