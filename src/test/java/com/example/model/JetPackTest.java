/**
 * Unit tests for JetPack functionality and behavior validation.
 * 
 * Purpose:
 * Validates correctness of JetPack through automated JUnit test cases. Ensures reliability
 * and prevents regressions by testing core functionality, edge cases, and integration scenarios.
 * 
 * Key Test Areas:
 * - Core JetPack operations and expected outcomes
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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.example.jetpack.JetPack;

/**
 * Comprehensive test suite for JetPack model
 */
public class JetPackTest {
    /** JetPack instance used for testing */
    private JetPack jetpack;
    
    /**
     * Sets up test fixture before each test method.
     * Resets callsign counter and creates fresh test jetpack instance.
     */
    @Before
    public void setUp() {
           com.example.jetpack.JetPack.resetCallsignCounter();  // Reset static counter for consistent tests
           // Create test jetpack with all required parameters
           jetpack = new JetPack("BOS-001", "ALPHA-01", "ALPHA-01", "John Doe", "2024", 
                   "FreedomFlyer", "LibertyCorp", new java.awt.Point(0,0), 0.0, 0.0);
    }
    
    /**
     * Tests basic JetPack creation and property getters.
     * Verifies all constructor parameters are stored correctly.
     */
    @Test
    public void testJetPackCreation() {
        assertNotNull("JetPack should be created", jetpack);  // Verify object created
        assertEquals("Serial number should match", "BOS-001", jetpack.getSerialNumber());  // Check serial
        assertEquals("Callsign should match", "ALPHA-01", jetpack.getCallsign());  // Check callsign
        assertEquals("Owner should match", "John Doe", jetpack.getOwnerName());  // Check owner
        assertEquals("Year should match", "2024", jetpack.getYear());  // Check year
        assertEquals("Model should match", "FreedomFlyer", jetpack.getModel());  // Check model
        assertEquals("Manufacturer should match", "LibertyCorp", jetpack.getManufacturer());  // Check manufacturer
    }
    
    /**
     * Tests callsign formatting requirements.
     * Verifies callsign contains hyphen and has proper structure.
     */
    @Test
    public void testCallsignFormatting() {
        assertTrue("Callsign should contain hyphen", jetpack.getCallsign().contains("-"));  // Check hyphen
        String[] parts = jetpack.getCallsign().split("-");  // Split on hyphen
        assertEquals("Callsign should have two parts", 2, parts.length);  // Check 2 parts (prefix-number)
        assertEquals("Number part should be 2 digits", 2, parts[1].length());  // Check number is 2 digits
    }
    
    /**
     * Tests serial number format requirements.
     * Verifies serial number has city prefix and hyphen.
     */
    @Test
    public void testSerialNumberFormat() {
        assertTrue("Serial number should contain city prefix", jetpack.getSerialNumber().startsWith("BOS"));  // Check BOS prefix
        assertTrue("Serial number should contain hyphen", jetpack.getSerialNumber().contains("-"));  // Check hyphen
    }
    
    /**
     * Tests that jetpack flight operations execute without exceptions.
     * Validates basic operation methods can be called successfully.
     */
    @Test
    public void testJetPackOperations() {
        // These methods should execute without exceptions
        jetpack.takeOff();  // Test takeoff
        jetpack.ascend();   // Test climb
        jetpack.descend();  // Test descent
        jetpack.land();     // Test landing
        jetpack.park();     // Test parking
    }
    
    /**
     * Tests that multiple jetpacks have unique identifiers.
     * Verifies serial numbers and callsigns are unique across instances.
     */
    @Test
    public void testMultipleJetPacksUniqueIdentifiers() {
        // Create two different jetpacks
        JetPack jp1 = new JetPack("BOS-001", "ALPHA-01", "Owner1", "2024", "Model1", "Mfg1");
        JetPack jp2 = new JetPack("BOS-002", "BRAVO-02", "Owner2", "2024", "Model2", "Mfg2");
        
        assertNotEquals("Serial numbers should be unique", jp1.getSerialNumber(), jp2.getSerialNumber());  // Check unique serials
        assertNotEquals("Callsigns should be unique", jp1.getCallsign(), jp2.getCallsign());  // Check unique callsigns
    }
    
    /**
     * Tests toString() method returns valid string representation.
     * Verifies toString produces non-null, non-empty string.
     */
    @Test
    public void testToString() {
        String str = jetpack.toString();  // Call toString
        assertNotNull("toString should not return null", str);  // Verify not null
        // JetPack uses default Object.toString(), so just verify it's not null
        assertTrue("toString should return non-empty string", str.length() > 0);  // Verify not empty
    }
}
