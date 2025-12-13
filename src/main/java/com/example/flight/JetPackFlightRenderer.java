/**
 * Rendering component for jetpackflight visualization.
 * 
 * Purpose:
 * Handles rendering logic for jetpackflight using appropriate
 * graphics APIs. Translates model data into visual representations with proper scaling,
 * colors, and transformations.
 * 
 * Key Responsibilities:
 * - Render jetpackflight data to graphics context
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

package com.example.flight;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * JetPackFlightRenderer - Handles all rendering logic for a JetPackFlight
 */
public class JetPackFlightRenderer {
    
    /**
     * Renders the complete jetpack flight visualization including trail, icon, labels, and markers.
     * Coordinates all rendering components based on flight state.
     * 
     * @param g2d Graphics2D context for drawing
     * @param state Flight render state containing all visual data
     */
    public void renderFlight(Graphics2D g2d, FlightRenderState state) {
        // Draw trail only if jetpack is active (not parked or halted)
        if (!state.isParked && !state.isEmergencyHalt) {
            drawTrail(g2d, state.trail, state.color);  // Render position history trail
        }
        
        // Draw the jetpack icon at current position
        drawJetpackIcon(g2d, state.x, state.y, state.destination, state.color, state.isParked);
        
        // Draw callsign label with altitude and status indicators
        drawCallsignLabel(g2d, state.x, state.y, state.callsign, state.altitude, 
                         state.isParked, state.isEmergencyHalt, state.isDetourActive, 
                         state.hasActiveHazards);
        
        // Draw destination and waypoint markers only for active flights
        if (!state.isParked && !state.isEmergencyHalt) {
            drawDestinationMarker(g2d, state.activeTarget, state.color);  // Draw target crosshair
            drawWaypointMarkers(g2d, state.waypoints, state.currentWaypointIndex, state.color);  // Draw remaining waypoints
        }
    }
    
    /**
     * Draws the flight path trail behind the jetpack with fading alpha effect.
     * Recent positions are drawn with full opacity, fading to transparent for older positions.
     * 
     * @param g2d Graphics2D context for drawing
     * @param trail List of recent position points (newest first)
     * @param color Base color for the trail
     */
    private void drawTrail(Graphics2D g2d, List<Point> trail, Color color) {
        for (int i = 0; i < trail.size() - 1; i++) {  // Iterate through trail segments
            float alpha = 1.0f - ((float)i / 15);  // Calculate fade: 1.0 for recent, 0.0 for oldest
            // Create semi-transparent color based on fade calculation
            Color trailColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 100));
            g2d.setColor(trailColor);  // Apply faded color
            g2d.setStroke(new BasicStroke(2));  // Set trail line width to 2 pixels
            Point p1 = trail.get(i);  // Get current point
            Point p2 = trail.get(i + 1);  // Get next point
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);  // Draw line segment between points
        }
    }
    
    /**
     * Draws the jetpack icon: either a parking "P" symbol or directional triangle.
     * Parked jetpacks show an orange circle with "P", flying jetpacks show a triangle
     * pointing toward the destination.
     * 
     * @param g2d Graphics2D context for drawing
     * @param x Current x position of jetpack
     * @param y Current y position of jetpack
     * @param destination Target destination point
     * @param color Jetpack color for the icon
     * @param isParked true if jetpack is parked, false if flying
     */
    private void drawJetpackIcon(Graphics2D g2d, double x, double y, Point destination, 
                                Color color, boolean isParked) {
        if (isParked) {  // Check if jetpack is parked
            // Draw orange circle with "P" for parked state
            g2d.setColor(new Color(255, 165, 0));  // Set orange color for parking indicator
            g2d.fillOval((int)x - 8, (int)y - 8, 16, 16);  // Draw 16x16 circle centered at position
            g2d.setColor(Color.WHITE);  // Set white color for text
            g2d.setFont(new Font("Arial", Font.BOLD, 12));  // Set bold 12pt font
            g2d.drawString("P", (int)x - 4, (int)y + 5);  // Draw "P" centered in circle
        } else {  // Jetpack is flying
            g2d.setColor(color);  // Use jetpack's assigned color
            
            // Calculate angle from current position to destination
            double angle = Math.atan2(destination.y - y, destination.x - x);  // Get angle in radians
            
            // Define triangle vertices pointing right (will be rotated toward destination)
            int[] xPoints = {0, -8, -8};  // Triangle vertices: tip at (0,0), base at x=-8
            int[] yPoints = {0, -5, 5};   // Triangle height spans from y=-5 to y=5
            
            AffineTransform old = g2d.getTransform();  // Save current transformation
            g2d.translate(x, y);  // Move origin to jetpack position
            g2d.rotate(angle);  // Rotate to point toward destination
            g2d.fillPolygon(xPoints, yPoints, 3);  // Draw filled triangle with 3 vertices
            g2d.setTransform(old);  // Restore original transformation
        }
    }
    
    /**
     * Draws the callsign label with status indicators and altitude information.
     * Label shows callsign, current status ([PARKED], [HALT], [DETOUR], [!]),
     * and altitude with color-coded background based on flight state.
     * 
     * @param g2d Graphics2D context for drawing
     * @param x Jetpack x position
     * @param y Jetpack y position
     * @param callsign Jetpack identifier
     * @param altitude Current altitude in units
     * @param isParked true if parked
     * @param isEmergencyHalt true if emergency halt active
     * @param isDetourActive true if on detour route
     * @param hasActiveHazards true if hazards present
     */
    private void drawCallsignLabel(Graphics2D g2d, double x, double y, String callsign, 
                                   double altitude, boolean isParked, boolean isEmergencyHalt,
                                   boolean isDetourActive, boolean hasActiveHazards) {
        g2d.setFont(new Font("Arial", Font.BOLD, 10));  // Set bold 10pt font for label
        
        // Build status suffix based on priority: parked > halt > detour > hazards
        String statusSuffix = "";  // Initialize empty status
        if (isParked) {
            statusSuffix = " [PARKED]";  // Indicate parked state
        } else if (isEmergencyHalt) {
            statusSuffix = " [HALT]";  // Indicate emergency halt
        } else if (isDetourActive) {
            statusSuffix = " [DETOUR]";  // Indicate detour active
        } else if (hasActiveHazards) {
            statusSuffix = " [!]";  // Indicate hazards present
        }
        
        String label = callsign + statusSuffix;  // Combine callsign and status
        FontMetrics fm = g2d.getFontMetrics();  // Get font metrics for width calculation
        int labelWidth = fm.stringWidth(label);  // Calculate label width in pixels
        
        // Add altitude indicator with formatting
        String altLabel = String.format(" [Alt:%d]", (int)altitude);  // Format altitude as integer
        String fullLabel = label + altLabel;  // Combine label and altitude
        int fullLabelWidth = fm.stringWidth(fullLabel);  // Calculate full label width
        
        // Select background color based on status (red for emergency, orange for hazards, black default)
        Color bgColor = new Color(0, 0, 0, 150);  // Default semi-transparent black
        if (isEmergencyHalt) {
            bgColor = new Color(255, 0, 0, 180);  // Red background for emergency
        } else if (hasActiveHazards) {
            bgColor = new Color(255, 165, 0, 180);  // Orange background for hazards
        }
        
        g2d.setColor(bgColor);  // Apply background color
        g2d.fillRect((int)x - 5, (int)y - 20, fullLabelWidth + 10, 15);  // Draw background rectangle
        
        // Draw callsign and status in white
        g2d.setColor(Color.WHITE);  // Set white text color
        g2d.drawString(label, (int)x, (int)y - 8);  // Draw callsign and status
        
        // Draw altitude with color coding: yellow for low, orange for high, cyan for normal
        g2d.setColor(altitude < 80 ? Color.YELLOW : altitude > 160 ? Color.ORANGE : Color.CYAN);  // Select altitude color
        g2d.drawString(altLabel, (int)x + labelWidth, (int)y - 8);  // Draw altitude indicator
    }
    
    /**
     * Draws the destination marker as a crosshair symbol.
     * Marker consists of a circle with horizontal and vertical lines through the center.
     * 
     * @param g2d Graphics2D context for drawing
     * @param target Destination point coordinates
     * @param color Base color for the marker (rendered semi-transparent)
     */
    private void drawDestinationMarker(Graphics2D g2d, Point target, Color color) {
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 128));  // Semi-transparent color
        g2d.drawOval(target.x - 5, target.y - 5, 10, 10);  // Draw 10x10 circle around target
        g2d.drawLine(target.x - 7, target.y, target.x + 7, target.y);  // Draw horizontal crosshair line
        g2d.drawLine(target.x, target.y - 7, target.x, target.y + 7);  // Draw vertical crosshair line
    }
    
    /**
     * Draws waypoint markers along the flight path for remaining waypoints.
     * Only draws waypoints that haven't been passed yet (from currentWaypointIndex onward).
     * Each waypoint is rendered as a small semi-transparent filled circle.
     * 
     * @param g2d Graphics2D context for drawing
     * @param waypoints List of all waypoints in the flight path
     * @param currentWaypointIndex Index of next waypoint to reach
     * @param color Base color for waypoint markers
     */
    private void drawWaypointMarkers(Graphics2D g2d, List<Point> waypoints, 
                                    int currentWaypointIndex, Color color) {
        if (!waypoints.isEmpty()) {  // Check if waypoints exist
            for (int i = currentWaypointIndex; i < waypoints.size(); i++) {  // Iterate from current to end
                Point wp = waypoints.get(i);  // Get waypoint coordinates
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 64));  // Very transparent color
                g2d.fillOval(wp.x - 3, wp.y - 3, 6, 6);  // Draw small 6x6 filled circle
            }
        }
    }
    
    /**
     * Data transfer object containing all state data needed for rendering a jetpack flight.
     * Encapsulates position, navigation, status, and visual properties to pass to renderer.
     * Immutable after construction to ensure consistent rendering state.
     */
    public static class FlightRenderState {
        // Position coordinates
        public double x;  // Current x position
        public double y;  // Current y position
        public double altitude;  // Current altitude above ground
        
        // Navigation targets
        public Point destination;  // Final destination point
        public Point activeTarget;  // Current navigation target (destination or waypoint)
        
        // Visual properties
        public Color color;  // Jetpack color for icon and markers
        public String callsign;  // Jetpack identifier for label
        
        // Status flags
        public boolean isParked;  // true if jetpack is parked
        public boolean isEmergencyHalt;  // true if emergency halt active
        public boolean isDetourActive;  // true if following detour route
        public boolean hasActiveHazards;  // true if hazards present
        
        // Path data
        public List<Point> trail;  // Recent position history for trail rendering
        public List<Point> waypoints;  // All waypoints in flight path
        public int currentWaypointIndex;  // Index of next waypoint to reach
        
        /**
         * Constructs a complete flight render state snapshot.
         * 
         * @param x Current x position
         * @param y Current y position
         * @param altitude Current altitude
         * @param destination Final destination point
         * @param activeTarget Current navigation target
         * @param color Jetpack color
         * @param callsign Jetpack identifier
         * @param isParked Parked status
         * @param isEmergencyHalt Emergency halt status
         * @param isDetourActive Detour status
         * @param hasActiveHazards Hazard presence
         * @param trail Position history trail
         * @param waypoints Flight path waypoints
         * @param currentWaypointIndex Next waypoint index
         */
        public FlightRenderState(double x, double y, double altitude, Point destination,
                                Point activeTarget, Color color, String callsign,
                                boolean isParked, boolean isEmergencyHalt, 
                                boolean isDetourActive, boolean hasActiveHazards,
                                List<Point> trail, List<Point> waypoints, 
                                int currentWaypointIndex) {
            this.x = x;  // Store x position
            this.y = y;  // Store y position
            this.altitude = altitude;  // Store altitude
            this.destination = destination;  // Store final destination
            this.activeTarget = activeTarget;  // Store current target
            this.color = color;  // Store jetpack color
            this.callsign = callsign;  // Store identifier
            this.isParked = isParked;  // Store parked flag
            this.isEmergencyHalt = isEmergencyHalt;  // Store halt flag
            this.isDetourActive = isDetourActive;  // Store detour flag
            this.hasActiveHazards = hasActiveHazards;  // Store hazard flag
            this.trail = trail;  // Store trail history
            this.waypoints = waypoints;  // Store waypoint list
            this.currentWaypointIndex = currentWaypointIndex;  // Store waypoint index
        }
    }
}
