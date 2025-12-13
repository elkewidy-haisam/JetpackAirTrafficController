/**
 * Collection of static utility methods for geometric calculations used throughout the application.
 * 
 * Purpose:
 * Provides reusable geometric computation functions to eliminate code duplication and ensure consistent
 * mathematical operations across the codebase. Includes distance calculations, angle computations, point
 * transformations, range checks, and clamping operations used extensively in flight path calculations,
 * collision detection, and UI rendering.
 * 
 * Key Responsibilities:
 * - Calculate Euclidean distances between points (2D coordinate space)
 * - Perform point-to-point and point-to-coordinate distance measurements
 * - Create Point objects from double coordinates with proper rounding
 * - Calculate angles between points for heading/direction computation
 * - Perform range/proximity checks for collision detection
 * - Clamp values to valid ranges (min/max boundaries)
 * - Support movement vector calculations for flight path updates
 * 
 * Interactions:
 * - Used by JetPackFlight for movement and distance calculations
 * - Referenced by CollisionDetector for proximity detection
 * - Utilized by FlightEmergencyHandler for nearest parking space searches
 * - Applied in NavigationCalculator for path planning
 * - Supports UI rendering for object positioning
 * 
 * Patterns & Constraints:
 * - Pure utility class with only static methods (stateless)
 * - Thread-safe due to lack of mutable state
 * - Uses standard Java Math library for calculations
 * - No external dependencies beyond java.awt.Point
 * - Alternative to/duplicate of NavigationCalculator in some functions
 * 
 * @author Haisam Elkewidy
 */

package com.example.utility;

public class GeometryUtils {
    /**
     * Calculates Euclidean distance between two points using double precision.
     * Uses Pythagorean theorem: distance = sqrt((x2-x1)² + (y2-y1)²)
     * 
     * @param x1 first point x-coordinate
     * @param y1 first point y-coordinate
     * @param x2 second point x-coordinate
     * @param y2 second point y-coordinate
     * @return distance between the two points
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;                 // Calculate horizontal distance
        double dy = y2 - y1;                 // Calculate vertical distance
        return Math.sqrt(dx * dx + dy * dy); // Apply Pythagorean theorem
    }

    /**
     * Calculates Euclidean distance between two points using integer coordinates.
     * Converts int coordinates to double for calculation.
     * 
     * @param x1 first point x-coordinate (integer)
     * @param y1 first point y-coordinate (integer)
     * @param x2 second point x-coordinate (integer)
     * @param y2 second point y-coordinate (integer)
     * @return distance between the two points as double
     */
    public static double calculateDistance(int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;                 // Calculate horizontal distance (auto-converted to double)
        double dy = y2 - y1;                 // Calculate vertical distance (auto-converted to double)
        return Math.sqrt(dx * dx + dy * dy); // Apply Pythagorean theorem
    }

    /**
     * Calculates Euclidean distance between two points using double coordinates.
     * Overloaded version for explicit double parameter types.
     * 
     * @param x1 first point x-coordinate (double)
     * @param y1 first point y-coordinate (double)
     * @param x2 second point x-coordinate (double)
     * @param y2 second point y-coordinate (double)
     * @return distance between the two points
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;                 // Calculate horizontal distance
        double dy = y2 - y1;                 // Calculate vertical distance
        return Math.sqrt(dx * dx + dy * dy); // Apply Pythagorean theorem
    }

}
