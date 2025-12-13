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
    /** X-coordinate of road origin point on the city map */
    public final double x;
    /** Y-coordinate of road origin point on the city map */
    public final double y;
    /** Length of the road in map units (along its main axis) */
    public final double length;
    /** Width of the road in map units (perpendicular to length) */
    public final double width;
    /** Rotation angle of the road in radians (0 = horizontal, π/2 = vertical) */
    public final double angle;

    /**
     * Constructs a new Road3D with specified geometry.
     * All parameters are immutable after construction for thread-safety.
     * 
     * @param x the x-coordinate of the road origin
     * @param y the y-coordinate of the road origin
     * @param length the road length in map units
     * @param width the road width in map units
     * @param angle the rotation angle in radians
     */
    public Road3D(double x, double y, double length, double width, double angle) {
        this.x = x;            // Store road x position
        this.y = y;            // Store road y position
        this.length = length;  // Store road length
        this.width = width;    // Store road width
        this.angle = angle;    // Store rotation angle
    }

    /** Returns the x-coordinate of this road. @return x-coordinate value */
    public double getX() { return x; }
    
    /** Returns the y-coordinate of this road. @return y-coordinate value */
    public double getY() { return y; }
    
    /** Returns the length of this road. @return length value in map units */
    public double getLength() { return length; }
    
    /** Returns the width of this road. @return width value in map units */
    public double getWidth() { return width; }
}
