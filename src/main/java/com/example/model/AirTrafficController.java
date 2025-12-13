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
    /** List of currently active jetpack flights being tracked */
    private final List<JetPackFlight> flights;
    /** List of accident alerts that have been reported */
    private final List<AccidentAlert> accidentAlerts;

    /**
     * Constructs a new AirTrafficController.
     * Initializes empty flight and accident alert lists.
     */
    public AirTrafficController() {
        this.flights = new ArrayList<>();          // Initialize empty flights list
        this.accidentAlerts = new ArrayList<>();  // Initialize empty accidents list
    }

    /**
     * Adds a jetpack flight to the tracking system.
     * 
     * @param flight the JetPackFlight to add
     */
    public void addFlight(JetPackFlight flight) {
        flights.add(flight);  // Append flight to tracking list
    }

    /**
     * Removes a jetpack flight from the tracking system.
     * 
     * @param flight the JetPackFlight to remove
     */
    public void removeFlight(JetPackFlight flight) {
        flights.remove(flight);  // Remove flight from tracking list
    }

    /**
     * Returns the list of currently tracked flights.
     * @return List of JetPackFlight objects
     */
    public List<JetPackFlight> getFlights() {
        return flights;  // Return the flights list
    }

    /**
     * Reports an accident alert to the system.
     * Alert is logged and stored for reference.
     * 
     * @param alert the AccidentAlert to report
     */
    public void reportAccident(AccidentAlert alert) {
        accidentAlerts.add(alert);  // Add alert to the list
        // Additional logic for handling accident alerts can be added here
    }

    /**
     * Returns the list of all accident alerts.
     * @return List of AccidentAlert objects
     */
    public List<AccidentAlert> getAccidentAlerts() {
        return accidentAlerts;  // Return the accident alerts list
    }
}
