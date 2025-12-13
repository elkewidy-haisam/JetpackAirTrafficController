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
    
    /**
     * Tests distance calculation for same point.
     * Verifies that distance from point to itself is 0.
     */
    @Test
    public void testCalculateDistanceSamePoint() {
        double distance = GeometryUtils.calculateDistance(100, 200, 100, 200);  // Calculate distance to same point
        assertEquals("Distance to same point should be 0", 0.0, distance, 0.01);  // Verify zero distance
    }
    
    /**
     * Tests horizontal distance calculation.
     * Verifies distance along X-axis with no Y change.
     */
    @Test
    public void testCalculateDistanceHorizontal() {
        double distance = GeometryUtils.calculateDistance(0, 0, 100, 0);  // Calculate horizontal distance
        assertEquals("Horizontal distance", 100.0, distance, 0.01);  // Verify 100 units
    }
    
    /**
     * Tests vertical distance calculation.
     * Verifies distance along Y-axis with no X change.
     */
    @Test
    public void testCalculateDistanceVertical() {
        double distance = GeometryUtils.calculateDistance(0, 0, 0, 100);  // Calculate vertical distance
        assertEquals("Vertical distance", 100.0, distance, 0.01);  // Verify 100 units
    }
    
    /**
     * Tests diagonal distance calculation using 3-4-5 Pythagorean triple.
     * Verifies correct application of Pythagorean theorem.
     */
    @Test
    public void testCalculateDistanceDiagonal() {
        // 3-4-5 triangle
        double distance = GeometryUtils.calculateDistance(0, 0, 3, 4);  // Calculate diagonal (3-4-5 triangle)
        assertEquals("Diagonal distance (3-4-5 triangle)", 5.0, distance, 0.01);  // Verify hypotenuse is 5
    }
    
    /**
     * Tests distance calculation with large coordinate values.
     * Verifies correct handling of large numbers.
     */
    @Test
    public void testCalculateDistanceLargeValues() {
        double distance = GeometryUtils.calculateDistance(1000, 1000, 2000, 2000);  // Calculate with large coords
        assertEquals("Large values distance", Math.sqrt(2000000), distance, 0.01);  // Verify sqrt(1000^2 + 1000^2)
    }
    
    /**
     * Tests distance calculation with negative coordinates.
     * Verifies correct handling across coordinate quadrants.
     */
    @Test
    public void testCalculateDistanceNegativeCoordinates() {
        double distance = GeometryUtils.calculateDistance(-100, -100, 100, 100);  // Calculate across quadrants
        assertEquals("Negative coordinates", Math.sqrt(80000), distance, 0.01);  // Verify sqrt(200^2 + 200^2)
    }
    
    /**
     * Tests distance calculation with double precision coordinates.
     * Verifies floating point arithmetic correctness.
     */
    @Test
    public void testCalculateDistanceWithDoubles() {
        double distance = GeometryUtils.calculateDistance(10.5, 20.5, 15.5, 25.5);  // Calculate with doubles
        double expected = Math.sqrt(25 + 25);  // 5^2 + 5^2 = sqrt(50)
        assertEquals("Double precision", expected, distance, 0.01);  // Verify result
    }
    
    /**
     * Tests Point creation from double coordinates.
     * Verifies conversion from double to integer coordinates.
     */
    @Test
    public void testCreatePoint() {
        Point p = GeometryUtils.createPoint(123.456, 789.123);  // Create Point from doubles
        assertNotNull("Point should be created", p);  // Verify Point exists
        assertEquals("Point X", 123, p.x);  // Verify X truncated to 123
        assertEquals("Point Y", 789, p.y);  // Verify Y truncated to 789
    }
    
    /**
     * Tests Point creation with rounding behavior.
     * Verifies that doubles are truncated (not rounded) to integers.
     */
    @Test
    public void testCreatePointRounding() {
        Point p1 = GeometryUtils.createPoint(10.4, 20.4);  // Create with .4 decimals
        assertEquals("Should round down", 10, p1.x);  // Verify truncated to 10
        assertEquals("Should round down", 20, p1.y);  // Verify truncated to 20
        
        Point p2 = GeometryUtils.createPoint(10.6, 20.6);  // Create with .6 decimals
        assertEquals("Should round down (truncate)", 10, p2.x);  // Verify truncated to 10 (not rounded to 11)
        assertEquals("Should round down (truncate)", 20, p2.y);  // Verify truncated to 20 (not rounded to 21)
    }
    
    /**
     * Tests Point creation with negative coordinates.
     * Verifies correct handling of negative values.
     */
    @Test
    public void testCreatePointNegative() {
        Point p = GeometryUtils.createPoint(-50.5, -100.5);  // Create with negative coords
        assertEquals("Negative X", -50, p.x);  // Verify negative X truncated
        assertEquals("Negative Y", -100, p.y);  // Verify negative Y truncated
    }
    
    /**
     * Tests Point creation at origin.
     * Verifies correct handling of zero coordinates.
     */
    @Test
    public void testCreatePointZero() {
        Point p = GeometryUtils.createPoint(0.0, 0.0);  // Create at origin
        assertEquals("Zero X", 0, p.x);  // Verify X is 0
        assertEquals("Zero Y", 0, p.y);  // Verify Y is 0
    }
    
    /**
     * Tests Pythagorean theorem with various Pythagorean triples.
     * Verifies correct distance calculation for known right triangles.
     */
    @Test
    public void testPythagoreanTheorem() {
        // Test various Pythagorean triples
        assertEquals("3-4-5", 5.0, GeometryUtils.calculateDistance(0, 0, 3, 4), 0.01);  // 3-4-5 triangle
        assertEquals("5-12-13", 13.0, GeometryUtils.calculateDistance(0, 0, 5, 12), 0.01);  // 5-12-13 triangle
        assertEquals("8-15-17", 17.0, GeometryUtils.calculateDistance(0, 0, 8, 15), 0.01);  // 8-15-17 triangle
        assertEquals("7-24-25", 25.0, GeometryUtils.calculateDistance(0, 0, 7, 24), 0.01);  // 7-24-25 triangle
    }
    
    /**
     * Tests distance calculation symmetry.
     * Verifies that d(A,B) = d(B,A).
     */
    @Test
    public void testSymmetricDistance() {
        double d1 = GeometryUtils.calculateDistance(10, 20, 50, 60);  // Calculate A to B
        double d2 = GeometryUtils.calculateDistance(50, 60, 10, 20);  // Calculate B to A
        assertEquals("Distance should be symmetric", d1, d2, 0.01);  // Verify d(A,B) = d(B,A)
    }
    
    /**
     * Tests distance calculation commutativity.
     * Verifies that argument order doesn't affect result.
     */
    @Test
    public void testDistanceCommutative() {
        // Test that order doesn't matter
        double d1 = GeometryUtils.calculateDistance(100, 200, 300, 400);  // Calculate forward
        double d2 = GeometryUtils.calculateDistance(300, 400, 100, 200);  // Calculate reverse
        assertEquals("Distance is commutative", d1, d2, 0.001);  // Verify same result
    }
    
    /**
     * Tests triangle inequality property.
     * Verifies that d(A,C) <= d(A,B) + d(B,C) for any three points.
     */
    @Test
    public void testDistanceTriangleInequality() {
        // For any three points, d(A,C) <= d(A,B) + d(B,C)
        double d_ac = GeometryUtils.calculateDistance(0, 0, 100, 100);  // Direct distance A to C
        double d_ab = GeometryUtils.calculateDistance(0, 0, 50, 0);  // Distance A to B
        double d_bc = GeometryUtils.calculateDistance(50, 0, 100, 100);  // Distance B to C
        
        assertTrue("Triangle inequality", d_ac <= d_ab + d_bc + 0.01);  // Verify d(A,C) <= d(A,B) + d(B,C)
    }
    
    /**
     * Tests very small distance calculation.
     * Verifies correct handling of points very close together.
     */
    @Test
    public void testVerySmallDistance() {
        double distance = GeometryUtils.calculateDistance(100.001, 200.001, 100.002, 200.002);  // Very close points
        assertTrue("Very small distance", distance < 0.01);  // Verify distance is very small
        assertTrue("Very small distance positive", distance > 0);  // Verify distance is positive
    }
    
    /**
     * Tests that distance is always positive.
     * Verifies non-negativity property of distance metric.
     */
    @Test
    public void testDistanceIsPositive() {
        double distance = GeometryUtils.calculateDistance(-100, -200, 100, 200);  // Calculate any distance
        assertTrue("Distance should always be positive", distance >= 0);  // Verify non-negative
    }
}
