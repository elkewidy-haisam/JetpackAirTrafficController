/**
 * Provides navigation calculations including distance, direction, and compass headings for jetpack flight paths.
 * 
 * Purpose:
 * Utility class containing static methods for calculating navigation data required by the air traffic
 * control system. Computes distances between points, compass directions (N, NE, E, etc.), and provides
 * helper functions for flight path planning and movement vector calculations.
 * 
 * Key Responsibilities:
 * - Calculate Euclidean distances between coordinates
 * - Convert angle calculations to compass direction strings (North, Southeast, etc.)
 * - Provide navigation utilities for jetpack flight path planning
 * - Support movement direction calculations for UI displays and logging
 * 
 * Interactions:
 * - Used by JetPackFlight for movement direction calculations
 * - Referenced by FlightMovementController for path planning
 * - Utilized by UI components to display jetpack heading information
 * - Supports logging systems with human-readable direction strings
 * 
 * Patterns & Constraints:
 * - Pure utility class with only static methods (stateless)
 * - Thread-safe due to lack of mutable state
 * - Uses standard Java Point class for coordinate representation
 * - Relies on Math.atan2 for angle calculations with proper quadrant handling
 * - Alternative to GeometryUtils with navigation-specific functionality
 * 
 * @author Haisam Elkewidy
 */

// NavigationCalculator.java
// by Haisam Elkewidy
// Provides navigation and direction calculation utilities for jetpack flights.

package com.example.navigation;

import java.awt.Point;

/**
 * NavigationCalculator
 * Provides navigation and direction calculation utilities for jetpack flights.
 * Calculates directions, compass headings, and movement vectors.
 */
public class NavigationCalculator {
    
    /**
     * Calculates Euclidean distance between two points using coordinates.
     * Uses the Pythagorean theorem: distance = sqrt((x2-x1)² + (y2-y1)²)
     * 
     * @param x1 First point x coordinate
     * @param y1 First point y coordinate
     * @param x2 Second point x coordinate
     * @param y2 Second point y coordinate
     * @return Distance between points as a double
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;                 // Calculate horizontal distance component
        double dy = y2 - y1;                 // Calculate vertical distance component
        return Math.sqrt(dx * dx + dy * dy); // Apply Pythagorean theorem
    }
    
    /**
     * Calculates Euclidean distance between two Point objects.
     * Convenience overload that extracts coordinates from Point objects.
     * 
     * @param from Starting point
     * @param to Ending point
     * @return Distance between points as a double
     */
    public static double calculateDistance(Point from, Point to) {
        // Delegate to coordinate-based method using Point x,y values
        return calculateDistance(from.x, from.y, to.x, to.y);
    }
    
    /**
     * Calculates the compass direction from one point to another.
     * Returns a cardinal or intercardinal direction string.
     * Uses angle ranges: East (±22.5°), Northeast (22.5-67.5°), etc.
     * 
     * @param fromX Starting x coordinate
     * @param fromY Starting y coordinate
     * @param toX Ending x coordinate
     * @param toY Ending y coordinate
     * @return Compass direction string (e.g., "North", "Southeast")
     */
    public static String calculateCompassDirection(double fromX, double fromY, double toX, double toY) {
        double dx = toX - fromX;              // Calculate x offset
        double dy = toY - fromY;              // Calculate y offset
        double angle = Math.atan2(dy, dx);    // Get angle in radians
        double degrees = Math.toDegrees(angle); // Convert to degrees (-180 to 180)
        
        // Check which 45-degree sector the angle falls into
        if (degrees > -22.5 && degrees <= 22.5) {
            return "East";       // 0° ± 22.5°
        } else if (degrees > 22.5 && degrees <= 67.5) {
            return "Northeast";  // 45° ± 22.5°
        } else if (degrees > 67.5 && degrees <= 112.5) {
            return "North";      // 90° ± 22.5°
        } else if (degrees > 112.5 && degrees <= 157.5) {
            return "Northwest";  // 135° ± 22.5°
        } else if (degrees > 157.5 || degrees <= -157.5) {
            return "West";       // 180° or -180°
        } else if (degrees > -157.5 && degrees <= -112.5) {
            return "Southwest";  // -135° ± 22.5°
        } else if (degrees > -112.5 && degrees <= -67.5) {
            return "South";      // -90° ± 22.5°
        } else {
            return "Southeast";  // -45° ± 22.5°
        }
    }
    
    /**
     * Calculates the compass direction and prepends a rocket emoji.
     * Useful for UI displays and logging with visual indicators.
     * 
     * @param fromX Starting x coordinate
     * @param fromY Starting y coordinate
     * @param toX Ending x coordinate
     * @param toY Ending y coordinate
     * @return Direction string prefixed with rocket emoji (e.g., "🚀 North")
     */
    public static String calculateDirectionWithEmoji(double fromX, double fromY, double toX, double toY) {
        String direction = calculateCompassDirection(fromX, fromY, toX, toY); // Get direction
        return "🚀 " + direction;  // Prepend rocket emoji
    }
    
    /**
     * Calculates angle in degrees from one point to another.
     * Returns angle in standard mathematical notation (-180 to 180).
     * 
     * @param fromX Starting x coordinate
     * @param fromY Starting y coordinate
     * @param toX Ending x coordinate
     * @param toY Ending y coordinate
     * @return Angle in degrees (-180 to 180)
     */
    public static double calculateAngle(double fromX, double fromY, double toX, double toY) {
        double dx = toX - fromX;              // Calculate x offset
        double dy = toY - fromY;              // Calculate y offset
        return Math.toDegrees(Math.atan2(dy, dx)); // Convert radians to degrees
    }
    
    /**
     * Calculates the next position when moving towards a target at given speed.
     * If target is within speed distance, returns target position directly.
     * Otherwise, calculates intermediate position along the path.
     * 
     * @param currentX Current x position
     * @param currentY Current y position
     * @param targetX Target x position
     * @param targetY Target y position
     * @param speed Movement speed per update
     * @return New position as Point object
     */
    public static Point calculateNextPosition(double currentX, double currentY, 
                                              double targetX, double targetY, double speed) {
        double dx = targetX - currentX;              // Calculate x distance to target
        double dy = targetY - currentY;              // Calculate y distance to target
        double distance = Math.sqrt(dx * dx + dy * dy); // Calculate total distance
        
        // If within speed distance, move directly to target
        if (distance <= speed) {
            return new Point((int)targetX, (int)targetY); // Return target position
        }
        
        // Calculate next position along the path
        double newX = currentX + (dx / distance) * speed; // Move speed units in x direction
        double newY = currentY + (dy / distance) * speed; // Move speed units in y direction
        
        return new Point((int)newX, (int)newY);  // Return new position as Point
    }
    
    /**
     * Checks if current position has reached the target within threshold distance.
     * Used to determine if navigation is complete.
     * 
     * @param currentX Current x position
     * @param currentY Current y position
     * @param targetX Target x position
     * @param targetY Target y position
     * @param threshold Distance threshold for "reached" status
     * @return true if within threshold distance, false otherwise
     */
    public static boolean hasReachedTarget(double currentX, double currentY, 
                                          double targetX, double targetY, double threshold) {
        // Calculate distance to target
        double distance = calculateDistance(currentX, currentY, targetX, targetY);
        return distance < threshold;  // Return true if within threshold
    }
    
    /**
     * Normalizes an angle to the 0-360 degree range.
     * Converts any angle to equivalent angle in [0, 360) range.
     * 
     * @param angle Angle in degrees (any value)
     * @return Normalized angle between 0 and 360
     */
    public static double normalizeAngle(double angle) {
        // Add 360 until angle is non-negative
        while (angle < 0) {
            angle += 360;  // Shift negative angles into positive range
        }
        // Subtract 360 until angle is less than 360
        while (angle >= 360) {
            angle -= 360;  // Wrap angles >= 360 back into range
        }
        return angle;  // Return normalized angle
    }
    
    /**
     * Calculates bearing from one point to another.
     * Bearing is measured clockwise from North (0° = North, 90° = East, etc.)
     * 
     * @param from Starting point
     * @param to Ending point
     * @return Bearing in degrees (0-360, where 0 = North)
     */
    public static double calculateBearing(Point from, Point to) {
        // Calculate standard angle from from point to point
        double angle = calculateAngle(from.x, from.y, to.x, to.y);
        // Convert to bearing (0 = North, 90 = East, etc.)
        double bearing = 90 - angle;  // Rotate coordinate system
        return normalizeAngle(bearing);  // Ensure result is in 0-360 range
    }
}
