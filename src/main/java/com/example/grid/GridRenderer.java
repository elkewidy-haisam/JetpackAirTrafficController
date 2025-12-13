/**
 * Rendering component for grid visualization.
 * 
 * Purpose:
 * Handles rendering logic for grid using appropriate
 * graphics APIs. Translates model data into visual representations with proper scaling,
 * colors, and transformations.
 * 
 * Key Responsibilities:
 * - Render grid data to graphics context
 * - Apply visual styling, colors, and effects
 * - Handle coordinate transformations and scaling
 * - Optimize rendering performance for smooth updates
 * 
 * Interactions:
 * - Called by panel paintComponent methods
 * - Consumes model data for visualization
 * - Uses graphics APIs (Graphics2D, JOGL) for drawing
 * 
 * Patterns & Constraints:
 * - Stateless rendering methods for thread safety
 * - Efficient drawing algorithms for real-time updates
 * - Handles null/invalid input gracefully
 * 
 * @author Haisam Elkewidy
 */

package com.example.grid;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * GridRenderer is a utility class for rendering grid overlays on map panels.
 * It supports toggling visibility, customizing grid color, and spacing.
 */
public class GridRenderer {

    private Grid grid;
    private boolean visible;
    private Color gridColor;
    private Color labelColor;
    private int gridSpacing;

    /**
     * Constructs a GridRenderer for the given grid.
     * Initializes with default colors, spacing, and visibility off.
     *
     * @param grid The grid to render
     */
    public GridRenderer(Grid grid) {
        // Store reference to grid model
        this.grid = grid;
        // Start with grid overlay hidden
        this.visible = false;
        // Set semi-transparent blue for grid lines (RGBA: 100, 100, 255, 80)
        this.gridColor = new Color(100, 100, 255, 80);
        // Set darker blue for coordinate labels (RGBA: 50, 50, 200, 150)
        this.labelColor = new Color(50, 50, 200, 150);
        // Set default spacing of 100 pixels between grid lines
        this.gridSpacing = 100;
    }

    /**
     * Sets the visibility of the grid overlay.
     * @param visible true to show, false to hide
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return true if the grid overlay is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Toggles the visibility of the grid overlay.
     */
    public void toggleVisibility() {
        this.visible = !this.visible;
    }

    /**
     * Sets the spacing between grid lines.
     * Clamps value to range [50, 200] pixels.
     *
     * @param spacing The spacing in pixels
     */
    public void setGridSpacing(int spacing) {
        // Clamp spacing to minimum 50 and maximum 200 pixels
        this.gridSpacing = Math.max(50, Math.min(200, spacing));
    }

    /**
     * Sets the color of the grid lines.
     * @param color The grid color
     */
    public void setGridColor(Color color) {
        this.gridColor = color;
    }

    /**
     * Renders the grid overlay on the map.
     * Draws vertical/horizontal lines, coordinate labels, and origin marker.
     *
     * @param g2d Graphics2D context for drawing
     * @param mapWidth Width of the map in pixels
     * @param mapHeight Height of the map in pixels
     */
    public void render(Graphics2D g2d, int mapWidth, int mapHeight) {
        // Skip rendering if grid is not visible
        if (!visible) return;

        // Set grid line color (semi-transparent blue)
        g2d.setColor(gridColor);
        // Use thin 1-pixel stroke for grid lines
        g2d.setStroke(new BasicStroke(1f));

        // Draw vertical grid lines from left to right
        for (int x = gridSpacing; x < mapWidth; x += gridSpacing) {
            // Draw line from top (y=0) to bottom (y=mapHeight)
            g2d.drawLine(x, 0, x, mapHeight);
        }

        // Draw horizontal grid lines from top to bottom
        for (int y = gridSpacing; y < mapHeight; y += gridSpacing) {
            // Draw line from left (x=0) to right (x=mapWidth)
            g2d.drawLine(0, y, mapWidth, y);
        }

        // Switch to label color for coordinate markers
        g2d.setColor(labelColor);
        // Use monospaced font for aligned coordinate labels
        g2d.setFont(new Font("Monospaced", Font.BOLD, 10));

        // Draw X-axis coordinate labels along top edge
        int labelX = 0;
        for (int x = gridSpacing; x < mapWidth; x += gridSpacing) {
            // Draw incrementing X coordinate label (0, 1, 2, ...)
            g2d.drawString(String.valueOf(labelX++), x + 3, 12);
        }

        // Draw Y-axis coordinate labels along left edge
        int labelY = 0;
        for (int y = gridSpacing; y < mapHeight; y += gridSpacing) {
            // Draw incrementing Y coordinate label (0, 1, 2, ...)
            g2d.drawString(String.valueOf(labelY++), 3, y + 12);
        }

        // Draw red origin marker at (0,0)
        g2d.setColor(Color.RED);
        // Fill 6x6 pixel circle centered at origin
        g2d.fillOval(-3, -3, 6, 6);
        // Label origin with coordinate text
        g2d.drawString("(0,0)", 5, 20);
    }

    /**
     * Converts pixel position to grid coordinates.
     * Divides pixel values by grid spacing to get grid cell indices.
     *
     * @param pixelX X position in pixels
     * @param pixelY Y position in pixels
     * @return Formatted string showing grid coordinates
     */
    public String getGridCoordinates(int pixelX, int pixelY) {
        // Calculate grid X coordinate by dividing pixel X by spacing
        int gridX = pixelX / gridSpacing;
        // Calculate grid Y coordinate by dividing pixel Y by spacing
        int gridY = pixelY / gridSpacing;
        // Format and return as "Grid: (X, Y)"
        return String.format("Grid: (%d, %d)", gridX, gridY);
    }
}
