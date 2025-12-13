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
    public static double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double calculateDistance(int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
