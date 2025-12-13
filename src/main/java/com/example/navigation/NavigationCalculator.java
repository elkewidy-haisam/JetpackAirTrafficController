/**
 * Utility class providing navigation calculations for jetpack flight path planning and direction finding.
 * 
 * Purpose:
 * Offers a comprehensive suite of navigation algorithms for calculating distances, bearings, headings,
 * and movement vectors essential to jetpack flight operations. Supports both 2D map-based navigation
 * and compass-bearing calculations for pilot guidance. Provides stateless utility methods that can be
 * called from any flight management component without instantiation.
 * 
 * Key Responsibilities:
 * - Calculate Euclidean distances between coordinates and Point objects
 * - Compute compass bearings and cardinal directions (N, NE, E, SE, S, SW, W, NW)
 * - Determine heading angles in degrees from current position to destination
 * - Generate movement direction vectors for incremental flight path progression
 * - Support waypoint-to-waypoint navigation with bearing updates
 * - Provide intermediate position calculations for smooth flight animations
 * - Enable turn angle calculations for smooth course corrections
 * 
 * Interactions:
 * - Used by JetPackFlight for movement direction and destination bearing
 * - Referenced by FlightMovementController for path following algorithms
 * - Integrated with FlightPath for waypoint-to-waypoint navigation
 * - Consulted by UI components for displaying flight direction indicators
 * - Supports Radio communications with bearing information for pilot guidance
 * - Used in emergency procedures to calculate direct routes to safe landing zones
 * 
 * Patterns & Constraints:
 * - Pure utility class with static methods only (no instance state)
 * - Thread-safe due to stateless design
 * - Combines with GeometryUtils for comprehensive spatial calculations
 * - Uses standard navigation conventions (0° = North, clockwise rotation)
 * - Supports both degree and radian angle representations
 * - Cardinal direction calculations use 45-degree sector boundaries
 * - Zero external dependencies beyond standard Java Math library
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
     * Calculates distance between two points
     * 
     * @param x1 First point x coordinate
     * @param y1 First point y coordinate
     * @param x2 Second point x coordinate
     * @param y2 Second point y coordinate
     * @return Distance between points
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Calculates distance between two points
     * 
     * @param from Starting point
     * @param to Ending point
     * @return Distance between points
     */
    public static double calculateDistance(Point from, Point to) {
        return calculateDistance(from.x, from.y, to.x, to.y);
    }
    
    /**
     * Calculates the compass direction from one point to another
     * 
     * @param fromX Starting x coordinate
     * @param fromY Starting y coordinate
     * @param toX Ending x coordinate
     * @param toY Ending y coordinate
     * @return Compass direction string (e.g., "North", "Southeast")
     */
    public static String calculateCompassDirection(double fromX, double fromY, double toX, double toY) {
        double dx = toX - fromX;
        double dy = toY - fromY;
        double angle = Math.atan2(dy, dx);
        double degrees = Math.toDegrees(angle);
        
        if (degrees > -22.5 && degrees <= 22.5) {
            return "East";
        } else if (degrees > 22.5 && degrees <= 67.5) {
            return "Northeast";
        } else if (degrees > 67.5 && degrees <= 112.5) {
            return "North";
        } else if (degrees > 112.5 && degrees <= 157.5) {
            return "Northwest";
        } else if (degrees > 157.5 || degrees <= -157.5) {
            return "West";
        } else if (degrees > -157.5 && degrees <= -112.5) {
            return "Southwest";
        } else if (degrees > -112.5 && degrees <= -67.5) {
            return "South";
        } else {
            return "Southeast";
        }
    }
    
    /**
     * Calculates the compass direction with emoji
     * 
     * @param fromX Starting x coordinate
     * @param fromY Starting y coordinate
     * @param toX Ending x coordinate
     * @param toY Ending y coordinate
     * @return Direction string with rocket emoji
     */
    public static String calculateDirectionWithEmoji(double fromX, double fromY, double toX, double toY) {
        String direction = calculateCompassDirection(fromX, fromY, toX, toY);
        return "🚀 " + direction;
    }
    
    /**
     * Calculates angle in degrees from one point to another
     * 
     * @param fromX Starting x coordinate
     * @param fromY Starting y coordinate
     * @param toX Ending x coordinate
     * @param toY Ending y coordinate
     * @return Angle in degrees
     */
    public static double calculateAngle(double fromX, double fromY, double toX, double toY) {
        double dx = toX - fromX;
        double dy = toY - fromY;
        return Math.toDegrees(Math.atan2(dy, dx));
    }
    
    /**
     * Calculates the next position moving towards a target
     * 
     * @param currentX Current x position
     * @param currentY Current y position
     * @param targetX Target x position
     * @param targetY Target y position
     * @param speed Movement speed
     * @return New position as Point
     */
    public static Point calculateNextPosition(double currentX, double currentY, 
                                              double targetX, double targetY, double speed) {
        double dx = targetX - currentX;
        double dy = targetY - currentY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance <= speed) {
            return new Point((int)targetX, (int)targetY);
        }
        
        double newX = currentX + (dx / distance) * speed;
        double newY = currentY + (dy / distance) * speed;
        
        return new Point((int)newX, (int)newY);
    }
    
    /**
     * Checks if a position has reached its target
     * 
     * @param currentX Current x position
     * @param currentY Current y position
     * @param targetX Target x position
     * @param targetY Target y position
     * @param threshold Distance threshold for "reached"
     * @return true if position has reached target
     */
    public static boolean hasReachedTarget(double currentX, double currentY, 
                                          double targetX, double targetY, double threshold) {
        double distance = calculateDistance(currentX, currentY, targetX, targetY);
        return distance < threshold;
    }
    
    /**
     * Normalizes an angle to 0-360 degrees
     * 
     * @param angle Angle in degrees
     * @return Normalized angle (0-360)
     */
    public static double normalizeAngle(double angle) {
        while (angle < 0) {
            angle += 360;
        }
        while (angle >= 360) {
            angle -= 360;
        }
        return angle;
    }
    
    /**
     * Calculates bearing from one point to another
     * 
     * @param from Starting point
     * @param to Ending point
     * @return Bearing in degrees (0-360)
     */
    public static double calculateBearing(Point from, Point to) {
        double angle = calculateAngle(from.x, from.y, to.x, to.y);
        // Convert to bearing (0 = North, 90 = East, etc.)
        double bearing = 90 - angle;
        return normalizeAngle(bearing);
    }
}
