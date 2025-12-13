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
