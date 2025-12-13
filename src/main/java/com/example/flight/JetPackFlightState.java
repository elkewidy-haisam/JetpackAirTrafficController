/**
 * Manages jetpack parking lifecycle including space selection, parking timers, and state transitions.
 * 
 * Purpose:
 * Encapsulates all parking-related behavior for a jetpack flight, including finding available parking
 * spaces, managing the parking duration timer, and coordinating state transitions between flying and
 * parked states. Integrates with radar communications and movement logging to broadcast parking events.
 * 
 * Key Responsibilities:
 * - Select target parking space from available spaces
 * - Manage parking countdown timer (duration until departure)
 * - Track parked vs. flying state
 * - Find nearest available parking during emergencies
 * - Communicate parking events via radar and movement logs
 * - Trigger UI repaints when parking state changes
 * 
 * Interactions:
 * - Wraps and manages JetPackFlight for parking operations
 * - Queries and occupies ParkingSpace objects
 * - Broadcasts parking events through RadarTapeWindow
 * - Logs parking actions via MovementLogger callback interface
 * - Triggers UI updates via repaint callback
 * - Uses Random for parking duration variability
 * 
 * Patterns & Constraints:
 * - State pattern: manages parked/flying state transitions
 * - Callback pattern: MovementLogger interface for loose coupling
 * - Timer-based parking duration (countdown to departure)
 * - Parking space selection: random from available or nearest for emergency
 * - Parking space occupation/vacation management
 * - Thread-safety depends on caller synchronization
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
    /**
     * Sets the callback function to trigger UI repaints when parking state changes.
     * 
     * @param repaintCallback Runnable that triggers UI repaint
     */
    public void setRepaintCallback(Runnable repaintCallback) {
        this.repaintCallback = repaintCallback;  // Store repaint callback for state change notifications
    }
    
    // The JetPackFlight instance being managed
    private final JetPackFlight flight;
    // Target parking space for current parking operation (null if not heading to parking)
    private ParkingSpace targetParking;
    // Current parking status: true if jetpack is parked, false if flying
    private boolean isParked;
    // Countdown timer for parking duration in update cycles (decrements to zero)
    private int parkingTimeRemaining;
    // Random number generator for parking duration and space selection variability
    private final Random random;
    // List of all available parking spaces in the city
    private final List<ParkingSpace> availableParkingSpaces;
    // Radar tape window for broadcasting parking-related communications
    private RadarTapeWindow radarTapeWindow;
    // Logger callback for recording parking movements to UI
    private MovementLogger movementLogger;
    // Callback to trigger UI repaint after parking state changes
    private Runnable repaintCallback;

    /**
     * Callback interface for logging jetpack movement events to UI.
     */
    public interface MovementLogger {
        /**
         * Appends a movement message to the jetpack movement log.
         * 
         * @param message Movement description to log
         */
        void appendJetpackMovement(String message);
    }

    /**
     * Constructs a new JetPackFlightState for managing parking lifecycle.
     * 
     * @param flight JetPackFlight instance to manage
     * @param parkingSpaces List of available parking spaces in the city
     */
    public JetPackFlightState(JetPackFlight flight, List<ParkingSpace> parkingSpaces) {
        this.flight = flight;  // Store flight reference
        this.availableParkingSpaces = parkingSpaces;  // Store parking space list
        this.random = new Random();  // Initialize random generator
        this.isParked = false;  // Start in flying state
        this.parkingTimeRemaining = 0;  // No parking time initially
        this.repaintCallback = null;  // No callback initially
    }

    /**
     * Sets the radar tape window for broadcasting parking events.
     * 
     * @param window RadarTapeWindow instance for communication display
     */
    public void setRadarTapeWindow(RadarTapeWindow window) {
        this.radarTapeWindow = window;  // Store radar window reference
    }

    /**
     * Sets the movement logger for recording parking events.
     * 
     * @param logger MovementLogger callback for logging
     */
    public void setMovementLogger(MovementLogger logger) {
        this.movementLogger = logger;  // Store logger callback
    }

    /**
     * Updates the parking state each cycle:
     * - If parked: decrements timer and departs when timer reaches zero
     * - If approaching parking: checks distance and parks if close enough
     * - If flying free: randomly selects new parking destination
     */
    public void updateParkingState() {
        if (isParked) {  // Check if currently parked
            parkingTimeRemaining--;  // Decrement parking timer
            if (parkingTimeRemaining <= 0) {  // Check if parking time expired
                departFromParking();  // Trigger departure sequence
            }
        } else if (targetParking != null) {  // Check if heading to parking
            // Calculate distance to target parking space
            double distance = Math.sqrt(
                Math.pow(flight.getX() - targetParking.getX(), 2) +  // Calculate x distance squared
                Math.pow(flight.getY() - targetParking.getY(), 2)   // Calculate y distance squared
            );
            if (distance < 30 && random.nextDouble() < 0.95) {  // Check if close enough with high probability
                arriveAtParking();  // Trigger arrival sequence
            }
        } else if (random.nextDouble() < 0.95) {  // Randomly decide to select parking (95% chance)
            selectRandomParking();  // Choose new parking destination
        }
    }

    /**
     * Selects a random available parking space and sets it as the flight destination.
     * Broadcasts parking selection via radar and movement logger.
     */
    private void selectRandomParking() {
        List<ParkingSpace> available = new ArrayList<>();  // Create list for available spaces
        for (ParkingSpace ps : availableParkingSpaces) {  // Iterate through all parking spaces
            if (!ps.isOccupied()) {  // Check if space is not occupied
                available.add(ps);  // Add to available list
            }
        }
        if (!available.isEmpty()) {  // Check if any parking spaces available
            targetParking = available.get(random.nextInt(available.size()));  // Randomly select from available
            flight.setNewDestination(new Point((int)targetParking.getX(), (int)targetParking.getY()));  // Set as flight destination
            if (radarTapeWindow != null && radarTapeWindow.isVisible()) {  // Check if radar window active
                radarTapeWindow.addMessage(flight.getJetpack().getCallsign() +  // Broadcast parking selection
                    " heading to parking " + targetParking.getId());
            }
            if (movementLogger != null) {  // Check if logger available
                movementLogger.appendJetpackMovement(flight.getJetpack().getCallsign() +  // Log parking selection
                    " 🚀 heading to parking " + targetParking.getId());
            }
        }
    }

    /**
     * Handles jetpack arrival at parking space:
     * - Sets parked state
     * - Occupies parking space
     * - Sets random parking duration (15-45 cycles = 30-90 seconds)
     * - Broadcasts arrival via radar and movement logger
     */
    private void arriveAtParking() {
        isParked = true;  // Set parked state
        targetParking.occupy();  // Mark parking space as occupied
        parkingTimeRemaining = 15 + random.nextInt(31);  // Random duration 15-45 cycles
        if (radarTapeWindow != null && radarTapeWindow.isVisible()) {  // Check if radar window active
            radarTapeWindow.addMessage(flight.getJetpack().getCallsign() +  // Broadcast landing
                " landed at parking " + targetParking.getId());
        }
        if (movementLogger != null) {  // Check if logger available
            int parkingSeconds = (parkingTimeRemaining * 2);  // Convert cycles to approximate seconds
            movementLogger.appendJetpackMovement(flight.getJetpack().getCallsign() +  // Log landing with duration
                " ✓ landed at " + targetParking.getId() + " (parking for ~" + parkingSeconds + "s)");
        }
        if (repaintCallback != null) repaintCallback.run();  // Trigger UI repaint
    }

    /**
     * Handles jetpack departure from parking:
     * - Clears parked state
     * - Vacates parking space
     * - Broadcasts departure via radar and movement logger
     * - Triggers UI repaint
     */
    private void departFromParking() {
        isParked = false;  // Clear parked state
        if (targetParking != null) {  // Check if parking space reference exists
            targetParking.vacate();  // Mark parking space as available
            if (radarTapeWindow != null && radarTapeWindow.isVisible()) {  // Check if radar window active
                radarTapeWindow.addMessage(flight.getJetpack().getCallsign() +  // Broadcast departure
                    " departing from parking " + targetParking.getId());
            }
            if (movementLogger != null) {  // Check if logger available
                movementLogger.appendJetpackMovement(flight.getJetpack().getCallsign() +  // Log departure
                    " 🚀 departing from " + targetParking.getId());
            }
            if (repaintCallback != null) repaintCallback.run();  // Trigger UI repaint
            targetParking = null;  // Clear parking reference
        }
    }

    /**
     * Checks if jetpack is currently parked.
     * 
     * @return true if parked, false if flying
     */
    public boolean isParked() {
        return isParked;  // Return parked state
    }

    /**
     * Gets the available parking spaces for this flight.
     * 
     * @return List of available parking spaces
     */
    public List<ParkingSpace> getAvailableParkingSpaces() {
        return availableParkingSpaces;  // Return parking space list
    }

    /**
     * Update method called each cycle to manage parking state transitions.
     */
    public void update() {
        updateParkingState();  // Delegate to parking state update logic
    }
}
