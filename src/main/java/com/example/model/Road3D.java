/**
 * Three-dimensional model representation of road for JOGL rendering.
 * 
 * Purpose:
 * Models road geometry with position, dimensions, and visual properties for
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

public class Road3D {
    // X-coordinate of road center point, Y-coordinate, length, width, and rotation angle
    public final double x, y, length, width, angle;

    /**
     * Constructs a new 3D road model with specified geometry.
     * 
     * @param x X-coordinate of road center in world space
     * @param y Y-coordinate of road center in world space
     * @param length Length of road segment (longitudinal dimension)
     * @param width Width of road surface (lateral dimension)
     * @param angle Rotation angle in degrees relative to positive X-axis
     */
    public Road3D(double x, double y, double length, double width, double angle) {
        // Store X-coordinate of road center position
        this.x = x;
        // Store Y-coordinate of road center position
        this.y = y;
        // Store length of road segment for rendering dimensions
        this.length = length;
        // Store width of road surface for rendering dimensions
        this.width = width;
        // Store rotation angle for proper orientation rendering
        this.angle = angle;
    }

    // Returns X-coordinate of road center position
    public double getX() { return x; }
    // Returns Y-coordinate of road center position
    public double getY() { return y; }
    // Returns length of road segment
    public double getLength() { return length; }
    // Returns width of road surface
    public double getWidth() { return width; }
}
