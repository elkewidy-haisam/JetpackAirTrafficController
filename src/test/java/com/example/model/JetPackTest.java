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
    private JetPack jetpack;
    
    @Before
    public void setUp() {
           com.example.jetpack.JetPack.resetCallsignCounter();
           jetpack = new JetPack("BOS-001", "ALPHA-01", "ALPHA-01", "John Doe", "2024", "FreedomFlyer", "LibertyCorp", new java.awt.Point(0,0), 0.0, 0.0);
    }
    
    @Test
    public void testJetPackCreation() {
        assertNotNull("JetPack should be created", jetpack);
        assertEquals("Serial number should match", "BOS-001", jetpack.getSerialNumber());
        assertEquals("Callsign should match", "ALPHA-01", jetpack.getCallsign());
        assertEquals("Owner should match", "John Doe", jetpack.getOwnerName());
        assertEquals("Year should match", "2024", jetpack.getYear());
        assertEquals("Model should match", "FreedomFlyer", jetpack.getModel());
        assertEquals("Manufacturer should match", "LibertyCorp", jetpack.getManufacturer());
    }
    
    @Test
    public void testCallsignFormatting() {
        assertTrue("Callsign should contain hyphen", jetpack.getCallsign().contains("-"));
        String[] parts = jetpack.getCallsign().split("-");
        assertEquals("Callsign should have two parts", 2, parts.length);
        assertEquals("Number part should be 2 digits", 2, parts[1].length());
    }
    
    @Test
    public void testSerialNumberFormat() {
        assertTrue("Serial number should contain city prefix", jetpack.getSerialNumber().startsWith("BOS"));
        assertTrue("Serial number should contain hyphen", jetpack.getSerialNumber().contains("-"));
    }
    
    @Test
    public void testJetPackOperations() {
        // These methods should execute without exceptions
        jetpack.takeOff();
        jetpack.ascend();
        jetpack.descend();
        jetpack.land();
        jetpack.park();
    }
    
    @Test
    public void testMultipleJetPacksUniqueIdentifiers() {
        JetPack jp1 = new JetPack("BOS-001", "ALPHA-01", "Owner1", "2024", "Model1", "Mfg1");
        JetPack jp2 = new JetPack("BOS-002", "BRAVO-02", "Owner2", "2024", "Model2", "Mfg2");
        
        assertNotEquals("Serial numbers should be unique", jp1.getSerialNumber(), jp2.getSerialNumber());
        assertNotEquals("Callsigns should be unique", jp1.getCallsign(), jp2.getCallsign());
    }
    
    @Test
    public void testToString() {
        String str = jetpack.toString();
        assertNotNull("toString should not return null", str);
        // JetPack uses default Object.toString(), so just verify it's not null
        assertTrue("toString should return non-empty string", str.length() > 0);
    }
}
