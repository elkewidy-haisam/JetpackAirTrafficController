/**
 * CityMapFlightInitializer component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides citymapflightinitializer functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core citymapflightinitializer operations
 * - Maintain necessary state for citymapflightinitializer functionality
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

package com.example.ui.citymap;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.jetpack.JetPack;
import com.example.parking.ParkingSpace;
import com.example.radio.Radio;

/**
 * CityMapFlightInitializer - Handles initialization of jetpack flights
 */
public class CityMapFlightInitializer {
    
    /**
     * Initializes all jetpack flights with random positions and destinations
     */
    public static void initializeFlights(ArrayList<JetPack> jetpacks,
                                        List<JetPackFlight> jetpackFlights,
                                        Map<JetPackFlight, JetPackFlightState> flightStates,
                                        List<ParkingSpace> parkingSpaces,
                                        Radio cityRadio,
                                        int mapWidth,
                                        int mapHeight,
                                        java.awt.image.BufferedImage mapImage,
                                        MovementLogger movementLogger,
                                        FlightStateProvider flightStateProvider) {
        Random rand = new Random();
        
        for (JetPack jp : jetpacks) {
            Point start = new Point(
                50 + rand.nextInt(Math.max(100, mapWidth - 100)), 
                50 + rand.nextInt(Math.max(100, mapHeight - 100))
            );
            Point destination = new Point(
                50 + rand.nextInt(Math.max(100, mapWidth - 100)), 
                50 + rand.nextInt(Math.max(100, mapHeight - 100))
            );
            Color color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            
            JetPackFlight flight = new JetPackFlight(jp, start, destination, color);
            
            flight.setMovementLogger(message -> movementLogger.logMovement(message));
            flight.setFlightStateProvider(f -> flightStateProvider.getFlightState(f));
            flight.setMapImage(mapImage);
            
            // Register flight with radio for command execution
            cityRadio.registerFlight(jp.getCallsign(), flight);
            
            jetpackFlights.add(flight);
            
            JetPackFlightState state = new JetPackFlightState(flight, parkingSpaces);
            state.setMovementLogger(message -> movementLogger.logMovement(message));
            
            flightStates.put(flight, state);
            
            // Register flight state with radio so it can access parking spaces for emergency landings
            cityRadio.registerFlightState(jp.getCallsign(), state);
        }
    }
    
    /**
     * Interface for logging flight movements
     */
    public interface MovementLogger {
        void logMovement(String message);
    }
    
    /**
     * Interface for providing flight state
     */
    public interface FlightStateProvider {
        JetPackFlightState getFlightState(JetPackFlight flight);
    }
}
