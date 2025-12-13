/**
 * Executes radio commands on registered flight objects using reflection for loose coupling.
 * 
 * Purpose:
 * Maintains a registry of active flights and their states, executing radio instructions (coordinate changes,
 * altitude adjustments, emergency landings) by dynamically invoking methods on flight objects via reflection.
 * This allows the radio system to send commands without direct dependencies on specific flight implementations.
 * 
 * Key Responsibilities:
 * - Maintain registries of flights and flight states indexed by callsign
 * - Execute coordinate instruction commands on flight objects
 * - Execute altitude instruction commands on flight objects
 * - Execute emergency landing directives on flight states
 * - Use reflection to invoke methods without compile-time dependencies
 * - Handle registration/unregistration of flights entering/leaving airspace
 * 
 * Interactions:
 * - Used by Radio to execute commands on target flights
 * - Invokes methods on JetPackFlight via ReflectionHelper
 * - Invokes methods on JetPackFlightState for parking operations
 * - Registers flights when they enter airspace
 * - Unregisters flights when they land or leave airspace
 * - Provides parking space information for emergency landings
 * 
 * Patterns & Constraints:
 * - Command pattern: encapsulates instructions as executable operations
 * - Registry pattern: maintains flight lookup by callsign
 * - Reflection-based invocation via ReflectionHelper for flexibility
 * - No type safety - relies on duck typing (method name matching)
 * - Graceful handling of reflection errors (logs warning, continues)
 * - Thread-safety not guaranteed - external synchronization required
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
    /** flightRegistry field */
    private Map<String, Object> flightRegistry;
    /** flightStateRegistry field */
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
