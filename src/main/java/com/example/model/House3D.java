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
    /** X-coordinate of house origin point on the city map */
    public final double x;
    /** Y-coordinate of house origin point on the city map */
    public final double y;
    /** Width of the house in map units (X-axis dimension) */
    public final double width;
    /** Depth of the house in map units (Y-axis dimension, also called length) */
    public final double depth;
    /** Height of the house in map units (Z-axis dimension, vertical) */
    public final double height;

    /**
     * Constructs a new House3D with specified geometry.
     * All parameters are immutable after construction for thread-safety.
     * 
     * @param x the x-coordinate of the house origin
     * @param y the y-coordinate of the house origin
     * @param width the house width in map units (X dimension)
     * @param depth the house depth in map units (Y dimension)
     * @param height the house height in map units (Z dimension)
     */
    public House3D(double x, double y, double width, double depth, double height) {
        this.x = x;            // Store house x position
        this.y = y;            // Store house y position
        this.width = width;    // Store house width (X dimension)
        this.depth = depth;    // Store house depth (Y dimension)
        this.height = height;  // Store house height (Z dimension)
    }

    /** Returns the x-coordinate of this house. @return x-coordinate value */
    public double getX() { return x; }
    
    /** Returns the y-coordinate of this house. @return y-coordinate value */
    public double getY() { return y; }
    
    /** Returns the width of this house. @return width value in map units */
    public double getWidth() { return width; }
    
    /** Returns the length (depth) of this house. @return depth value in map units */
    public double getLength() { return depth; }
    
    /** Returns the height of this house. @return height value in map units */
    public double getHeight() { return height; }
}
