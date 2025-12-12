/**
 * AirTrafficController.java
 * by Haisam Elkewidy
 *
 * Central controller for managing jetpack flights, city state, and accident alerts.
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
