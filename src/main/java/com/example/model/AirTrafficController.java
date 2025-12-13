/**
 * Original backend air traffic controller coordinating jetpack operations without GUI dependencies.
 * 
 * Purpose:
 * Standalone ATC implementation for programmatic/command-line use without Swing dependencies. Manages
 * jetpack registry, flight path assignments, and radar tracking. Represents the original backend design
 * before GUI integration via AirTrafficControllerFrame. Can be used for automated testing, batch processing,
 * or server-side operations.
 * 
 * Key Responsibilities:
 * - Maintain registry of active jetpacks
 * - Assign and manage flight paths
 * - Coordinate radar tracking
 * - Provide programmatic ATC API
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class AirTrafficController {
    /** List of all active jetpack flights being managed */
    private final List<JetPackFlight> flights;
    /** List of all accident alerts in the system */
    private final List<AccidentAlert> accidentAlerts;

    /**
     * Constructs a new AirTrafficController with empty flight and alert lists.
     * Initializes the controller ready to manage jetpack operations.
     */
    public AirTrafficController() {
        this.flights = new ArrayList<>();  // Initialize empty flight list
        this.accidentAlerts = new ArrayList<>();  // Initialize empty accident alert list
    }

    /**
     * Adds a flight to the managed flights list.
     * Registers a new jetpack flight for tracking and management.
     * 
     * @param flight the JetPackFlight to add to management
     */
    public void addFlight(JetPackFlight flight) {
        flights.add(flight);  // Add flight to tracked list
    }

    /**
     * Removes a flight from the managed flights list.
     * Unregisters a jetpack flight from tracking (e.g., after landing).
     * 
     * @param flight the JetPackFlight to remove from management
     */
    public void removeFlight(JetPackFlight flight) {
        flights.remove(flight);  // Remove flight from tracked list
    }

    /**
     * Returns the list of all managed flights.
     * Returns the actual list (not a copy), allowing external modification.
     * 
     * @return List of all JetPackFlight objects being managed
     */
    public List<JetPackFlight> getFlights() {
        return flights;  // Return the flights list
    }

    /**
     * Reports and registers an accident alert in the system.
     * Adds the alert to the tracking list for safety monitoring.
     * 
     * @param alert the AccidentAlert to report and track
     */
    public void reportAccident(AccidentAlert alert) {
        accidentAlerts.add(alert);  // Add alert to tracked list
        // Additional logic for handling accident alerts can be added here
        // Future: Broadcast alert to nearby flights
        // Future: Update restricted zones
    }

    /**
     * Returns the list of all accident alerts.
     * Returns the actual list (not a copy), allowing external modification.
     * 
     * @return List of all AccidentAlert objects in the system
     */
    public List<AccidentAlert> getAccidentAlerts() {
        return accidentAlerts;  // Return the accident alerts list
    }
}
