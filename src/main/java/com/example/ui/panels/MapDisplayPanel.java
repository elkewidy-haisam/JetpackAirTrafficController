/**
 * MapDisplayPanel.java
 * by Haisam Elkewidy
 *
 * This class handles MapDisplayPanel functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - mapIcon (ImageIcon)
 *   - parkingSpaces (List<RealisticCityMap.ParkingSpace>)
 *   - parkingRenderer (ParkingSpaceRenderer)
 *   - shadingRenderer (ShadingRenderer)
 *
 * Methods:
 *   - MapDisplayPanel(mapIcon, parkingSpaces)
 *
 */

package com.example.ui.panels;

import javax.swing.*;

import com.example.map.RealisticCityMap;

import java.awt.*;
import java.util.List;

/**
 * MapDisplayPanel.java
 * by Haisam Elkewidy
 * 
 * Custom panel for rendering the city map with jetpacks and parking spaces.
 */
public class MapDisplayPanel extends JPanel {
    private ImageIcon mapIcon;
    private List<RealisticCityMap.ParkingSpace> parkingSpaces;
    
    /**
     * Functional interface for rendering parking spaces
     */
    public interface ParkingSpaceRenderer {
        void render(Graphics2D g2d);
    }
    
    /**
     * Functional interface for applying time-based shading
     */
    public interface ShadingRenderer {
        void render(Graphics2D g2d, int width, int height);
    }
    
    private ParkingSpaceRenderer parkingRenderer;
    private ShadingRenderer shadingRenderer;
    
    public MapDisplayPanel(ImageIcon mapIcon, 
                          List<RealisticCityMap.ParkingSpace> parkingSpaces) {
        this.mapIcon = mapIcon;
        this.parkingSpaces = parkingSpaces;
        setBackground(Color.WHITE);
    }
    
    /**
     * Sets the parking space renderer
     */
    public void setParkingRenderer(ParkingSpaceRenderer renderer) {
        this.parkingRenderer = renderer;
    }
    
    /**
     * Sets the shading renderer
     */
    public void setShadingRenderer(ShadingRenderer renderer) {
        this.shadingRenderer = renderer;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the map image
        if (mapIcon != null) {
            g2d.drawImage(mapIcon.getImage(), 0, 0, this);
        }
        
        // Apply time-based shading overlay
        if (shadingRenderer != null) {
            shadingRenderer.render(g2d, getWidth(), getHeight());
        }
        
        // Draw parking spaces
        if (parkingRenderer != null) {
            parkingRenderer.render(g2d);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        if (mapIcon != null) {
            return new Dimension(mapIcon.getIconWidth(), mapIcon.getIconHeight());
        }
        return super.getPreferredSize();
    }
    
    /**
     * Gets the parking spaces list
     */
    public List<RealisticCityMap.ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }
}
