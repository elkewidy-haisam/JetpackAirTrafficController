
package com.example.utility.geometry;
import java.awt.Point;

/**
 * GeometryUtils.java
 * Utility class for common geometric calculations used across the codebase.
 * Centralizes distance calculations, point operations, and coordinate transformations.
 *
 * Last updated: December 10, 2025
 * Author: Jetpack Air Traffic Controller Team
 */
public class GeometryUtils {
        /**
         * Creates a new Point from double coordinates (converts to int)
         * Always round down for both positive and negative
         */
        public static Point createPoint(double x, double y) {
            int px = x < 0 ? (int)Math.ceil(x) : (int)Math.floor(x);
            int py = y < 0 ? (int)Math.ceil(y) : (int)Math.floor(y);
            return new Point(px, py);
        }
    
    /**
     * Calculates the Euclidean distance between two points
     * 
     * @param x1 X coordinate of first point
     * @param y1 Y coordinate of first point
     * @param x2 X coordinate of second point
     * @param y2 Y coordinate of second point
     * @return Distance between the two points
     */
    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

        // Added for test compatibility
        public static double calculateDistance(int x1, int y1, int x2, int y2) {
            return calculateDistance((double)x1, (double)y1, (double)x2, (double)y2);
        }

    
    /**
     * Calculates the Euclidean distance between two points
     * 
     * @param p1 First point
     * @param p2 Second point
     * @return Distance between the two points
     */
    public static double calculateDistance(Point p1, Point p2) {
        return calculateDistance(p1.x, p1.y, p2.x, p2.y);
    }
    
    /**
     * Calculates the distance from a point to coordinates
     * 
     * @param from Starting point
     * @param toX Target X coordinate
     * @param toY Target Y coordinate
     * @return Distance to target
     */
    public static double calculateDistance(Point from, double toX, double toY) {
        return calculateDistance(from.x, from.y, toX, toY);
    }
    
    /**
     * Creates a new Point from double coordinates (converts to int)
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @return New Point with integer coordinates
     */
    
    /**
     * Calculates the angle (in radians) from one point to another
     * 
     * @param fromX Starting X coordinate
     * @param fromY Starting Y coordinate
     * @param toX Target X coordinate
     * @param toY Target Y coordinate
     * @return Angle in radians
     */
    public static double calculateAngle(double fromX, double fromY, double toX, double toY) {
        return Math.atan2(toY - fromY, toX - fromX);
    }
    
    /**
     * Calculates the angle (in radians) from one point to another
     * 
     * @param from Starting point
     * @param to Target point
     * @return Angle in radians
     */
    public static double calculateAngle(Point from, Point to) {
        return calculateAngle(from.x, from.y, to.x, to.y);
    }
    
    /**
     * Moves a point toward a target by a specified distance
     * 
     * @param currentX Current X coordinate
     * @param currentY Current Y coordinate
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     * @param speed Movement speed/distance
     * @return New point after movement
     */
    public static Point moveToward(double currentX, double currentY, double targetX, double targetY, double speed) {
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
     * Checks if a point is within a circular range of another point
     * 
     * @param x1 X coordinate of first point
     * @param y1 Y coordinate of first point
     * @param x2 X coordinate of second point
     * @param y2 Y coordinate of second point
     * @param range Maximum distance for proximity
     * @return true if points are within range, false otherwise
     */
    public static boolean isWithinRange(double x1, double y1, double x2, double y2, double range) {
        return calculateDistance(x1, y1, x2, y2) <= range;
    }
    
    /**
     * Checks if a point is within a circular range of another point
     * 
     * @param p1 First point
     * @param p2 Second point
     * @param range Maximum distance for proximity
     * @return true if points are within range, false otherwise
     */
    public static boolean isWithinRange(Point p1, Point p2, double range) {
        return calculateDistance(p1, p2) <= range;
    }
    
    /**
     * Clamps a value between a minimum and maximum
     * 
     * @param value Value to clamp
     * @param min Minimum value
     * @param max Maximum value
     * @return Clamped value
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Clamps a value between a minimum and maximum (integer version)
     * 
     * @param value Value to clamp
     * @param min Minimum value
     * @param max Maximum value
     * @return Clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
