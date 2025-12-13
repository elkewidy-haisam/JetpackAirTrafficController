/**
 * Unit tests for BuildingCollisionDetector functionality and behavior validation.
 * 
 * Purpose:
 * Validates correctness of BuildingCollisionDetector through automated JUnit test cases.
 * Ensures that jetpacks properly avoid buildings and houses in both 2D and 3D views.
 * 
 * Key Test Areas:
 * - Building collision detection with jetpack positions
 * - Path clearance checking between two points
 * - Safe altitude calculation near buildings
 * - Collision avoidance for different building types
 * 
 * @author Haisam Elkewidy
 */

package com.example.detection;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.example.model.Building3D;
import com.example.model.CityModel3D;
import com.example.model.House3D;

import java.awt.image.BufferedImage;

/**
 * Test suite for BuildingCollisionDetector
 */
public class BuildingCollisionDetectorTest {
    private BuildingCollisionDetector detector;
    private CityModel3D cityModel;
    private BufferedImage testMap;
    
    @Before
    public void setUp() {
        // Create a simple test map (100x100 pixels)
        testMap = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        
        // Fill with land color (green)
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                testMap.setRGB(x, y, 0xFF00FF00); // Green
            }
        }
        
        // Create city model with test map
        cityModel = new CityModel3D("TestCity", testMap);
        detector = new BuildingCollisionDetector(cityModel);
    }
    
    @Test
    public void testNoCollisionInOpenSpace() {
        // Position near edge of map (likely no buildings) at high altitude should not collide
        assertFalse("Open space should not have collision", 
            detector.checkCollision(5, 5, 200));
    }
    
    @Test
    public void testCollisionDetectionWithBuildings() {
        // Get a building from the city model
        if (!cityModel.getBuildings().isEmpty()) {
            Building3D building = cityModel.getBuildings().get(0);
            
            // Position inside building at ground level should collide
            double centerX = building.getX() + building.getWidth() / 2;
            double centerY = building.getY() + building.getLength() / 2;
            double centerAlt = building.getHeight() / 2;
            
            assertTrue("Jetpack inside building should collide", 
                detector.checkCollision(centerX, centerY, centerAlt));
        }
    }
    
    @Test
    public void testNoCollisionAboveBuilding() {
        // Get a building from the city model
        if (!cityModel.getBuildings().isEmpty()) {
            Building3D building = cityModel.getBuildings().get(0);
            
            // Position well above building should not collide
            // Use a much higher altitude to ensure no collision
            double centerX = building.getX() + building.getWidth() / 2;
            double centerY = building.getY() + building.getLength() / 2;
            double highAlt = 500; // Very high altitude, definitely above any building
            
            assertFalse("Jetpack at very high altitude should not collide", 
                detector.checkCollision(centerX, centerY, highAlt));
        }
    }
    
    @Test
    public void testPathClearanceInOpenSpace() {
        // Path at very high altitude should be clear (above all buildings)
        assertTrue("Path in open space should be clear",
            detector.isPathClear(10, 10, 300, 20, 20, 300));
    }
    
    @Test
    public void testPathBlockedByBuilding() {
        // Get a building from the city model
        if (!cityModel.getBuildings().isEmpty()) {
            Building3D building = cityModel.getBuildings().get(0);
            
            // Path through building should be blocked
            double x1 = building.getX() - 10;
            double y1 = building.getY() + building.getLength() / 2;
            double x2 = building.getX() + building.getWidth() + 10;
            double y2 = building.getY() + building.getLength() / 2;
            double altitude = building.getHeight() / 2;
            
            assertFalse("Path through building should be blocked",
                detector.isPathClear(x1, y1, altitude, x2, y2, altitude));
        }
    }
    
    @Test
    public void testMinimumSafeAltitude() {
        // Get a building from the city model
        if (!cityModel.getBuildings().isEmpty()) {
            Building3D building = cityModel.getBuildings().get(0);
            
            // Position at building center
            double centerX = building.getX() + building.getWidth() / 2;
            double centerY = building.getY() + building.getLength() / 2;
            
            double minAlt = detector.getMinimumSafeAltitude(centerX, centerY);
            
            // Minimum safe altitude should be greater than building height
            assertTrue("Safe altitude should be above building", 
                minAlt >= building.getHeight());
        }
    }
    
    @Test
    public void testSafePositionAboveBuilding() {
        // Get a building from the city model
        if (!cityModel.getBuildings().isEmpty()) {
            Building3D building = cityModel.getBuildings().get(0);
            
            // Position well above building should be safe
            double centerX = building.getX() + building.getWidth() / 2;
            double centerY = building.getY() + building.getLength() / 2;
            
            // Get minimum safe altitude and go well above it
            double minSafeAlt = detector.getMinimumSafeAltitude(centerX, centerY);
            double safeAlt = Math.max(minSafeAlt + 10, building.getHeight() + 50);
            
            assertTrue("Position above building should be safe",
                detector.isSafePosition(centerX, centerY, safeAlt));
        }
    }
    
    @Test
    public void testUnsafePositionInBuilding() {
        // Get a building from the city model
        if (!cityModel.getBuildings().isEmpty()) {
            Building3D building = cityModel.getBuildings().get(0);
            
            // Position inside building should not be safe
            double centerX = building.getX() + building.getWidth() / 2;
            double centerY = building.getY() + building.getLength() / 2;
            double unsafeAlt = building.getHeight() / 2;
            
            assertFalse("Position inside building should not be safe",
                detector.isSafePosition(centerX, centerY, unsafeAlt));
        }
    }
    
    @Test
    public void testGetNearbyBuildings() {
        // Get a building from the city model
        if (!cityModel.getBuildings().isEmpty()) {
            Building3D building = cityModel.getBuildings().get(0);
            
            // Position at building center
            double centerX = building.getX() + building.getWidth() / 2;
            double centerY = building.getY() + building.getLength() / 2;
            
            var nearbyBuildings = detector.getNearbyBuildings(centerX, centerY, 100);
            
            assertNotNull("Nearby buildings list should not be null", nearbyBuildings);
            assertTrue("Should find at least one nearby building", 
                nearbyBuildings.size() > 0);
        }
    }
    
    @Test
    public void testJetpackRadiusConfiguration() {
        double expectedRadius = 5.0; // Default radius
        assertEquals("Jetpack radius should match default", 
            expectedRadius, detector.getJetpackRadius(), 0.01);
    }
    
    @Test
    public void testCustomJetpackRadius() {
        double customRadius = 10.0;
        BuildingCollisionDetector customDetector = 
            new BuildingCollisionDetector(cityModel, customRadius);
        
        assertEquals("Jetpack radius should match custom value", 
            customRadius, customDetector.getJetpackRadius(), 0.01);
    }
}
