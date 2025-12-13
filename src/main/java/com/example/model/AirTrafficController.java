/**
 * Backend domain controller managing flights and accident alerts for non-GUI operational scenarios.
 * 
 * Purpose:
 * Provides a simplified, programmatic API for air traffic control operations without GUI dependencies.
 * Manages collections of active jetpack flights and accident alerts for backend services, command-line
 * tools, batch processing, and automated testing. Serves as an alternative to the GUI-based
 * AirTrafficControllerFrame for scenarios where visual interfaces are not required or desired.
 * 
 * Key Responsibilities:
 * - Maintain registry of active JetPackFlight instances
 * - Track reported accident alerts and incidents
 * - Support flight addition and removal operations
 * - Provide queries for flight and accident collections
 * - Enable backend integration without Swing/UI coupling
 * - Support automated testing with programmatic control
 * - Facilitate command-line utilities and batch operations
 * 
 * Interactions:
 * - Alternative to AirTrafficControllerFrame for non-GUI scenarios
 * - Manages simplified JetPackFlight model objects (backend version)
 * - Integrates with AccidentAlert for incident tracking
 * - Can be used in testing frameworks without UI initialization
 * - Supports scripting and automation scenarios
 * - Enables server-side jetpack tracking services
 * - Coordinates with FlightPath for backend flight planning
 * 
 * Patterns & Constraints:
 * - Simple domain controller pattern for backend operations
 * - No UI dependencies (contrast with AirTrafficControllerFrame)
 * - Lightweight flight tracking with basic add/remove/query operations
 * - Direct list access for flexibility (no encapsulation overhead)
 * - Thread-safe reads; external synchronization for modifications
 * - Suitable for testing, CLI tools, services, and batch processing
 * - See ARCHITECTURE_NOTES.md for GUI vs. backend architecture details
 * - Coexists with GUI implementation without conflict
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
