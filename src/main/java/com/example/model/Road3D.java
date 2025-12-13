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
    /** The x-coordinate of the road's center position */
    public final double x;
    /** The y-coordinate of the road's center position */
    public final double y;
    /** The length of the road along its primary axis */
    public final double length;
    /** The width of the road perpendicular to its length */
    public final double width;
    /** The rotation angle of the road in degrees */
    public final double angle;

    /**
     * Constructs a new Road3D with specified geometry.
     * All geometric properties are immutable after construction.
     * 
     * @param x center x-coordinate position
     * @param y center y-coordinate position
     * @param length road length along primary axis
     * @param width road width perpendicular to length
     * @param angle rotation angle in degrees
     */
    public Road3D(double x, double y, double length, double width, double angle) {
        this.x = x;            // Store road x position
        this.y = y;            // Store road y position
        this.length = length;  // Store road length
        this.width = width;    // Store road width
        this.angle = angle;    // Store rotation angle
    }

    /**
     * Returns the x-coordinate of the road.
     * @return x-coordinate value
     */
    public double getX() { return x; }  // Return x position
    
    /**
     * Returns the y-coordinate of the road.
     * @return y-coordinate value
     */
    public double getY() { return y; }  // Return y position
    
    /**
     * Returns the length of the road.
     * @return length value
     */
    public double getLength() { return length; }  // Return road length
    
    /**
     * Returns the width of the road.
     * @return width value
     */
    public double getWidth() { return width; }  // Return road width
}
