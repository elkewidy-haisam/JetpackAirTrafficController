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
    private final List<JetPackFlight> flights;
    private final List<AccidentAlert> accidentAlerts;

    public AirTrafficController() {
        this.flights = new ArrayList<>();
        this.accidentAlerts = new ArrayList<>();
    }

    public void addFlight(JetPackFlight flight) {
        flights.add(flight);
    }

    public void removeFlight(JetPackFlight flight) {
        flights.remove(flight);
    }

    public List<JetPackFlight> getFlights() {
        return flights;
    }

    public void reportAccident(AccidentAlert alert) {
        accidentAlerts.add(alert);
        // Additional logic for handling accident alerts can be added here
    }

    public List<AccidentAlert> getAccidentAlerts() {
        return accidentAlerts;
    }
}
