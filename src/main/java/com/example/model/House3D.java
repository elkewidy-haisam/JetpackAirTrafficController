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
    /** The x-coordinate of the house's corner position */
    public final double x;
    /** The y-coordinate of the house's corner position */
    public final double y;
    /** The width of the house along the x-axis */
    public final double width;
    /** The depth of the house along the y-axis */
    public final double depth;
    /** The height of the house along the z-axis */
    public final double height;

    /**
     * Constructs a new House3D with specified 3D dimensions.
     * All geometric properties are immutable after construction.
     * 
     * @param x corner x-coordinate position
     * @param y corner y-coordinate position
     * @param width house width along x-axis
     * @param depth house depth along y-axis
     * @param height house height along z-axis
     */
    public House3D(double x, double y, double width, double depth, double height) {
        this.x = x;            // Store house x position
        this.y = y;            // Store house y position
        this.width = width;    // Store house width
        this.depth = depth;    // Store house depth
        this.height = height;  // Store house height
    }

    /**
     * Returns the x-coordinate of the house.
     * @return x-coordinate value
     */
    public double getX() { return x; }  // Return x position
    
    /**
     * Returns the y-coordinate of the house.
     * @return y-coordinate value
     */
    public double getY() { return y; }  // Return y position
    
    /**
     * Returns the width of the house.
     * @return width value
     */
    public double getWidth() { return width; }  // Return house width
    
    /**
     * Returns the depth (length) of the house.
     * @return depth value
     */
    public double getLength() { return depth; }  // Return house depth (alias for length)
    
    /**
     * Returns the height of the house.
     * @return height value
     */
    public double getHeight() { return height; }  // Return house height
}
