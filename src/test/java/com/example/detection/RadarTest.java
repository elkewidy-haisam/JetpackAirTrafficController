package com.example.detection;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.example.detection.Radar.RadarContact;
import com.example.jetpack.JetPack;

/**
 * Comprehensive test suite for Radar system
 */
public class RadarTest {
    private Radar radar;
    private JetPack jetpack1;
    private JetPack jetpack2;
    private JetPack jetpack3;
    
    @Before
    public void setUp() {
        radar = new Radar("RADAR-TEST-01", 100.0, 500, 500);
        jetpack1 = new JetPack("JP1", "TEST-001", "ALPHA-01", "Pilot1", "2024", "Model1", "Mfg1", new java.awt.Point(0,0), 0.0, 0.0);
        jetpack2 = new JetPack("JP2", "TEST-002", "BRAVO-02", "Pilot2", "2024", "Model2", "Mfg2", new java.awt.Point(0,0), 0.0, 0.0);
        jetpack3 = new JetPack("JP3", "TEST-003", "CHARLIE-03", "Pilot3", "2024", "Model3", "Mfg3", new java.awt.Point(0,0), 0.0, 0.0);
    }
    
    @Test
    public void testRadarInitialization() {
        assertNotNull("Radar should be initialized", radar);
        assertEquals("Radar ID should match", "RADAR-TEST-01", radar.getRadarID());
        assertEquals("Radar range should match", 100.0, radar.getRadarRange(), 0.01);
        assertEquals("Center X should match", 500, radar.getCenterX());
        assertEquals("Center Y should match", 500, radar.getCenterY());
        assertTrue("Radar should be active", radar.isActive());
    }
    
    @Test
    public void testDefaultRadarConstructor() {
        Radar defaultRadar = new Radar();
        assertNotNull("Default radar should be created", defaultRadar);
        assertTrue("Default radar should be active", defaultRadar.isActive());
        assertEquals("Default range should be 50.0", 50.0, defaultRadar.getRadarRange(), 0.01);
    }
    
    @Test
    public void testAddJetpackToRadar() {
        radar.addJetpackToRadar(jetpack1, 100, 200, 1500);
        assertEquals("Radar should track 1 jetpack", 1, radar.getTrackedJetpackCount());
        
        radar.addJetpackToRadar(jetpack2, 300, 400, 2000);
        assertEquals("Radar should track 2 jetpacks", 2, radar.getTrackedJetpackCount());
    }
    
    @Test
    public void testRemoveJetpackFromRadar() {
        radar.addJetpackToRadar(jetpack1, 100, 200, 1500);
        radar.addJetpackToRadar(jetpack2, 300, 400, 2000);
        assertEquals("Should track 2 jetpacks", 2, radar.getTrackedJetpackCount());
        
        radar.removeJetpackFromRadar(jetpack1);
        assertEquals("Should track 1 jetpack after removal", 1, radar.getTrackedJetpackCount());
    }
    
    @Test
    public void testUpdateJetpackPosition() {
        radar.addJetpackToRadar(jetpack1, 100, 200, 1500);
        radar.updateJetPackPosition(jetpack1, 150, 250, 1600);
        
        Map<JetPack, RadarContact> positions = radar.getJetPackPositions();
        RadarContact contact = positions.get(jetpack1);
        assertNotNull("Contact should exist", contact);
        assertEquals("X should be updated", 150, contact.getX());
        assertEquals("Y should be updated", 250, contact.getY());
        assertEquals("Altitude should be updated", 1600, contact.getAltitude());
    }
    
    @Test
    public void testIdentifyAircraft() {
        radar.addJetpackToRadar(jetpack1, 100, 200, 1500);
        String id = radar.identifyAircraft(jetpack1);
        
        assertNotNull("Identification should not be null", id);
        assertTrue("Should contain callsign", id.contains("ALPHA-01"));
        assertTrue("Should contain serial", id.contains("TEST-001"));
    }
    
    @Test
    public void testGetJetpacksInRadius() {
        radar.addJetpackToRadar(jetpack1, 500, 500, 1500); // At center
        radar.addJetpackToRadar(jetpack2, 550, 550, 1500); // Close
        radar.addJetpackToRadar(jetpack3, 1000, 1000, 1500); // Far
        
        List<JetPack> nearby = radar.getJetpacksInRadius(500, 500, 100.0);
        assertTrue("Should include jetpack at center", nearby.contains(jetpack1));
        assertTrue("Should include nearby jetpack", nearby.contains(jetpack2));
        assertFalse("Should not include far jetpack", nearby.contains(jetpack3));
    }
    
    @Test
    public void testCheckForCollisions() {
        radar.addJetpackToRadar(jetpack1, 500, 500, 1500);
        radar.addJetpackToRadar(jetpack2, 510, 510, 1500); // Very close
        
        List<String> warnings = radar.checkForCollisions(50.0);
        assertFalse("Should detect potential collision", warnings.isEmpty());
    }
    
    @Test
    public void testRadarRangeModification() {
        radar.setRadarRange(200.0);
        assertEquals("Range should be updated", 200.0, radar.getRadarRange(), 0.01);
    }
    
    @Test
    public void testRadarActivation() {
        radar.setActive(false);
        assertFalse("Radar should be inactive", radar.isActive());
        
        radar.setActive(true);
        assertTrue("Radar should be active", radar.isActive());
    }
    
    @Test
    public void testMultipleJetpackTracking() {
        for (int i = 0; i < 10; i++) {
            JetPack jp = new JetPack("TEST-" + i, "TEST-" + i, "Pilot" + i, "2024", "Model", "Mfg");
            radar.addJetpackToRadar(jp, i * 100, i * 100, 1500 + i * 100);
        }
        
        assertEquals("Should track 10 jetpacks", 10, radar.getTrackedJetpackCount());
    }
    
    @Test
    public void testGetJetPackPositions() {
        radar.addJetpackToRadar(jetpack1, 100, 200, 1500);
        radar.addJetpackToRadar(jetpack2, 300, 400, 2000);
        
        Map<JetPack, RadarContact> positions = radar.getJetPackPositions();
        assertNotNull("Positions map should not be null", positions);
        assertEquals("Should have 2 positions", 2, positions.size());
        assertTrue("Should contain jetpack1", positions.containsKey(jetpack1));
        assertTrue("Should contain jetpack2", positions.containsKey(jetpack2));
    }
    
    @Test
    public void testRadarContactTimestamp() {
        radar.addJetpackToRadar(jetpack1, 100, 200, 1500);
        Map<JetPack, RadarContact> positions = radar.getJetPackPositions();
        RadarContact contact = positions.get(jetpack1);
        
        assertNotNull("Timestamp should exist", contact.getLastUpdated());
        assertTrue("Timestamp should be recent", contact.getLastUpdated() > 0);
    }
    
    @Test
    public void testScanInterval() {
        assertEquals("Default scan interval should be 1000ms", 1000, radar.getScanInterval());
        radar.setScanInterval(500);
        assertEquals("Scan interval should be updated", 500, radar.getScanInterval());
    }
    
    @Test
    public void testToString() {
        String str = radar.toString();
        assertNotNull("toString should not be null", str);
        assertTrue("Should contain radar ID", str.contains("RADAR-TEST-01"));
        assertTrue("Should contain range info", str.contains("100.0"));
    }
}
