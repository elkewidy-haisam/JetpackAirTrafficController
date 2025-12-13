/**
 * Three-dimensional model representation of building for JOGL rendering.
 * 
 * Purpose:
 * Models building geometry with position, dimensions, and visual properties for
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

import java.awt.Color;

/**
 * Building3D - 3D building representation for city rendering and collision.
 * Includes position, dimensions, color, type, and footprint logic.
 */
public class Building3D {
    /** X-coordinate of building origin point on city map */
    private final double x;
    /** Y-coordinate of building origin point on city map */
    private final double y;
    /** Width of building in map units (X-axis dimension) */
    private final double width;
    /** Depth of building in map units (Y-axis dimension) */
    private final double depth;
    /** Height of building in map units (Z-axis, vertical dimension) */
    private final double height;
    /** Color used for rendering this building */
    private Color color;
    /** Building type classification (skyscraper, office, residential, commercial, landmark) */
    private final String type;
    /** Whether this building has windows for visual detail */
    private final boolean hasWindows;
    /** Approximate number of floors based on height */
    private final int floors;
    
    /**
     * Constructs a new Building3D with specified geometry and type.
     * Automatically assigns appropriate color based on building type.
     * 
     * @param x x-coordinate of building origin
     * @param y y-coordinate of building origin
     * @param width building width (X dimension)
     * @param depth building depth (Y dimension)
     * @param height building height (Z dimension)
     * @param type building type classification
     */
    public Building3D(double x, double y, double width, double depth, double height, String type) {
        this.x = x;              // Store x position
        this.y = y;              // Store y position
        this.width = width;      // Store width
        this.depth = depth;      // Store depth
        this.height = height;    // Store height
        this.type = type;        // Store building type
        this.hasWindows = true;  // Enable windows for all buildings
        this.floors = (int)(height / 10);  // Calculate floors: roughly 1 floor per 10 height units
        assignColorByType();     // Assign color based on type
    }
    
    /**
     * Assigns appropriate color based on building type.
     * Different building types get distinct colors for visual identification.
     */
    private void assignColorByType() {
        switch (type) {  // Select color based on type
            case "skyscraper":  // Tall commercial buildings
                color = new Color(100, 120, 140);  // Dark blue-grey for modern skyscrapers
                break;
            case "office":  // Office buildings
                color = new Color(140, 140, 140);  // Neutral grey for offices
                break;
            case "residential":  // Residential buildings
                color = new Color(180, 160, 140);  // Warm tan/beige for homes
                break;
            case "commercial":  // Commercial/retail buildings
                color = new Color(160, 140, 120);  // Light brown for shops
                break;
            case "landmark":  // Notable landmarks
                color = new Color(150, 130, 110);  // Distinctive color for special buildings
                break;
            default:  // Unknown building type
                color = new Color(130, 130, 130);  // Default grey
        }
    }
    
    /**
     * Checks if a point is inside this building's footprint.
     * Tests if point falls within building's rectangular boundary.
     * 
     * @param px x-coordinate of point to test
     * @param py y-coordinate of point to test
     * @return true if point is inside footprint, false otherwise
     */
    public boolean containsPoint(double px, double py) {
        // Check if point is within x boundaries AND within y boundaries
        return px >= x && px <= x + width &&  // Point x is between building left and right edges
               py >= y && py <= y + depth;     // Point y is between building front and back edges
    }
    
    /**
     * Calculates distance from a point to this building.
     * Returns 0 if point is inside building, otherwise distance to nearest edge.
     * 
     * @param px x-coordinate of point
     * @param py y-coordinate of point
     * @return distance to building in map units
     */
    public double distanceTo(double px, double py) {
        // Distance to closest point on building rectangle
        // For x: if point left of building use (x-px), if right use (px-(x+width)), else 0
        double dx = Math.max(x - px, Math.max(0, px - (x + width)));
        // For y: if point above building use (y-py), if below use (py-(y+depth)), else 0
        double dy = Math.max(y - py, Math.max(0, py - (y + depth)));
        return Math.sqrt(dx * dx + dy * dy);  // Euclidean distance
    }
    
    // Getters for building properties
    /** Returns x-coordinate. @return x position */
    public double getX() { return x; }
    /** Returns y-coordinate. @return y position */
    public double getY() { return y; }
    /** Returns width. @return width value */
    public double getWidth() { return width; }
    /** Returns depth (also called length). @return depth value */
    public double getLength() { return depth; }
    /** Returns height. @return height value */
    public double getHeight() { return height; }
    /** Returns building type. @return type string */
    public String getType() { return type; }
    /** Returns building color. @return Color object */
    public Color getColor() { return color; }
    /** Returns whether building has windows. @return true if has windows */
    public boolean hasWindows() { return hasWindows; }
    /** Returns number of floors. @return floor count */
    public int getFloors() { return floors; }
}
