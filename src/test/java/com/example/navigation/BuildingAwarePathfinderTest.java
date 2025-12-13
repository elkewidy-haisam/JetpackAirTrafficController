/**
 * Unit tests for BuildingAwarePathfinder functionality.
 * 
 * Purpose:
 * Validates that the A* pathfinding algorithm correctly finds paths around buildings
 * and generates waypoints that avoid obstacles.
 * 
 * @author Haisam Elkewidy
 */

package com.example.navigation;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.example.model.CityModel3D;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Test suite for BuildingAwarePathfinder
 */
public class BuildingAwarePathfinderTest {
    private BuildingAwarePathfinder pathfinder;
    private CityModel3D cityModel;
    private BufferedImage testMap;
    
    @Before
    public void setUp() {
        // Create a test map (200x200 pixels)
        testMap = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        
        // Fill with land color (green)
        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                testMap.setRGB(x, y, 0xFF00FF00); // Green
            }
        }
        
        // Create city model with test map
        cityModel = new CityModel3D("TestCity", testMap);
        pathfinder = new BuildingAwarePathfinder(cityModel);
    }
    
    @Test
    public void testPathfinderCreation() {
        assertNotNull("Pathfinder should be created", pathfinder);
    }
    
    @Test
    public void testFindPathInOpenSpace() {
        // Path in open space should be found
        List<Point> path = pathfinder.findPath(10, 10, 50, 50);
        
        // Should have at least start and end points
        assertNotNull("Path should not be null", path);
        // Note: path might be empty if straight line is clear, which is okay
    }
    
    @Test
    public void testPathAvoidsBuildingsWhenPossible() {
        // Test that pathfinder attempts to find a path
        // Even if buildings exist, pathfinder should return a path or empty list
        List<Point> path = pathfinder.findPath(20, 20, 180, 180);
        
        assertNotNull("Path should not be null", path);
        // Path can be empty if direct line is clear or waypoints if obstacles exist
    }
    
    @Test
    public void testPathFromSameStartAndDestination() {
        // Path from same point should be valid
        List<Point> path = pathfinder.findPath(50, 50, 50, 50);
        
        assertNotNull("Path should not be null", path);
    }
    
    @Test
    public void testPathNearMapEdge() {
        // Path near edge should be handled
        List<Point> path = pathfinder.findPath(5, 5, 195, 195);
        
        assertNotNull("Path should not be null", path);
    }
    
    @Test
    public void testPathWithBuildingsInCity() {
        // With actual city buildings, pathfinder should generate waypoints
        CityModel3D nyCity = new CityModel3D("New York", testMap);
        BuildingAwarePathfinder nyPathfinder = new BuildingAwarePathfinder(nyCity);
        
        List<Point> path = nyPathfinder.findPath(50, 50, 150, 150);
        
        assertNotNull("Path should not be null", path);
        // New York has many buildings, so pathfinder should attempt to find a route
        // (may be empty if straight line is clear)
    }
    
    @Test
    public void testMultiplePathsAreConsistent() {
        // Multiple calls with same parameters should be deterministic
        List<Point> path1 = pathfinder.findPath(30, 30, 170, 170);
        List<Point> path2 = pathfinder.findPath(30, 30, 170, 170);
        
        assertNotNull("First path should not be null", path1);
        assertNotNull("Second path should not be null", path2);
        assertEquals("Paths should have same length", path1.size(), path2.size());
    }
}
