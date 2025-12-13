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
    // X-coordinate of bridge center point in world space
    public final double x, y, length, width, angle;

    /**
     * Constructs a new 3D bridge model with specified geometry.
     * 
     * @param x X-coordinate of bridge center in world space
     * @param y Y-coordinate of bridge center in world space
     * @param length Length of bridge span (longitudinal dimension)
     * @param width Width of bridge deck (lateral dimension)
     * @param angle Rotation angle in degrees relative to positive X-axis
     */
    public Bridge3D(double x, double y, double length, double width, double angle) {
        // Store X-coordinate of bridge center position
        this.x = x;
        // Store Y-coordinate of bridge center position
        this.y = y;
        // Store length of bridge span for rendering dimensions
        this.length = length;
        // Store width of bridge deck for rendering dimensions
        this.width = width;
        // Store rotation angle for proper orientation rendering
        this.angle = angle;
    }

    // Returns X-coordinate of bridge center position
    public double getX() { return x; }
    // Returns Y-coordinate of bridge center position
    public double getY() { return y; }
    // Returns length of bridge span
    public double getLength() { return length; }
    // Returns width of bridge deck
    public double getWidth() { return width; }
}
