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

/**
 * Represents a 3D road segment for rendering in JOGL/OpenGL scenes.
 * Immutable geometry model for street visualization.
 */
public class Road3D {
    // X-coordinate of road center point in world space
    public final double x;
    // Y-coordinate of road center point in world space
    public final double y;
    // Length of the road segment along its orientation axis
    public final double length;
    // Width (perpendicular to length) of the road surface
    public final double width;
    // Rotation angle in degrees or radians for road orientation
    public final double angle;

    /**
     * Constructs a new 3D road segment with specified geometry.
     * All fields are immutable after construction for thread safety.
     *
     * @param x X-coordinate of road center
     * @param y Y-coordinate of road center
     * @param length Length of road segment
     * @param width Width of road surface
     * @param angle Rotation angle for road orientation
     */
    public Road3D(double x, double y, double length, double width, double angle) {
        // Assign X position of road centerline
        this.x = x;
        // Assign Y position of road centerline
        this.y = y;
        // Assign road segment length
        this.length = length;
        // Assign road surface width
        this.width = width;
        // Assign rotation angle for rendering orientation
        this.angle = angle;
    }

    /**
     * Gets the X-coordinate of the road center.
     *
     * @return X position in world space
     */
    public double getX() { return x; }
    
    /**
     * Gets the Y-coordinate of the road center.
     *
     * @return Y position in world space
     */
    public double getY() { return y; }
    
    /**
     * Gets the length of the road segment.
     *
     * @return Road length value
     */
    public double getLength() { return length; }
    
    /**
     * Gets the width of the road surface.
     *
     * @return Road width value
     */
    public double getWidth() { return width; }
}
