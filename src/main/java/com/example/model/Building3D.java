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
    // X-coordinate of building's southwest corner in city grid
    private final double x;
    // Y-coordinate of building's southwest corner in city grid
    private final double y;
    // Building width (X-axis extent) in grid units
    private final double width;
    // Building depth (Y-axis extent) in grid units
    private final double depth;
    // Building height (Z-axis/altitude) in grid units
    private final double height;
    // Visual color for rendering building exterior
    private Color color;
    // Building type classification (skyscraper, office, residential, commercial, landmark)
    private final String type;
    // Whether building has windows for visual detail
    private final boolean hasWindows;
    // Approximate number of floors based on height
    private final int floors;
    
    /**
     * Constructs a new Building3D with specified geometry and type.
     * Color is automatically assigned based on building type.
     * 
     * @param x X-coordinate of southwest corner
     * @param y Y-coordinate of southwest corner
     * @param width Building width (X-axis extent)
     * @param depth Building depth (Y-axis extent)
     * @param height Building height (Z-axis extent)
     * @param type Building type classification
     */
    public Building3D(double x, double y, double width, double depth, double height, String type) {
        // Store X-coordinate position
        this.x = x;
        // Store Y-coordinate position
        this.y = y;
        // Store building width
        this.width = width;
        // Store building depth
        this.depth = depth;
        // Store building height
        this.height = height;
        // Store building type for color assignment
        this.type = type;
        // Enable windows for visual detail
        this.hasWindows = true;
        // Calculate approximate floor count (10 units per floor)
        this.floors = (int)(height / 10);
        // Assign color based on building type
        assignColorByType();
    }
    
    /**
     * Assigns building color based on its type classification.
     * Different building types receive characteristic colors.
     */
    private void assignColorByType() {
        // Switch on building type to assign appropriate color
        switch (type) {
            // Skyscraper: dark blue-grey (100,120,140)
            case "skyscraper":
                color = new Color(100, 120, 140);
                break;
            // Office: neutral grey (140,140,140)
            case "office":
                color = new Color(140, 140, 140);
                break;
            // Residential: tan/beige (180,160,140)
            case "residential":
                color = new Color(180, 160, 140);
                break;
            // Commercial: light brown (160,140,120)
            case "commercial":
                color = new Color(160, 140, 120);
                break;
            // Landmark: distinctive brown (150,130,110)
            case "landmark":
                color = new Color(150, 130, 110);
                break;
            // Default: medium grey (130,130,130)
            default:
                color = new Color(130, 130, 130);
        }
    }
    
    /**
     * Checks if a point is inside this building's footprint.
     * 
     * @param px X-coordinate of point to check
     * @param py Y-coordinate of point to check
     * @return true if point is inside building footprint, false otherwise
     */
    public boolean containsPoint(double px, double py) {
        // Check if point is within building's rectangular bounds
        return px >= x && px <= x + width &&
               py >= y && py <= y + depth;
    }
    
    /**
     * Calculates distance from a point to this building.
     * Returns distance to closest point on building rectangle.
     * 
     * @param px X-coordinate of point
     * @param py Y-coordinate of point
     * @return Distance in grid units
     */
    public double distanceTo(double px, double py) {
        // Calculate horizontal distance to closest edge (0 if inside)
        double dx = Math.max(x - px, Math.max(0, px - (x + width)));
        // Calculate vertical distance to closest edge (0 if inside)
        double dy = Math.max(y - py, Math.max(0, py - (y + depth)));
        // Return Euclidean distance to closest point on rectangle
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    // Accessor methods for building properties
    
    // Returns X-coordinate of southwest corner
    public double getX() { return x; }
    // Returns Y-coordinate of southwest corner
    public double getY() { return y; }
    // Returns building width
    public double getWidth() { return width; }
    // Returns building depth (length in Y direction)
    public double getLength() { return depth; }
    // Returns building height
    public double getHeight() { return height; }
    // Returns building type classification
    public String getType() { return type; }
    // Returns building visual color
    public Color getColor() { return color; }
    // Returns whether building has windows
    public boolean hasWindows() { return hasWindows; }
    // Returns approximate floor count
    public int getFloors() { return floors; }
}
