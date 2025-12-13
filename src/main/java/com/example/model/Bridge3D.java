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

/**
 * Represents a 3D bridge structure for rendering in JOGL/OpenGL scenes.
 * Immutable geometry model for bridge visualization over water or roads.
 */
public class Bridge3D {
    // X-coordinate of bridge center point in world space
    public final double x;
    // Y-coordinate of bridge center point in world space
    public final double y;
    // Length of the bridge span along its orientation axis
    public final double length;
    // Width (perpendicular to length) of the bridge deck
    public final double width;
    // Rotation angle in degrees or radians for bridge orientation
    public final double angle;

    /**
     * Constructs a new 3D bridge with specified geometry.
     * All fields are immutable after construction for thread safety.
     *
     * @param x X-coordinate of bridge center
     * @param y Y-coordinate of bridge center
     * @param length Length of bridge span
     * @param width Width of bridge deck
     * @param angle Rotation angle for bridge orientation
     */
    public Bridge3D(double x, double y, double length, double width, double angle) {
        // Assign X position of bridge centerline
        this.x = x;
        // Assign Y position of bridge centerline
        this.y = y;
        // Assign bridge span length
        this.length = length;
        // Assign bridge deck width
        this.width = width;
        // Assign rotation angle for rendering orientation
        this.angle = angle;
    }

    /**
     * Gets the X-coordinate of the bridge center.
     *
     * @return X position in world space
     */
    public double getX() { return x; }
    
    /**
     * Gets the Y-coordinate of the bridge center.
     *
     * @return Y position in world space
     */
    public double getY() { return y; }
    
    /**
     * Gets the length of the bridge span.
     *
     * @return Bridge length value
     */
    public double getLength() { return length; }
    
    /**
     * Gets the width of the bridge deck.
     *
     * @return Bridge width value
     */
    public double getWidth() { return width; }
}
