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
    private final double x;          // X position on map
    private final double y;          // Y position on map
    private final double width;      // Building width
    private final double depth;      // Building depth
    private final double height;     // Building height (altitude)
    private Color color;             // Building color
    private final String type;       // Building type (skyscraper, office, residential, etc.)
    private final boolean hasWindows;
    private final int floors;
    
    /**
     * Constructs a new 3D building with specified geometry and type.
     * Automatically assigns color based on building type and estimates floor count.
     *
     * @param x X-coordinate of building base
     * @param y Y-coordinate of building base
     * @param width Width of building footprint
     * @param depth Depth of building footprint
     * @param height Height of building structure
     * @param type Building type classification
     */
    public Building3D(double x, double y, double width, double depth, double height, String type) {
        // Set building X position
        this.x = x;
        // Set building Y position
        this.y = y;
        // Set building width
        this.width = width;
        // Set building depth
        this.depth = depth;
        // Set building height
        this.height = height;
        // Set building type classification
        this.type = type;
        // Enable window rendering by default
        this.hasWindows = true;
        // Estimate floor count (assuming 10 units per floor)
        this.floors = (int)(height / 10);
        // Assign appropriate color based on building type
        assignColorByType();
    }
    
    /**
     * Assigns building color based on type classification.
     * Uses predefined color scheme for different building categories.
     */
    private void assignColorByType() {
        // Select color based on building type
        switch (type) {
            case "skyscraper":
                // Dark blue-grey for tall buildings
                color = new Color(100, 120, 140);
                break;
            case "office":
                // Medium grey for office buildings
                color = new Color(140, 140, 140);
                break;
            case "residential":
                // Tan/beige for residential buildings
                color = new Color(180, 160, 140);
                break;
            case "commercial":
                // Light brown for commercial buildings
                color = new Color(160, 140, 120);
                break;
            case "landmark":
                // Distinctive tan for landmark buildings
                color = new Color(150, 130, 110);
                break;
            default:
                // Default grey for unspecified building types
                color = new Color(130, 130, 130);
        }
    }
    
    /**
     * Checks if a point is inside this building's footprint.
     * Tests if point falls within building's 2D bounding rectangle.
     *
     * @param px X-coordinate of point to test
     * @param py Y-coordinate of point to test
     * @return true if point is inside building footprint
     */
    public boolean containsPoint(double px, double py) {
        // Check if point X is within building X bounds AND point Y is within building Y bounds
        return px >= x && px <= x + width &&
               py >= y && py <= y + depth;
    }
    
    /**
     * Calculates distance from a point to this building.
     * Computes shortest distance to building's bounding rectangle.
     *
     * @param px X-coordinate of point
     * @param py Y-coordinate of point
     * @return Distance in map units from point to nearest building edge
     */
    public double distanceTo(double px, double py) {
        // Calculate horizontal distance to closest point on building rectangle
        // (0 if point is between x and x+width, otherwise distance to nearest edge)
        double dx = Math.max(x - px, Math.max(0, px - (x + width)));
        // Calculate vertical distance to closest point on building rectangle
        // (0 if point is between y and y+depth, otherwise distance to nearest edge)
        double dy = Math.max(y - py, Math.max(0, py - (y + depth)));
        // Return Euclidean distance using Pythagorean theorem
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    // Getters and setters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getLength() { return depth; }
    public double getHeight() { return height; }
    public String getType() { return type; }
    public Color getColor() { return color; }
    public boolean hasWindows() { return hasWindows; }
    public int getFloors() { return floors; }
}
