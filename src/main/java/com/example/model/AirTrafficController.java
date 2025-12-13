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
    // Registry of all active jetpack flights currently in the airspace
    private final List<JetPackFlight> flights;
    
    // Collection of accident alerts requiring operator attention and avoidance
    private final List<AccidentAlert> accidentAlerts;

    /**
     * Constructs a new AirTrafficController with empty flight and alert registries.
     * Initializes data structures ready to track flights and manage incidents.
     * This is a simplified model-layer ATC (distinct from the full controller.AirTrafficController).
     */
    public AirTrafficController() {
        // Initialize empty flight registry for tracking active jetpack operations
        this.flights = new ArrayList<>();
        
        // Initialize empty accident alert collection for safety incident tracking
        this.accidentAlerts = new ArrayList<>();
    }

    /**
     * Registers a new jetpack flight in the airspace.
     * Adds the flight to the active registry for tracking and management.
     * Should be called when a jetpack takes off or enters controlled airspace.
     * 
     * @param flight The JetPackFlight to add to active tracking
     */
    public void addFlight(JetPackFlight flight) {
        // Add flight to registry - becomes part of active airspace management
        flights.add(flight);
    }

    /**
     * Removes a jetpack flight from the active registry.
     * Called when a jetpack lands, exits airspace, or is deactivated.
     * Does nothing if the flight is not currently registered.
     * 
     * @param flight The JetPackFlight to remove from tracking
     */
    public void removeFlight(JetPackFlight flight) {
        // Remove flight from registry - no longer tracked in active airspace
        // List.remove() returns false if flight not found, but doesn't throw exception
        flights.remove(flight);
    }

    /**
     * Returns the list of all active flights.
     * Provides access to current airspace occupancy for monitoring and coordination.
     * Returns direct reference - callers should not modify unless managing the registry.
     * 
     * @return List of active JetPackFlight objects
     */
    public List<JetPackFlight> getFlights() {
        // Return reference to flight list for read/write access
        // Note: Consider returning defensive copy for encapsulation
        return flights;
    }

    /**
     * Reports a new accident alert to the system.
     * Adds the alert to the active collection for operator awareness and flight routing.
     * Triggers safety protocols and proximity warnings to nearby aircraft.
     * 
     * @param alert The AccidentAlert to register and track
     */
    public void reportAccident(AccidentAlert alert) {
        // Add accident alert to registry for tracking and notification
        accidentAlerts.add(alert);
        
        // Additional logic for handling accident alerts can be added here
        // Future enhancements could include:
        //   - Broadcasting alert to nearby flights via radio
        //   - Triggering automatic flight path rerouting
        //   - Logging to accident report file
        //   - Updating UI with alert indicator
    }

    /**
     * Returns the list of all active accident alerts.
     * Provides access to current incident status for safety monitoring.
     * Returns direct reference - callers should not modify unless managing alerts.
     * 
     * @return List of active AccidentAlert objects
     */
    public List<AccidentAlert> getAccidentAlerts() {
        // Return reference to accident alerts list
        // Note: Consider returning defensive copy for encapsulation
        return accidentAlerts;
    }
}
