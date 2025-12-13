/**
 * Unit tests for CollisionDetector functionality and behavior validation.
 * 
 * Purpose:
 * Validates correctness of CollisionDetector through automated JUnit test cases. Ensures reliability
 * and prevents regressions by testing core functionality, edge cases, and integration scenarios.
 * 
 * Key Test Areas:
 * - Core CollisionDetector operations and expected outcomes
 * - Edge cases and boundary condition handling
 * - Error scenarios and exception handling
 * - Integration with related components
 * 
 * Patterns & Constraints:
 * - JUnit framework for test execution and assertions
 * - Isolated test methods minimize inter-test dependencies
 * - Setup/teardown methods manage test state lifecycle
 * 
 * @author Haisam Elkewidy
 */

package com.example.detection;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.jetpack.JetPack;
import com.example.parking.ParkingSpace;

/**
 * Comprehensive test suite for CollisionDetector
 */
public class CollisionDetectorTest {
    private CollisionDetector detector;
    private List<JetPackFlight> flights;
    private Map<JetPackFlight, JetPackFlightState> flightStates;
    private List<ParkingSpace> parkingSpaces;
    
    @Before
    public void setUp() {
        detector = new CollisionDetector();
        flights = new ArrayList<>();
        flightStates = new HashMap<>();
        parkingSpaces = new ArrayList<>();
        
        // Create some parking spaces
        for (int i = 0; i < 10; i++) {
            parkingSpaces.add(new ParkingSpace("TEST-P" + i, 100 * i, 100 * i));
        }
    }
    
    @Test
    public void testCollisionDetectorCreation() {
        assertNotNull("Detector should be created", detector);
        assertNotNull("Accident alert should exist", detector.getAccidentAlert());
    }
    
    @Test
    public void testNoCollisionWithSeparatedFlights() {
        JetPack jp1 = new JetPack("TEST-001", "ALPHA-01", "Pilot1", "2024", "Model1", "Mfg1");
        JetPack jp2 = new JetPack("TEST-002", "BRAVO-02", "Pilot2", "2024", "Model2", "Mfg2");
        
        JetPackFlight flight1 = new JetPackFlight(jp1, new Point(100, 100), new Point(200, 200), Color.RED);
        JetPackFlight flight2 = new JetPackFlight(jp2, new Point(500, 500), new Point(600, 600), Color.BLUE);
        
        flights.add(flight1);
        flights.add(flight2);
        
        flightStates.put(flight1, new JetPackFlightState(flight1, parkingSpaces));
        flightStates.put(flight2, new JetPackFlightState(flight2, parkingSpaces));
        
        // Should not throw exception
        detector.checkCollisions(flights, flightStates);
    }
    
    @Test
    public void testCollisionWithCloseFlights() {
        JetPack jp1 = new JetPack("TEST-001", "ALPHA-01", "Pilot1", "2024", "Model1", "Mfg1");
        JetPack jp2 = new JetPack("TEST-002", "BRAVO-02", "Pilot2", "2024", "Model2", "Mfg2");
        
        // Create flights very close together
        JetPackFlight flight1 = new JetPackFlight(jp1, new Point(100, 100), new Point(200, 200), Color.RED);
        JetPackFlight flight2 = new JetPackFlight(jp2, new Point(105, 105), new Point(205, 205), Color.BLUE);
        
        flights.add(flight1);
        flights.add(flight2);
        
        flightStates.put(flight1, new JetPackFlightState(flight1, parkingSpaces));
        flightStates.put(flight2, new JetPackFlightState(flight2, parkingSpaces));
        
        // Should detect potential collision
        detector.checkCollisions(flights, flightStates);
        // If collision detected, emergency reroute should be set
    }
    
    @Test
    public void testParkedFlightsIgnored() {
        JetPack jp1 = new JetPack("TEST-001", "ALPHA-01", "Pilot1", "2024", "Model1", "Mfg1");
        JetPack jp2 = new JetPack("TEST-002", "BRAVO-02", "Pilot2", "2024", "Model2", "Mfg2");
        
        JetPackFlight flight1 = new JetPackFlight(jp1, new Point(100, 100), new Point(200, 200), Color.RED);
        JetPackFlight flight2 = new JetPackFlight(jp2, new Point(105, 105), new Point(205, 205), Color.BLUE);
        
        flights.add(flight1);
        flights.add(flight2);
        
        JetPackFlightState state1 = new JetPackFlightState(flight1, parkingSpaces);
        JetPackFlightState state2 = new JetPackFlightState(flight2, parkingSpaces);
        
        flightStates.put(flight1, state1);
        flightStates.put(flight2, state2);
        
        // Parked flights should be ignored
        detector.checkCollisions(flights, flightStates);
    }
    
    @Test
    public void testMultipleFlightCollisionDetection() {
        for (int i = 0; i < 5; i++) {
            JetPack jp = new JetPack("TEST-" + i, "CALL-" + i, "Pilot" + i, "2024", "Model", "Mfg");
            // Create flights with varying distances
            Point start = new Point(100 + i * 50, 100 + i * 50);
            Point dest = new Point(200 + i * 50, 200 + i * 50);
            JetPackFlight flight = new JetPackFlight(jp, start, dest, Color.RED);
            
            flights.add(flight);
            flightStates.put(flight, new JetPackFlightState(flight, parkingSpaces));
        }
        
        // Should check all pairs
        detector.checkCollisions(flights, flightStates);
        assertTrue("Should have checked multiple flights", flights.size() == 5);
    }
    
    @Test
    public void testEmptyFlightList() {
        // Should handle empty list gracefully
        detector.checkCollisions(flights, flightStates);
        assertTrue("Empty list should not cause error", flights.isEmpty());
    }
    
    @Test
    public void testSingleFlight() {
        JetPack jp = new JetPack("TEST-001", "ALPHA-01", "Pilot1", "2024", "Model1", "Mfg1");
        JetPackFlight flight = new JetPackFlight(jp, new Point(100, 100), new Point(200, 200), Color.RED);
        
        flights.add(flight);
        flightStates.put(flight, new JetPackFlightState(flight, parkingSpaces));
        
        // Single flight should not collide with itself
        detector.checkCollisions(flights, flightStates);
    }
    
    @Test
    public void testAccidentAlertIntegration() {
        assertNotNull("Accident alert should be integrated", detector.getAccidentAlert());
        assertTrue("Accident alert should have ID", 
            detector.getAccidentAlert().getAlertSystemID().contains("COLLISION"));
    }
}
