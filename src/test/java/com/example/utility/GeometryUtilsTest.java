/**
 * Unit tests for GeometryUtils functionality and behavior validation.
 * 
 * Purpose:
 * Validates correctness of GeometryUtils through automated JUnit test cases. Ensures reliability
 * and prevents regressions by testing core functionality, edge cases, and integration scenarios.
 * 
 * Key Test Areas:
 * - Core GeometryUtils operations and expected outcomes
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

package com.example.utility;

import com.example.utility.geometry.GeometryUtils;

import java.awt.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Comprehensive test suite for GeometryUtils
 */
public class GeometryUtilsTest {
    
    @Test
    public void testCalculateDistanceSamePoint() {
        double distance = GeometryUtils.calculateDistance(100, 200, 100, 200);
        assertEquals("Distance to same point should be 0", 0.0, distance, 0.01);
    }
    
    @Test
    public void testCalculateDistanceHorizontal() {
        double distance = GeometryUtils.calculateDistance(0, 0, 100, 0);
        assertEquals("Horizontal distance", 100.0, distance, 0.01);
    }
    
    @Test
    public void testCalculateDistanceVertical() {
        double distance = GeometryUtils.calculateDistance(0, 0, 0, 100);
        assertEquals("Vertical distance", 100.0, distance, 0.01);
    }
    
    @Test
    public void testCalculateDistanceDiagonal() {
        // 3-4-5 triangle
        double distance = GeometryUtils.calculateDistance(0, 0, 3, 4);
        assertEquals("Diagonal distance (3-4-5 triangle)", 5.0, distance, 0.01);
    }
    
    @Test
    public void testCalculateDistanceLargeValues() {
        double distance = GeometryUtils.calculateDistance(1000, 1000, 2000, 2000);
        assertEquals("Large values distance", Math.sqrt(2000000), distance, 0.01);
    }
    
    @Test
    public void testCalculateDistanceNegativeCoordinates() {
        double distance = GeometryUtils.calculateDistance(-100, -100, 100, 100);
        assertEquals("Negative coordinates", Math.sqrt(80000), distance, 0.01);
    }
    
    @Test
    public void testCalculateDistanceWithDoubles() {
        double distance = GeometryUtils.calculateDistance(10.5, 20.5, 15.5, 25.5);
        double expected = Math.sqrt(25 + 25); // 5^2 + 5^2
        assertEquals("Double precision", expected, distance, 0.01);
    }
    
    @Test
    public void testCreatePoint() {
        Point p = GeometryUtils.createPoint(123.456, 789.123);
        assertNotNull("Point should be created", p);
        assertEquals("Point X", 123, p.x);
        assertEquals("Point Y", 789, p.y);
    }
    
    @Test
    public void testCreatePointRounding() {
        Point p1 = GeometryUtils.createPoint(10.4, 20.4);
        assertEquals("Should round down", 10, p1.x);
        assertEquals("Should round down", 20, p1.y);
        
        Point p2 = GeometryUtils.createPoint(10.6, 20.6);
        assertEquals("Should round down (truncate)", 10, p2.x);
        assertEquals("Should round down (truncate)", 20, p2.y);
    }
    
    @Test
    public void testCreatePointNegative() {
        Point p = GeometryUtils.createPoint(-50.5, -100.5);
        assertEquals("Negative X", -50, p.x);
        assertEquals("Negative Y", -100, p.y);
    }
    
    @Test
    public void testCreatePointZero() {
        Point p = GeometryUtils.createPoint(0.0, 0.0);
        assertEquals("Zero X", 0, p.x);
        assertEquals("Zero Y", 0, p.y);
    }
    
    @Test
    public void testPythagoreanTheorem() {
        // Test various Pythagorean triples
        assertEquals("3-4-5", 5.0, GeometryUtils.calculateDistance(0, 0, 3, 4), 0.01);
        assertEquals("5-12-13", 13.0, GeometryUtils.calculateDistance(0, 0, 5, 12), 0.01);
        assertEquals("8-15-17", 17.0, GeometryUtils.calculateDistance(0, 0, 8, 15), 0.01);
        assertEquals("7-24-25", 25.0, GeometryUtils.calculateDistance(0, 0, 7, 24), 0.01);
    }
    
    @Test
    public void testSymmetricDistance() {
        double d1 = GeometryUtils.calculateDistance(10, 20, 50, 60);
        double d2 = GeometryUtils.calculateDistance(50, 60, 10, 20);
        assertEquals("Distance should be symmetric", d1, d2, 0.01);
    }
    
    @Test
    public void testDistanceCommutative() {
        // Test that order doesn't matter
        double d1 = GeometryUtils.calculateDistance(100, 200, 300, 400);
        double d2 = GeometryUtils.calculateDistance(300, 400, 100, 200);
        assertEquals("Distance is commutative", d1, d2, 0.001);
    }
    
    @Test
    public void testDistanceTriangleInequality() {
        // For any three points, d(A,C) <= d(A,B) + d(B,C)
        double d_ac = GeometryUtils.calculateDistance(0, 0, 100, 100);
        double d_ab = GeometryUtils.calculateDistance(0, 0, 50, 0);
        double d_bc = GeometryUtils.calculateDistance(50, 0, 100, 100);
        
        assertTrue("Triangle inequality", d_ac <= d_ab + d_bc + 0.01);
    }
    
    @Test
    public void testVerySmallDistance() {
        double distance = GeometryUtils.calculateDistance(100.001, 200.001, 100.002, 200.002);
        assertTrue("Very small distance", distance < 0.01);
        assertTrue("Very small distance positive", distance > 0);
    }
    
    @Test
    public void testDistanceIsPositive() {
        double distance = GeometryUtils.calculateDistance(-100, -200, 100, 200);
        assertTrue("Distance should always be positive", distance >= 0);
    }
}
