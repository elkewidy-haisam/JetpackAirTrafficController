/**
 * Rendering component for citymap visualization.
 * 
 * Purpose:
 * Handles rendering logic for citymap using appropriate
 * graphics APIs. Translates model data into visual representations with proper scaling,
 * colors, and transformations.
 * 
 * Key Responsibilities:
 * - Render citymap data to graphics context
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

package com.example.ui.citymap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.example.flight.JetPackFlight;
import com.example.grid.GridRenderer;
import com.example.parking.ParkingSpace;
import com.example.utility.performance.PerformanceMonitor;
import com.example.utility.timezone.TimezoneHelper;

/**
 * CityMapRenderer - Handles all rendering logic for the city map panel
 */
public class CityMapRenderer {
    private final String city;
    private final ImageIcon mapIcon;
    private final GridRenderer gridRenderer;
    private final PerformanceMonitor performanceMonitor;
    
    public CityMapRenderer(String city, ImageIcon mapIcon, GridRenderer gridRenderer, 
                          PerformanceMonitor performanceMonitor) {
        this.city = city;
        this.mapIcon = mapIcon;
        this.gridRenderer = gridRenderer;
        this.performanceMonitor = performanceMonitor;
    }
    
    /**
     * Main rendering method - draws the entire map with all components
     */
    public void paintMapComponent(Graphics g, JComponent component, List<JetPackFlight> jetpackFlights, 
                                  List<ParkingSpace> parkingSpaces) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw base map
        g2d.drawImage(mapIcon.getImage(), 0, 0, component);
        
        int mapWidth = mapIcon.getIconWidth();
        int mapHeight = mapIcon.getIconHeight();
        
        // Apply time-based shading
        applyTimeBasedShading(g2d, mapWidth, mapHeight);
        
        // Draw grid overlay if enabled
        if (gridRenderer != null) {
            gridRenderer.render(g2d, mapWidth, mapHeight);
        }
        
        // Draw parking spaces
        drawParkingSpaces(g2d, parkingSpaces);
        
            // Only render visible jetpacks
            for (JetPackFlight flight : jetpackFlights) {
                flight.draw(g2d);
        }
        
        // Draw performance monitor if enabled
        if (performanceMonitor != null) {
            performanceMonitor.render(g2d, 10, mapHeight - 90);
        }
    }
    
    /**
     * Applies time-based shading to the map based on current time
     */
    private void applyTimeBasedShading(Graphics2D g2d, int width, int height) {
        ZoneId timezone = TimezoneHelper.getTimezoneForCity(city);
        LocalDateTime now = LocalDateTime.now(timezone);
        int hour = now.getHour();
        
        Color shadingColor;
        int alpha;
        
        if (hour >= 6 && hour < 7) {
            // Dawn
            shadingColor = new Color(255, 140, 0);
            alpha = 30;
        } else if (hour >= 7 && hour < 17) {
            // Daytime
            shadingColor = new Color(255, 255, 200);
            alpha = 10;
        } else if (hour >= 17 && hour < 18) {
            // Dusk
            shadingColor = new Color(255, 69, 0);
            alpha = 40;
        } else if (hour >= 18 && hour < 20) {
            // Evening
            shadingColor = new Color(25, 25, 112);
            alpha = 60;
        } else if (hour >= 20 || hour < 6) {
            // Night
            shadingColor = new Color(0, 0, 50);
            alpha = 80;
        } else {
            shadingColor = new Color(255, 255, 255);
            alpha = 0;
        }
        
        if (alpha > 0) {
            g2d.setColor(new Color(shadingColor.getRed(), shadingColor.getGreen(), 
                                  shadingColor.getBlue(), alpha));
            g2d.fillRect(0, 0, width, height);
        }
    }
    
    /**
     * Draws all parking spaces on the map
     */
    private void drawParkingSpaces(Graphics2D g2d, List<ParkingSpace> parkingSpaces) {
        for (ParkingSpace ps : parkingSpaces) {
            if (ps.isOccupied()) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.GREEN);
            }
            
            g2d.fillOval((int)ps.getX() - 10, (int)ps.getY() - 10, 20, 20);
            
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval((int)ps.getX() - 10, (int)ps.getY() - 10, 20, 20);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2d.getFontMetrics();
            String label = "P";
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, (int)ps.getX() - labelWidth/2, (int)ps.getY() + 5);
        }
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(mapIcon.getIconWidth(), mapIcon.getIconHeight());
    }
}
