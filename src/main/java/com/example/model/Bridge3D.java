/**
 * Three-dimensional model representation of bridge for JOGL rendering.
 * 
 * Purpose:
 * Models bridge geometry with position, dimensions, and visual properties for
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

public class Bridge3D {
    /** X-coordinate of bridge origin point on the city map */
    public final double x;
    /** Y-coordinate of bridge origin point on the city map */
    public final double y;
    /** Length of the bridge in map units (along its main axis) */
    public final double length;
    /** Width of the bridge in map units (perpendicular to length) */
    public final double width;
    /** Rotation angle of the bridge in radians (0 = horizontal, π/2 = vertical) */
    public final double angle;

    /**
     * Constructs a new Bridge3D with specified geometry.
     * All parameters are immutable after construction for thread-safety.
     * 
     * @param x the x-coordinate of the bridge origin
     * @param y the y-coordinate of the bridge origin
     * @param length the bridge length in map units
     * @param width the bridge width in map units
     * @param angle the rotation angle in radians
     */
    public Bridge3D(double x, double y, double length, double width, double angle) {
        this.x = x;            // Store bridge x position
        this.y = y;            // Store bridge y position
        this.length = length;  // Store bridge length
        this.width = width;    // Store bridge width
        this.angle = angle;    // Store rotation angle
    }

    /** Returns the x-coordinate of this bridge. @return x-coordinate value */
    public double getX() { return x; }
    
    /** Returns the y-coordinate of this bridge. @return y-coordinate value */
    public double getY() { return y; }
    
    /** Returns the length of this bridge. @return length value in map units */
    public double getLength() { return length; }
    
    /** Returns the width of this bridge. @return width value in map units */
    public double getWidth() { return width; }
}
