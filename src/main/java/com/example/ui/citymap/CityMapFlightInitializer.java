/**
 * CityMapFlightInitializer.java
 * by Haisam Elkewidy
 *
 * This class handles CityMapFlightInitializer functionality in the Air Traffic Controller system.
 *
 * Methods:
 *   - initializeFlights(jetpacks, jetpackFlights, Map<JetPackFlight, flightStates, parkingSpaces, cityRadio, mapWidth, mapHeight, mapImage, movementLogger, flightStateProvider)
 *
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
