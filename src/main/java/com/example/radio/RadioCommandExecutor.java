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
    // Registry mapping callsigns to flight objects for command execution
    private Map<String, Object> flightRegistry;
    
    // Registry mapping callsigns to flight state objects for parking/landing data
    private Map<String, Object> flightStateRegistry;
    
    /**
     * Constructs a new RadioCommandExecutor with empty registries.
     * Initializes mappings ready to receive flight and state registrations.
     */
    public RadioCommandExecutor() {
        // Initialize empty flight registry for command targeting
        this.flightRegistry = new HashMap<>();
        
        // Initialize empty state registry for accessing parking/landing data
        this.flightStateRegistry = new HashMap<>();
    }
    
    /**
     * Registers a flight object for receiving radio commands.
     * Associates the callsign with the flight instance for command routing.
     * 
     * @param callsign Aircraft identifier for command targeting
     * @param flight Flight object that will receive commands via reflection
     */
    public void registerFlight(String callsign, Object flight) {
        // Map callsign to flight object for command execution routing
        flightRegistry.put(callsign, flight);
    }
    
    /**
     * Registers a flight state object for accessing operational data.
     * Provides access to parking spaces and landing zones for emergency operations.
     * 
     * @param callsign Aircraft identifier
     * @param flightState State object containing parking/landing information
     */
    public void registerFlightState(String callsign, Object flightState) {
        // Map callsign to state object for accessing operational data
        flightStateRegistry.put(callsign, flightState);
    }
    
    /**
     * Unregisters a flight from command execution.
     * Removes both flight object and state from registries when aircraft leaves airspace.
     * 
     * @param callsign Aircraft identifier to unregister
     */
    public void unregisterFlight(String callsign) {
        // Remove flight object from command routing
        flightRegistry.remove(callsign);
        
        // Remove associated state from operational data access
        flightStateRegistry.remove(callsign);
    }
    
    /**
     * Executes coordinate change instruction on a flight via reflection.
     * Routes command to flight's receiveCoordinateInstruction method if registered.
     * Silently ignores command if flight not found in registry.
     * 
     * @param callsign Target aircraft identifier
     * @param newX New X coordinate to navigate to
     * @param newY New Y coordinate to navigate to
     * @param reason Explanation for coordinate change (e.g., "Weather avoidance")
     */
    public void executeCoordinateInstruction(String callsign, int newX, int newY, String reason) {
        // Lookup flight object by callsign
        Object flight = flightRegistry.get(callsign);
        
        // Execute command only if flight is registered
        if (flight != null) {
            // Use reflection to invoke receiveCoordinateInstruction(int, int, String)
            // Allows dynamic command execution without compile-time coupling
            ReflectionHelper.invokeThreeArgMethod(flight, "receiveCoordinateInstruction",
                newX, int.class,
                newY, int.class,
                reason, String.class);
        }
    }
    
    /**
     * Executes altitude change instruction on a flight via reflection.
     * Routes command to flight's receiveAltitudeInstruction method if registered.
     * Silently ignores command if flight not found in registry.
     * 
     * @param callsign Target aircraft identifier
     * @param newAltitude New altitude to climb/descend to
     * @param reason Explanation for altitude change (e.g., "Traffic separation")
     */
    public void executeAltitudeInstruction(String callsign, double newAltitude, String reason) {
        // Lookup flight object by callsign
        Object flight = flightRegistry.get(callsign);
        
        // Execute command only if flight is registered
        if (flight != null) {
            // Use reflection to invoke receiveAltitudeInstruction(double, String)
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
