/**
 * Three-dimensional model representation of building for JOGL rendering.
 * 
 * Purpose:
 * Models building geometry with position, dimensions, and visual properties for
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

import java.awt.Color;

/**
 * Building3D - 3D building representation for city rendering and collision.
 * Includes position, dimensions, color, type, and footprint logic.
 */
public class Building3D {
    private final double x;          // X position on map
    private final double y;          // Y position on map
    private final double width;      // Building width
    private final double depth;      // Building depth
    private final double height;     // Building height (altitude)
    private Color color;             // Building color
    private final String type;       // Building type (skyscraper, office, residential, etc.)
    private final boolean hasWindows;
    private final int floors;
    
    public Building3D(double x, double y, double width, double depth, double height, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.type = type;
        this.hasWindows = true;
        this.floors = (int)(height / 10); // Approximate floor count
        assignColorByType();
    }
    
    private void assignColorByType() {
        switch (type) {
            case "skyscraper":
                color = new Color(100, 120, 140); // Dark blue-grey
                break;
            case "office":
                color = new Color(140, 140, 140); // Grey
                break;
            case "residential":
                color = new Color(180, 160, 140); // Tan/beige
                break;
            case "commercial":
                color = new Color(160, 140, 120); // Light brown
                break;
            case "landmark":
                color = new Color(150, 130, 110); // Distinctive color
                break;
            default:
                color = new Color(130, 130, 130);
        }
    }
    
    /**
     * Check if a point is inside this building's footprint
     */
    public boolean containsPoint(double px, double py) {
        return px >= x && px <= x + width &&
               py >= y && py <= y + depth;
    }
    
    /**
     * Get distance from a point to this building
     */
    public double distanceTo(double px, double py) {
        // Distance to closest point on building rectangle
        double dx = Math.max(x - px, Math.max(0, px - (x + width)));
        double dy = Math.max(y - py, Math.max(0, py - (y + depth)));
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Check if a 3D point (x, y, altitude) collides with this building's hitbox
     * @param px X coordinate
     * @param py Y coordinate (map coordinate)
     * @param altitude Altitude/height
     * @return true if the point is inside the building's 3D volume
     */
    public boolean collidesWithPoint(double px, double py, double altitude) {
        // Check if point is within building's horizontal footprint
        boolean inFootprint = px >= x && px <= x + width &&
                              py >= y && py <= y + depth;
        
        // Check if point is within building's vertical extent
        boolean inHeight = altitude >= 0 && altitude <= height;
        
        return inFootprint && inHeight;
    }
    
    /**
     * Check if a 3D sphere (jetpack with radius) collides with this building's hitbox
     * @param px X coordinate of sphere center
     * @param py Y coordinate of sphere center
     * @param altitude Altitude of sphere center
     * @param radius Radius of the sphere (jetpack collision radius)
     * @return true if the sphere intersects with the building
     */
    public boolean collidesWithSphere(double px, double py, double altitude, double radius) {
        // Find closest point on building to sphere center
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
    
    // Getters and setters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getLength() { return depth; }
    public double getHeight() { return height; }
    public String getType() { return type; }
    public Color getColor() { return color; }
    public boolean hasWindows() { return hasWindows; }
    public int getFloors() { return floors; }
}
