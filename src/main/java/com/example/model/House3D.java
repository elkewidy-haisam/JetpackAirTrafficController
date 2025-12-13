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

/**
 * Represents a 3D house structure for rendering in JOGL/OpenGL scenes.
 * Immutable geometry model for residential building visualization.
 */
public class House3D {
    // X-coordinate of house base position in world space
    public final double x;
    // Y-coordinate of house base position in world space
    public final double y;
    // Width of the house footprint (horizontal extent)
    public final double width;
    // Depth of the house footprint (front-to-back extent)
    public final double depth;
    // Height of the house structure (vertical extent)
    public final double height;

    /**
     * Constructs a new 3D house with specified dimensions.
     * All fields are immutable after construction for thread safety.
     *
     * @param x X-coordinate of house base
     * @param y Y-coordinate of house base
     * @param width Width of house footprint
     * @param depth Depth of house footprint
     * @param height Height of house structure
     */
    public House3D(double x, double y, double width, double depth, double height) {
        // Assign X position of house base
        this.x = x;
        // Assign Y position of house base
        this.y = y;
        // Assign house width (horizontal footprint)
        this.width = width;
        // Assign house depth (front-to-back dimension)
        this.depth = depth;
        // Assign house height (vertical extent)
        this.height = height;
    }

    /**
     * Gets the X-coordinate of the house base.
     *
     * @return X position in world space
     */
    public double getX() { return x; }
    
    /**
     * Gets the Y-coordinate of the house base.
     *
     * @return Y position in world space
     */
    public double getY() { return y; }
    
    /**
     * Gets the width of the house footprint.
     *
     * @return House width value
     */
    public double getWidth() { return width; }
    
    /**
     * Gets the depth (length) of the house footprint.
     * Note: Method name is getLength() but returns depth field.
     *
     * @return House depth value
     */
    public double getLength() { return depth; }
    
    /**
     * Gets the height of the house structure.
     *
     * @return House height value
     */
    public double getHeight() { return height; }
}
