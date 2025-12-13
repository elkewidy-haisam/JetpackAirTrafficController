/**
 * AirTrafficController component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides airtrafficcontroller functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core airtrafficcontroller operations
 * - Maintain necessary state for airtrafficcontroller functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class AirTrafficController {
    // Collection of all active jetpack flights being tracked
    private final List<JetPackFlight> flights;
    // Collection of all active accident alerts in the airspace
    private final List<AccidentAlert> accidentAlerts;

    /**
     * Constructs a new AirTrafficController with empty flight and alert lists.
     */
    public AirTrafficController() {
        // Initialize empty list to hold active flights
        this.flights = new ArrayList<>();
        // Initialize empty list to hold accident alerts
        this.accidentAlerts = new ArrayList<>();
    }

    /**
     * Adds a new flight to the tracking system.
     * 
     * @param flight JetPackFlight to add to active flights
     */
    public void addFlight(JetPackFlight flight) {
        // Append flight to collection of tracked flights
        flights.add(flight);
    }

    /**
     * Removes a flight from the tracking system.
     * Called when flight lands or leaves airspace.
     * 
     * @param flight JetPackFlight to remove from active flights
     */
    public void removeFlight(JetPackFlight flight) {
        // Remove flight from collection of tracked flights
        flights.remove(flight);
    }

    /**
     * Returns the list of all active flights.
     * 
     * @return List of JetPackFlight objects
     */
    public List<JetPackFlight> getFlights() {
        // Return reference to flights collection
        return flights;
    }

    /**
     * Reports an accident by adding it to the alerts system.
     * 
     * @param alert AccidentAlert to report and track
     */
    public void reportAccident(AccidentAlert alert) {
        // Add alert to collection of active accident alerts
        accidentAlerts.add(alert);
        // Stub comment: Additional logic for handling accident alerts can be added here
    }

    /**
     * Returns the list of all active accident alerts.
     * 
     * @return List of AccidentAlert objects
     */
    public List<AccidentAlert> getAccidentAlerts() {
        // Return reference to accident alerts collection
        return accidentAlerts;
    }
}
