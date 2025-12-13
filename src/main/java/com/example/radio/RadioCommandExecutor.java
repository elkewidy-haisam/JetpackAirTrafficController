/**
 * RadioCommandExecutor component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides radiocommandexecutor functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core radiocommandexecutor operations
 * - Maintain necessary state for radiocommandexecutor functionality
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

package com.example.radio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.flight.JetPackFlightState;
import com.example.parking.ParkingSpace;
import com.example.utility.reflection.ReflectionHelper;

/**
 * RadioCommandExecutor - executes radio commands on registered flights using reflection
 */
public class RadioCommandExecutor {
    private Map<String, Object> flightRegistry;
    private Map<String, Object> flightStateRegistry;
    
    public RadioCommandExecutor() {
        this.flightRegistry = new HashMap<>();
        this.flightStateRegistry = new HashMap<>();
    }
    
    /**
     * Registers a flight for radio command execution
     */
    public void registerFlight(String callsign, Object flight) {
        flightRegistry.put(callsign, flight);
    }
    
    /**
     * Registers a flight state for radio command execution
     */
    public void registerFlightState(String callsign, Object flightState) {
        flightStateRegistry.put(callsign, flightState);
    }
    
    /**
     * Unregisters a flight
     */
    public void unregisterFlight(String callsign) {
        flightRegistry.remove(callsign);
        flightStateRegistry.remove(callsign);
    }
    
    /**
     * Executes coordinate change instruction on a flight
     */
    public void executeCoordinateInstruction(String callsign, int newX, int newY, String reason) {
        Object flight = flightRegistry.get(callsign);
        if (flight != null) {
            ReflectionHelper.invokeThreeArgMethod(flight, "receiveCoordinateInstruction",
                newX, int.class,
                newY, int.class,
                reason, String.class);
        }
    }
    
    /**
     * Executes altitude change instruction on a flight
     */
    public void executeAltitudeInstruction(String callsign, double newAltitude, String reason) {
        Object flight = flightRegistry.get(callsign);
        if (flight != null) {
            ReflectionHelper.invokeTwoArgMethod(flight, "receiveAltitudeInstruction",
                newAltitude, double.class,
                reason, String.class);
        }
    }
    
    /**
     * Executes emergency landing instruction on a flight
     */
    public void executeEmergencyLandingInstruction(String callsign, String directive) {
        Object flight = flightRegistry.get(callsign);
        Object flightState = flightStateRegistry.get(callsign);
        
        if (flight != null && flightState != null && flightState instanceof JetPackFlightState) {
            // Get parking spaces from flight state
            List<ParkingSpace> parkingSpaces = ((JetPackFlightState) flightState).getAvailableParkingSpaces();
            
            ReflectionHelper.invokeTwoArgMethod(flight, "receiveEmergencyLandingInstruction",
                parkingSpaces, java.util.List.class,
                directive, String.class);
        } else if (flight != null) {
            // Fallback: if no state registered, pass null (will cause halt in place)
            ReflectionHelper.invokeTwoArgMethod(flight, "receiveEmergencyLandingInstruction",
                null, java.util.List.class,
                directive, String.class);
        }
    }
    
    /**
     * Checks if a flight is registered
     */
    public boolean isFlightRegistered(String callsign) {
        return flightRegistry.containsKey(callsign);
    }
    
    /**
     * Gets the number of registered flights
     */
    public int getRegisteredFlightCount() {
        return flightRegistry.size();
    }
}
