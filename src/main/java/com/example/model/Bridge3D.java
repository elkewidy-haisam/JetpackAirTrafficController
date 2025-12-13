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
    /** The x-coordinate of the bridge's center position */
    public final double x;
    /** The y-coordinate of the bridge's center position */
    public final double y;
    /** The length of the bridge along its primary axis */
    public final double length;
    /** The width of the bridge perpendicular to its length */
    public final double width;
    /** The rotation angle of the bridge in degrees */
    public final double angle;

    /**
     * Constructs a new Bridge3D with specified geometry.
     * All geometric properties are immutable after construction.
     * 
     * @param x center x-coordinate position
     * @param y center y-coordinate position
     * @param length bridge length along primary axis
     * @param width bridge width perpendicular to length
     * @param angle rotation angle in degrees
     */
    public Bridge3D(double x, double y, double length, double width, double angle) {
        this.x = x;            // Store bridge x position
        this.y = y;            // Store bridge y position
        this.length = length;  // Store bridge length
        this.width = width;    // Store bridge width
        this.angle = angle;    // Store rotation angle
    }

    /**
     * Returns the x-coordinate of the bridge.
     * @return x-coordinate value
     */
    public double getX() { return x; }  // Return x position
    
    /**
     * Returns the y-coordinate of the bridge.
     * @return y-coordinate value
     */
    public double getY() { return y; }  // Return y position
    
    /**
     * Returns the length of the bridge.
     * @return length value
     */
    public double getLength() { return length; }  // Return bridge length
    
    /**
     * Returns the width of the bridge.
     * @return width value
     */
    public double getWidth() { return width; }  // Return bridge width
}
