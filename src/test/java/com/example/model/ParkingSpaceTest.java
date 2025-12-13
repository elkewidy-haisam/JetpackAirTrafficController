/**
 * Unit tests for ParkingSpace functionality and behavior validation.
 * 
 * Purpose:
 * Validates correctness of ParkingSpace through automated JUnit test cases. Ensures reliability
 * and prevents regressions by testing core functionality, edge cases, and integration scenarios.
 * 
 * Key Test Areas:
 * - Core ParkingSpace operations and expected outcomes
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

package com.example.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test suite for ParkingSpace
 */
public class ParkingSpaceTest {
    /** Parking space instance used across test methods */
    private ParkingSpace parkingSpace;
    
    /**
     * Sets up test fixture before each test method.
     * Creates a parking space at coordinates (100, 200) with ID "BOS-P001".
     */
    @Before
    public void setUp() {
        parkingSpace = new ParkingSpace("BOS-P001", 100.0, 200.0);  // Create test parking space
    }
    
    /**
     * Tests parking space object creation and initial state.
     * Verifies ID, coordinates, and occupied status.
     */
    @Test
    public void testParkingSpaceCreation() {
        assertNotNull("ParkingSpace should be created", parkingSpace);  // Verify object exists
        assertEquals("ID should match", "BOS-P001", parkingSpace.getId());  // Check ID
        assertEquals("X coordinate should match", 100.0, parkingSpace.getX(), 0.01);  // Check X
        assertEquals("Y coordinate should match", 200.0, parkingSpace.getY(), 0.01);  // Check Y
        assertFalse("Should not be occupied initially", parkingSpace.isOccupied());  // Check unoccupied
    }
    
    /**
     * Tests occupy() method functionality.
     * Verifies that parking space becomes occupied after calling occupy().
     */
    @Test
    public void testOccupyParking() {
        assertFalse("Initially unoccupied", parkingSpace.isOccupied());  // Verify starts unoccupied
        
        parkingSpace.occupy();  // Mark as occupied
        assertTrue("Should be occupied after occupy()", parkingSpace.isOccupied());  // Verify occupied
    }
    
    /**
     * Tests vacate() method functionality.
     * Verifies that occupied parking space becomes vacant after calling vacate().
     */
    @Test
    public void testVacateParking() {
        parkingSpace.occupy();  // First occupy the space
        assertTrue("Should be occupied", parkingSpace.isOccupied());  // Verify occupied
        
        parkingSpace.vacate();  // Mark as vacant
        assertFalse("Should be vacant after vacate()", parkingSpace.isOccupied());  // Verify vacant
    }
    
    /**
     * Tests multiple occupy/vacate cycles.
     * Verifies that parking space can be occupied and vacated repeatedly.
     */
    @Test
    public void testMultipleOccupyVacateCycles() {
        // Repeat 10 occupy/vacate cycles
        for (int i = 0; i < 10; i++) {  // Loop 10 times
            parkingSpace.occupy();  // Occupy
            assertTrue("Should be occupied in cycle " + i, parkingSpace.isOccupied());  // Check occupied
            
            parkingSpace.vacate();  // Vacate
            assertFalse("Should be vacant in cycle " + i, parkingSpace.isOccupied());  // Check vacant
        }
    }
    
    /**
     * Tests parking space creation with integer coordinates.
     * Verifies that integer coordinates are converted to doubles correctly.
     */
    @Test
    public void testParkingSpaceWithIntCoordinates() {
        ParkingSpace ps = new ParkingSpace("TEST-P001", 50, 75);  // Create with int coords
        assertEquals("X should be 50", 50.0, ps.getX(), 0.01);  // Check X coordinate
        assertEquals("Y should be 75", 75.0, ps.getY(), 0.01);  // Check Y coordinate
    }
    
    /**
     * Tests parking space ID format.
     * Verifies ID contains city prefix and parking identifier.
     */
    @Test
    public void testParkingSpaceIDFormat() {
        assertTrue("ID should contain city prefix", parkingSpace.getId().startsWith("BOS"));  // Check city prefix
        assertTrue("ID should contain P", parkingSpace.getId().contains("P"));  // Check parking identifier
    }
    
    /**
     * Tests creation of multiple parking spaces.
     * Verifies that each parking space has a unique ID.
     */
    @Test
    public void testMultipleParkingSpaces() {
        ParkingSpace ps1 = new ParkingSpace("BOS-P001", 100, 200);  // Create first space
        ParkingSpace ps2 = new ParkingSpace("BOS-P002", 150, 250);  // Create second space
        ParkingSpace ps3 = new ParkingSpace("NYC-P001", 200, 300);  // Create third space (different city)
        
        assertNotEquals("IDs should be unique", ps1.getId(), ps2.getId());  // Compare ID 1 and 2
        assertNotEquals("IDs should be unique", ps1.getId(), ps3.getId());  // Compare ID 1 and 3
    }
    
    /**
     * Tests coordinate precision.
     * Verifies that double precision coordinates are stored accurately.
     */
    @Test
    public void testCoordinatePrecision() {
        ParkingSpace ps = new ParkingSpace("TEST-P001", 123.456, 789.123);  // Create with precise coords
        assertEquals("X precision", 123.456, ps.getX(), 0.001);  // Check X with 3 decimal places
        assertEquals("Y precision", 789.123, ps.getY(), 0.001);  // Check Y with 3 decimal places
    }
    
    /**
     * Tests zero coordinates.
     * Verifies that parking space can be created at origin (0, 0).
     */
    @Test
    public void testZeroCoordinates() {
        ParkingSpace ps = new ParkingSpace("TEST-P001", 0, 0);  // Create at origin
        assertEquals("Zero X", 0.0, ps.getX(), 0.01);  // Check X is zero
        assertEquals("Zero Y", 0.0, ps.getY(), 0.01);  // Check Y is zero
    }
    
    /**
     * Tests negative coordinates.
     * Some systems might allow negative coordinates for offset maps.
     */
    @Test
    public void testNegativeCoordinates() {
        // Some systems might allow negative coordinates for offset maps
        ParkingSpace ps = new ParkingSpace("TEST-P001", -50.0, -100.0);  // Create with negative coords
        assertEquals("Negative X", -50.0, ps.getX(), 0.01);  // Check negative X
        assertEquals("Negative Y", -100.0, ps.getY(), 0.01);  // Check negative Y
    }
    
    /**
     * Tests large coordinate values.
     * Verifies that large coordinates are handled correctly.
     */
    @Test
    public void testLargeCoordinates() {
        ParkingSpace ps = new ParkingSpace("TEST-P001", 9999.99, 8888.88);  // Create with large coords
        assertEquals("Large X", 9999.99, ps.getX(), 0.01);  // Check large X
        assertEquals("Large Y", 8888.88, ps.getY(), 0.01);  // Check large Y
    }
    
    /**
     * Tests toString() method.
     * Verifies that string representation is not null and non-empty.
     */
    @Test
    public void testToString() {
        String str = parkingSpace.toString();  // Get string representation
        assertNotNull("toString should not be null", str);  // Verify not null
        // ParkingSpace uses default Object.toString(), so just verify it's not null
        assertTrue("toString should return non-empty string", str.length() > 0);  // Verify non-empty
    }
    
    /**
     * Tests occupy() idempotence.
     * Verifies that calling occupy() multiple times keeps space occupied.
     */
    @Test
    public void testOccupyIdempotent() {
        parkingSpace.occupy();  // Occupy once
        parkingSpace.occupy();  // Occupy again (idempotent operation)
        assertTrue("Should remain occupied", parkingSpace.isOccupied());  // Verify still occupied
    }
    
    /**
     * Tests vacate() idempotence.
     * Verifies that calling vacate() on vacant space keeps it vacant.
     */
    @Test
    public void testVacateIdempotent() {
        parkingSpace.vacate();  // Vacate already vacant space (idempotent operation)
        assertFalse("Should remain vacant", parkingSpace.isOccupied());  // Verify still vacant
    }
}
