/**
 * Utility class for geometric calculations used throughout the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides centralized, reusable mathematical operations for 2D spatial calculations essential to
 * jetpack flight tracking, collision detection, proximity analysis, and position management. Eliminates
 * code duplication by consolidating distance calculations, point creation, and coordinate operations
 * into a single, well-tested utility class.
 * 
 * Key Responsibilities:
 * - Calculate Euclidean distances between coordinates and Point objects
 * - Create Point instances from double-precision coordinates with proper rounding
 * - Provide overloaded methods supporting both int and double coordinate systems
 * - Enable efficient distance-based queries for collision detection and proximity alerts
 * - Support geometric operations for flight path calculations and spatial analysis
 * 
 * Interactions:
 * - Used by JetPackFlight for distance calculations during movement updates
 * - Referenced by CollisionDetector for proximity and collision analysis
 * - Integrated with FlightEmergencyHandler for nearest parking space location
 * - Utilized throughout UI components for rendering position calculations
 * - Supports NavigationCalculator and spatial query operations
 * 
 * Patterns & Constraints:
 * - Pure utility class with static methods only (no instance state)
 * - Thread-safe due to stateless design and immutable operations
 * - Uses standard Pythagorean theorem for distance calculations
 * - Supports both integer and double-precision coordinate systems for flexibility
 * - Zero external dependencies; relies only on java.awt.Point and Math
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
