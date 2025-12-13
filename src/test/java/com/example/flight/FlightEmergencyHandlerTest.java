/**
 * Unit tests for FlightEmergencyHandler functionality and behavior validation.
 * 
 * Purpose:
 * Validates correctness of FlightEmergencyHandler through automated JUnit test cases. Ensures reliability
 * and prevents regressions by testing core functionality, edge cases, and integration scenarios.
 * 
 * Key Test Areas:
 * - Core FlightEmergencyHandler operations and expected outcomes
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

package com.example.flight;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.example.parking.ParkingSpace;

/**
 * Comprehensive test suite for FlightEmergencyHandler
 */
public class FlightEmergencyHandlerTest {
    private FlightEmergencyHandler handler;
    private List<ParkingSpace> parkingSpaces;
    
    @Before
    public void setUp() {
        handler = new FlightEmergencyHandler("TEST-ALPHA-01");
        parkingSpaces = new ArrayList<>();
        
        // Create test parking spaces
        for (int i = 0; i < 10; i++) {
            parkingSpaces.add(new ParkingSpace("TEST-P" + i, 100 * i, 100 * i));
        }
    }
    
    @Test
    public void testHandlerCreation() {
        assertNotNull("Handler should be created", handler);
        assertNull("Initial radio destination should be null", handler.getRadioDestination());
        assertNull("Initial radio altitude should be null", handler.getRadioAltitude());
        assertFalse("Should not be following instructions initially", 
            handler.isFollowingRadioInstruction());
    }
    
    @Test
    public void testReceiveCoordinateInstruction() {
        Point newDest = handler.receiveCoordinateInstruction(500, 600, "Test instruction");
        
        assertNotNull("Should return destination", newDest);
        assertEquals("X should match", 500, newDest.x);
        assertEquals("Y should match", 600, newDest.y);
        assertTrue("Should be following instruction", handler.isFollowingRadioInstruction());
        assertEquals("Radio destination should be set", 500, handler.getRadioDestination().x);
    }
    
    @Test
    public void testReceiveAltitudeInstruction() {
        double currentAlt = 1000.0;
        double newAlt = 1500.0;
        
        double adjustedAlt = handler.receiveAltitudeInstruction(currentAlt, newAlt, "Climb to 1500");
        
        assertTrue("Should be following instruction", handler.isFollowingRadioInstruction());
        assertNotNull("Radio altitude should be set", handler.getRadioAltitude());
        // Altitude adjusts gradually
        assertTrue("Adjusted altitude should be between current and target", 
            adjustedAlt > currentAlt && adjustedAlt <= newAlt);
    }
    
    @Test
    public void testAltitudeGradualChange() {
        double currentAlt = 1000.0;
        double targetAlt = 2000.0;
        
        double adjusted = handler.receiveAltitudeInstruction(currentAlt, targetAlt, "Climb");
        
        // Should not jump directly to target
        assertTrue("Should not jump to target immediately", adjusted < targetAlt);
        assertTrue("Should move toward target", adjusted > currentAlt);
    }
    
    @Test
    public void testDescendingAltitude() {
        double currentAlt = 2000.0;
        double targetAlt = 1000.0;
        
        double adjusted = handler.receiveAltitudeInstruction(currentAlt, targetAlt, "Descend");
        
        assertTrue("Should descend", adjusted < currentAlt);
        assertTrue("Should not overshoot", adjusted >= targetAlt);
    }
    
    @Test
    public void testEmergencyLandingWithParkingSpaces() {
        Point emergencySpot = handler.receiveEmergencyLandingInstruction(
            500.0, 500.0, parkingSpaces, "Test emergency");
        
        assertNotNull("Should find emergency landing spot", emergencySpot);
    }
    
    @Test
    public void testEmergencyLandingWithoutParkingSpaces() {
        Point emergencySpot = handler.receiveEmergencyLandingInstruction(
            500.0, 500.0, null, "Test emergency");
        
        assertNull("Should return null without parking spaces", emergencySpot);
    }
    
    @Test
    public void testEmergencyLandingWithEmptyParkingSpaces() {
        List<ParkingSpace> empty = new ArrayList<>();
        Point emergencySpot = handler.receiveEmergencyLandingInstruction(
            500.0, 500.0, empty, "Test emergency");
        
        assertNull("Should return null with empty parking list", emergencySpot);
    }
    
    @Test
    public void testFindEmergencyLandingSpot() {
        Point spot = handler.findEmergencyLandingSpot(500.0, 500.0, parkingSpaces, "Test");
        assertNotNull("Should find a spot", spot);
    }
    
    @Test
    public void testFindNearestParkingSpace() {
        // Position close to first parking space (0, 0)
        Point spot = handler.findEmergencyLandingSpot(10.0, 10.0, parkingSpaces, "Near origin");
        
        assertNotNull("Should find nearest spot", spot);
        assertEquals("Should be near origin X", 0, spot.x);
        assertEquals("Should be near origin Y", 0, spot.y);
    }
    
    @Test
    public void testFindNearestWithOccupiedSpaces() {
        // Occupy some spaces
        parkingSpaces.get(0).occupy();
        parkingSpaces.get(1).occupy();
        
        Point spot = handler.findEmergencyLandingSpot(10.0, 10.0, parkingSpaces, "With occupied");
        
        assertNotNull("Should find available spot", spot);
        // Should skip occupied spaces
    }
    
    @Test
    public void testCheckRadioDestinationReached() {
        handler.receiveCoordinateInstruction(500, 500, "Test");
        Point radioDest = handler.getRadioDestination();
        
        // Pass the radio destination as currentDest with small distance
        boolean reached = handler.checkRadioDestinationReached(radioDest, 1.0, 2.0);
        assertTrue("Should detect destination reached", reached);
        assertNull("Radio destination should be cleared", handler.getRadioDestination());
    }
    
    @Test
    public void testCheckRadioDestinationNotReached() {
        handler.receiveCoordinateInstruction(500, 500, "Test");
        Point farDest = new Point(1000, 1000);
        
        boolean reached = handler.checkRadioDestinationReached(farDest, 100.0, 2.0);
        assertFalse("Should not detect far destination as reached", reached);
        assertNotNull("Radio destination should still be set", handler.getRadioDestination());
    }
    
    @Test
    public void testCheckRadioAltitudeReached() {
        handler.receiveAltitudeInstruction(1000.0, 1500.0, "Climb");
        
        // Simulate reaching target altitude
        boolean reached = handler.checkRadioAltitudeReached(1500.0);
        assertTrue("Should detect altitude reached", reached);
        assertNull("Radio altitude should be cleared", handler.getRadioAltitude());
    }
    
    @Test
    public void testCheckRadioAltitudeNotReached() {
        handler.receiveAltitudeInstruction(1000.0, 1500.0, "Climb");
        
        boolean reached = handler.checkRadioAltitudeReached(1100.0);
        assertFalse("Should not detect altitude reached yet", reached);
        assertNotNull("Radio altitude should still be set", handler.getRadioAltitude());
    }
    
    @Test
    public void testClearInstructions() {
        handler.receiveCoordinateInstruction(500, 500, "Test");
        handler.receiveAltitudeInstruction(1000.0, 1500.0, "Climb");
        
        assertTrue("Should be following instructions", handler.isFollowingRadioInstruction());
        
        handler.clearInstructions();
        
        assertNull("Radio destination should be cleared", handler.getRadioDestination());
        assertNull("Radio altitude should be cleared", handler.getRadioAltitude());
        assertFalse("Should not be following instructions", handler.isFollowingRadioInstruction());
    }
    
    @Test
    public void testGenerateEmergencyDetour() {
        Point destination = new Point(1000, 1000);
        List<Point> detour = handler.generateEmergencyDetour(500.0, 500.0, destination);
        
        assertNotNull("Detour should be generated", detour);
        assertEquals("Detour should have 2 waypoints", 2, detour.size());
        assertEquals("Final waypoint should be destination", destination, detour.get(1));
    }
    
    @Test
    public void testEmergencyLandingOverWater() {
        // Create a test image with water and land
        BufferedImage testImage = createTestImage();
        handler.setMapImage(testImage);
        
        // Position over water (top half of test image)
        Point spot = handler.findEmergencyLandingSpot(500.0, 250.0, parkingSpaces, "Over water");
        
        // Should redirect to land or find safe spot
        assertNotNull("Should find a safe landing spot", spot);
    }
    
    @Test
    public void testEmergencyLandingOnLand() {
        BufferedImage testImage = createTestImage();
        handler.setMapImage(testImage);
        
        // Position over land (bottom half of test image)
        Point spot = handler.findEmergencyLandingSpot(500.0, 750.0, parkingSpaces, "On land");
        
        assertNotNull("Should find landing spot on land", spot);
    }
    
    private BufferedImage createTestImage() {
        BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        
        // Top half is water (blue)
        for (int y = 0; y < 500; y++) {
            for (int x = 0; x < 1000; x++) {
                img.setRGB(x, y, 0x0000FF);
            }
        }
        
        // Bottom half is land (green)
        for (int y = 500; y < 1000; y++) {
            for (int x = 0; x < 1000; x++) {
                img.setRGB(x, y, 0x00FF00);
            }
        }
        
        return img;
    }
    
    @Test
    public void testSetMapImage() {
        BufferedImage img = createTestImage();
        handler.setMapImage(img);
        // Should not throw exception
    }
}
