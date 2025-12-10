package com.example.grid;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * GridRenderer.java
 * Utility class for rendering grid overlays on maps
 */
public class GridRenderer {
    private Grid grid;
    private boolean visible;
    private Color gridColor;
    private Color labelColor;
    private int gridSpacing;
    
    public GridRenderer(Grid grid) {
        this.grid = grid;
        this.visible = false;
        this.gridColor = new Color(100, 100, 255, 80); // Semi-transparent blue
        this.labelColor = new Color(50, 50, 200, 150);
        this.gridSpacing = 100; // pixels between grid lines
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void toggleVisibility() {
        this.visible = !this.visible;
    }
    
    public void setGridSpacing(int spacing) {
        this.gridSpacing = Math.max(50, Math.min(200, spacing));
    }
    
    public void setGridColor(Color color) {
        this.gridColor = color;
    }
    
    /**
     * Renders the grid overlay on the map
     */
    public void render(Graphics2D g2d, int mapWidth, int mapHeight) {
        if (!visible) return;
        
        g2d.setColor(gridColor);
        g2d.setStroke(new BasicStroke(1f));
        
        // Draw vertical lines
        for (int x = gridSpacing; x < mapWidth; x += gridSpacing) {
            g2d.drawLine(x, 0, x, mapHeight);
        }
        
        // Draw horizontal lines
        for (int y = gridSpacing; y < mapHeight; y += gridSpacing) {
            g2d.drawLine(0, y, mapWidth, y);
        }
        
        // Draw grid labels
        g2d.setColor(labelColor);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 10));
        
        int labelX = 0;
        for (int x = gridSpacing; x < mapWidth; x += gridSpacing) {
            g2d.drawString(String.valueOf(labelX++), x + 3, 12);
        }
        
        int labelY = 0;
        for (int y = gridSpacing; y < mapHeight; y += gridSpacing) {
            g2d.drawString(String.valueOf(labelY++), 3, y + 12);
        }
        
        // Draw origin marker
        g2d.setColor(Color.RED);
        g2d.fillOval(-3, -3, 6, 6);
        g2d.drawString("(0,0)", 5, 20);
    }
    
    /**
     * Get grid coordinates for a pixel position
     */
    public String getGridCoordinates(int pixelX, int pixelY) {
        int gridX = pixelX / gridSpacing;
        int gridY = pixelY / gridSpacing;
        return String.format("Grid: (%d, %d)", gridX, gridY);
    }
}
