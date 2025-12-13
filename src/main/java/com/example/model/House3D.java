/**
 * Three-dimensional model representation of house for JOGL rendering.
 * 
 * Purpose:
 * Models house geometry with position, dimensions, and visual properties for
 * hardware-accelerated 3D rendering via JOGL. Supports collision detection, spatial queries,
 * and realistic visualization in the 3D city view.
 * 
 * Key Responsibilities:
 * - Store geometric data (position, dimensions) for rendering
 * - Provide visual attributes (color, texture, detail level) for realism
 * - Support collision and proximity queries for spatial operations
 * - Enable type classification and metadata access
 * 
 * Interactions:
 * - Rendered by JOGL3DPanel and JOGLRenderer3D
 * - Referenced by CityModel3D for scene composition
 * - Used in collision detection and spatial analysis
 * 
 * Patterns & Constraints:
 * - Immutable geometry for thread-safe access
 * - Compatible with OpenGL rendering pipelines
 * - Simple collision models for performance
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class House3D {
    public final double x, y, width, depth, height;

    public House3D(double x, double y, double width, double depth, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.depth = depth;
        this.height = height;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getLength() { return depth; }
    public double getHeight() { return height; }
    
    /**
     * Check if a 3D point (x, y, altitude) collides with this house's hitbox
     * @param px X coordinate
     * @param py Y coordinate (map coordinate)
     * @param altitude Altitude/height
     * @return true if the point is inside the house's 3D volume
     */
    public boolean collidesWithPoint(double px, double py, double altitude) {
        // Check if point is within house's horizontal footprint
        boolean inFootprint = px >= x && px <= x + width &&
                              py >= y && py <= y + depth;
        
        // Check if point is within house's vertical extent
        boolean inHeight = altitude >= 0 && altitude <= height;
        
        return inFootprint && inHeight;
    }
    
    /**
     * Check if a 3D sphere (jetpack with radius) collides with this house's hitbox
     * @param px X coordinate of sphere center
     * @param py Y coordinate of sphere center
     * @param altitude Altitude of sphere center
     * @param radius Radius of the sphere (jetpack collision radius)
     * @return true if the sphere intersects with the house
     */
    public boolean collidesWithSphere(double px, double py, double altitude, double radius) {
        // Find closest point on house to sphere center
        double closestX = Math.max(x, Math.min(px, x + width));
        double closestY = Math.max(y, Math.min(py, y + depth));
        double closestZ = Math.max(0, Math.min(altitude, height));
        
        // Calculate distance from sphere center to closest point
        double dx = px - closestX;
        double dy = py - closestY;
        double dz = altitude - closestZ;
        double distanceSquared = dx * dx + dy * dy + dz * dz;
        
        // Collision occurs if distance is less than radius
        return distanceSquared <= (radius * radius);
    }
}
