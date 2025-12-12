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
    private ParkingSpace parkingSpace;
    
    @Before
    public void setUp() {
        parkingSpace = new ParkingSpace("BOS-P001", 100.0, 200.0);
    }
    
    @Test
    public void testParkingSpaceCreation() {
        assertNotNull("ParkingSpace should be created", parkingSpace);
        assertEquals("ID should match", "BOS-P001", parkingSpace.getId());
        assertEquals("X coordinate should match", 100.0, parkingSpace.getX(), 0.01);
        assertEquals("Y coordinate should match", 200.0, parkingSpace.getY(), 0.01);
        assertFalse("Should not be occupied initially", parkingSpace.isOccupied());
    }
    
    @Test
    public void testOccupyParking() {
        assertFalse("Initially unoccupied", parkingSpace.isOccupied());
        
        parkingSpace.occupy();
        assertTrue("Should be occupied after occupy()", parkingSpace.isOccupied());
    }
    
    @Test
    public void testVacateParking() {
        parkingSpace.occupy();
        assertTrue("Should be occupied", parkingSpace.isOccupied());
        
        parkingSpace.vacate();
        assertFalse("Should be vacant after vacate()", parkingSpace.isOccupied());
    }
    
    @Test
    public void testMultipleOccupyVacateCycles() {
        for (int i = 0; i < 10; i++) {
            parkingSpace.occupy();
            assertTrue("Should be occupied in cycle " + i, parkingSpace.isOccupied());
            
            parkingSpace.vacate();
            assertFalse("Should be vacant in cycle " + i, parkingSpace.isOccupied());
        }
    }
    
    @Test
    public void testParkingSpaceWithIntCoordinates() {
        ParkingSpace ps = new ParkingSpace("TEST-P001", 50, 75);
        assertEquals("X should be 50", 50.0, ps.getX(), 0.01);
        assertEquals("Y should be 75", 75.0, ps.getY(), 0.01);
    }
    
    @Test
    public void testParkingSpaceIDFormat() {
        assertTrue("ID should contain city prefix", parkingSpace.getId().startsWith("BOS"));
        assertTrue("ID should contain P", parkingSpace.getId().contains("P"));
    }
    
    @Test
    public void testMultipleParkingSpaces() {
        ParkingSpace ps1 = new ParkingSpace("BOS-P001", 100, 200);
        ParkingSpace ps2 = new ParkingSpace("BOS-P002", 150, 250);
        ParkingSpace ps3 = new ParkingSpace("NYC-P001", 200, 300);
        
        assertNotEquals("IDs should be unique", ps1.getId(), ps2.getId());
        assertNotEquals("IDs should be unique", ps1.getId(), ps3.getId());
    }
    
    @Test
    public void testCoordinatePrecision() {
        ParkingSpace ps = new ParkingSpace("TEST-P001", 123.456, 789.123);
        assertEquals("X precision", 123.456, ps.getX(), 0.001);
        assertEquals("Y precision", 789.123, ps.getY(), 0.001);
    }
    
    @Test
    public void testZeroCoordinates() {
        ParkingSpace ps = new ParkingSpace("TEST-P001", 0, 0);
        assertEquals("Zero X", 0.0, ps.getX(), 0.01);
        assertEquals("Zero Y", 0.0, ps.getY(), 0.01);
    }
    
    @Test
    public void testNegativeCoordinates() {
        // Some systems might allow negative coordinates for offset maps
        ParkingSpace ps = new ParkingSpace("TEST-P001", -50.0, -100.0);
        assertEquals("Negative X", -50.0, ps.getX(), 0.01);
        assertEquals("Negative Y", -100.0, ps.getY(), 0.01);
    }
    
    @Test
    public void testLargeCoordinates() {
        ParkingSpace ps = new ParkingSpace("TEST-P001", 9999.99, 8888.88);
        assertEquals("Large X", 9999.99, ps.getX(), 0.01);
        assertEquals("Large Y", 8888.88, ps.getY(), 0.01);
    }
    
    @Test
    public void testToString() {
        String str = parkingSpace.toString();
        assertNotNull("toString should not be null", str);
        // ParkingSpace uses default Object.toString(), so just verify it's not null
        assertTrue("toString should return non-empty string", str.length() > 0);
    }
    
    @Test
    public void testOccupyIdempotent() {
        parkingSpace.occupy();
        parkingSpace.occupy(); // Occupy again
        assertTrue("Should remain occupied", parkingSpace.isOccupied());
    }
    
    @Test
    public void testVacateIdempotent() {
        parkingSpace.vacate();
        assertFalse("Should remain vacant", parkingSpace.isOccupied());
    }
}
