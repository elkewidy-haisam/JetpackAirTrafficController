/**
 * GeometryUtils component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides geometryutils functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core geometryutils operations
 * - Maintain necessary state for geometryutils functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility;

public class GeometryUtils {
    /**
     * Calculates Euclidean distance between two points.
     * 
     * @param x1 X-coordinate of first point
     * @param y1 Y-coordinate of first point
     * @param x2 X-coordinate of second point
     * @param y2 Y-coordinate of second point
     * @return Euclidean distance between the two points
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        // Calculate horizontal distance component
        double dx = x2 - x1;
        // Calculate vertical distance component
        double dy = y2 - y1;
        // Apply Pythagorean theorem: distance = sqrt(dx² + dy²)
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculates Euclidean distance between two integer coordinate points.
     * 
     * @param x1 X-coordinate of first point (integer)
     * @param y1 Y-coordinate of first point (integer)
     * @param x2 X-coordinate of second point (integer)
     * @param y2 Y-coordinate of second point (integer)
     * @return Euclidean distance as double
     */
    public static double calculateDistance(int x1, int y1, int x2, int y2) {
        // Calculate horizontal distance (integers implicitly cast to double)
        double dx = x2 - x1;
        // Calculate vertical distance (integers implicitly cast to double)
        double dy = y2 - y1;
        // Apply Pythagorean theorem: distance = sqrt(dx² + dy²)
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculates Euclidean distance between two double coordinate points.
     * 
     * @param x1 X-coordinate of first point (double)
     * @param y1 Y-coordinate of first point (double)
     * @param x2 X-coordinate of second point (double)
     * @param y2 Y-coordinate of second point (double)
     * @return Euclidean distance as double
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        // Calculate horizontal distance component
        double dx = x2 - x1;
        // Calculate vertical distance component
        double dy = y2 - y1;
        // Apply Pythagorean theorem: distance = sqrt(dx² + dy²)
        return Math.sqrt(dx * dx + dy * dy);
    }

}
