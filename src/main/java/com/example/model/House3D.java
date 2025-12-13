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
    // X-coordinate of house position, Y-coordinate, width, depth, and height dimensions
    public final double x, y, width, depth, height;

    /**
     * Constructs a new 3D house model with specified geometry.
     * 
     * @param x X-coordinate of house position in world space
     * @param y Y-coordinate of house position in world space
     * @param width Width of house (lateral dimension, X-axis)
     * @param depth Depth of house (longitudinal dimension, Y-axis)
     * @param height Height of house (vertical dimension, Z-axis)
     */
    public House3D(double x, double y, double width, double depth, double height) {
        // Store X-coordinate of house position
        this.x = x;
        // Store Y-coordinate of house position
        this.y = y;
        // Store width dimension for rendering house size
        this.width = width;
        // Store depth dimension for rendering house size
        this.depth = depth;
        // Store height dimension for rendering house vertical extent
        this.height = height;
    }

    // Returns X-coordinate of house position
    public double getX() { return x; }
    // Returns Y-coordinate of house position
    public double getY() { return y; }
    // Returns width dimension of house
    public double getWidth() { return width; }
    // Returns depth dimension of house (alias for compatibility)
    public double getLength() { return depth; }
    // Returns height dimension of house
    public double getHeight() { return height; }
}
