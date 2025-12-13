/**
 * UI panel component for display display and interaction.
 * 
 * Purpose:
 * Specialized Swing JPanel providing display-related visualization and user interaction.
 * Integrates with the main application frame to present display data in a clear,
 * actionable format for air traffic controllers.
 * 
 * Key Responsibilities:
 * - Render display information with appropriate visual styling
 * - Handle user interactions related to display operations
 * - Update display in response to system state changes
 * - Provide callbacks for parent frame coordination
 * 
 * Interactions:
 * - Embedded in AirTrafficControllerFrame or CityMapPanel
 * - Receives updates from manager classes and controllers
 * - Triggers actions via event listeners and callbacks
 * 
 * Patterns & Constraints:
 * - Extends JPanel for Swing integration
 * - Custom paintComponent for rendering where needed
 * - Event-driven updates for responsive UI
 * 
 * @author Haisam Elkewidy
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
    /** Icon containing the city map background image */
    private ImageIcon mapIcon;
    /** List of parking spaces to render on the map */
    private List<RealisticCityMap.ParkingSpace> parkingSpaces;
    
    /**
     * Functional interface for rendering parking spaces.
     * Allows customizable parking space visualization via strategy pattern.
     */
    public interface ParkingSpaceRenderer {
        /**
         * Renders parking spaces on the graphics context.
         * @param g2d Graphics2D context for drawing
         */
        void render(Graphics2D g2d);
    }
    
    /**
     * Functional interface for applying time-based shading.
     * Allows day/night cycle visualization via strategy pattern.
     */
    public interface ShadingRenderer {
        /**
         * Renders time-based shading overlay on the graphics context.
         * @param g2d Graphics2D context for drawing
         * @param width panel width in pixels
         * @param height panel height in pixels
         */
        void render(Graphics2D g2d, int width, int height);
    }
    
    /** Renderer strategy for drawing parking spaces */
    private ParkingSpaceRenderer parkingRenderer;
    /** Renderer strategy for drawing time-based shading */
    private ShadingRenderer shadingRenderer;
    
    /**
     * Constructs a new MapDisplayPanel with map image and parking spaces.
     * 
     * @param mapIcon the city map background image
     * @param parkingSpaces list of parking spaces to display
     */
    public MapDisplayPanel(ImageIcon mapIcon, 
                          List<RealisticCityMap.ParkingSpace> parkingSpaces) {
        this.mapIcon = mapIcon;  // Store map image reference
        this.parkingSpaces = parkingSpaces;  // Store parking spaces list
        setBackground(Color.WHITE);  // Set white background
    }
    
    /**
     * Sets the parking space renderer strategy.
     * Allows custom parking space visualization.
     * 
     * @param renderer the parking space renderer to use
     */
    public void setParkingRenderer(ParkingSpaceRenderer renderer) {
        this.parkingRenderer = renderer;  // Store renderer strategy
    }
    
    /**
     * Sets the shading renderer strategy.
     * Allows custom time-based shading effects.
     * 
     * @param renderer the shading renderer to use
     */
    public void setShadingRenderer(ShadingRenderer renderer) {
        this.shadingRenderer = renderer;  // Store renderer strategy
    }
    
    /**
     * Paints the map display panel with all layers.
     * Draws map image, applies shading, and renders parking spaces.
     * 
     * @param g Graphics context for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Paint background
        Graphics2D g2d = (Graphics2D) g;  // Cast to Graphics2D for advanced rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // Enable antialiasing
        
        // Draw the map image as base layer
        if (mapIcon != null) {  // Check map icon exists
            g2d.drawImage(mapIcon.getImage(), 0, 0, this);  // Draw image at origin
        }
        
        // Apply time-based shading overlay (day/night effect)
        if (shadingRenderer != null) {  // Check shading renderer is set
            shadingRenderer.render(g2d, getWidth(), getHeight());  // Render shading overlay
        }
        
        // Draw parking spaces on top
        if (parkingRenderer != null) {  // Check parking renderer is set
            parkingRenderer.render(g2d);  // Render parking spaces
        }
    }
    
    /**
     * Returns the preferred size of this panel.
     * Uses map icon dimensions if available.
     * 
     * @return preferred size matching map dimensions
     */
    @Override
    public Dimension getPreferredSize() {
        if (mapIcon != null) {  // Check map icon exists
            // Return dimension matching map size
            return new Dimension(mapIcon.getIconWidth(), mapIcon.getIconHeight());
        }
        return super.getPreferredSize();  // Return default size if no map
    }
    
    /**
     * Gets the parking spaces list.
     * Returns reference to parking spaces displayed on map.
     * 
     * @return list of parking spaces
     */
    public List<RealisticCityMap.ParkingSpace> getParkingSpaces() {
        return parkingSpaces;  // Return parking spaces list
    }
}
