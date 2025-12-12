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
     * Renders the complete jetpack flight visualization
     */
    public void renderFlight(Graphics2D g2d, FlightRenderState state) {
        // Draw trail
        if (!state.isParked && !state.isEmergencyHalt) {
            drawTrail(g2d, state.trail, state.color);
        }
        
        // Draw jetpack icon
        drawJetpackIcon(g2d, state.x, state.y, state.destination, state.color, state.isParked);
        
        // Draw callsign label with status
        drawCallsignLabel(g2d, state.x, state.y, state.callsign, state.altitude, 
                         state.isParked, state.isEmergencyHalt, state.isDetourActive, 
                         state.hasActiveHazards);
        
        // Draw destination and waypoint markers
        if (!state.isParked && !state.isEmergencyHalt) {
            drawDestinationMarker(g2d, state.activeTarget, state.color);
            drawWaypointMarkers(g2d, state.waypoints, state.currentWaypointIndex, state.color);
        }
    }
    
    /**
     * Draws the trail behind the jetpack with fading alpha
     */
    private void drawTrail(Graphics2D g2d, List<Point> trail, Color color) {
        for (int i = 0; i < trail.size() - 1; i++) {
            float alpha = 1.0f - ((float)i / 15);
            Color trailColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 100));
            g2d.setColor(trailColor);
            g2d.setStroke(new BasicStroke(2));
            Point p1 = trail.get(i);
            Point p2 = trail.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
    
    /**
     * Draws the jetpack icon (triangle or parked "P" icon)
     */
    private void drawJetpackIcon(Graphics2D g2d, double x, double y, Point destination, 
                                Color color, boolean isParked) {
        if (isParked) {
            // Draw "P" icon for parked jetpack
            g2d.setColor(new Color(255, 165, 0)); // Orange color for parked
            g2d.fillOval((int)x - 8, (int)y - 8, 16, 16);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("P", (int)x - 4, (int)y + 5);
        } else {
            g2d.setColor(color);
            
            // Calculate angle towards destination
            double angle = Math.atan2(destination.y - y, destination.x - x);
            
            // Create triangle pointing in direction of travel
            int[] xPoints = {0, -8, -8};
            int[] yPoints = {0, -5, 5};
            
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(angle);
            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.setTransform(old);
        }
    }
    
    /**
     * Draws the callsign label with status indicators and altitude
     */
    private void drawCallsignLabel(Graphics2D g2d, double x, double y, String callsign, 
                                   double altitude, boolean isParked, boolean isEmergencyHalt,
                                   boolean isDetourActive, boolean hasActiveHazards) {
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        
        // Build status suffix
        String statusSuffix = "";
        if (isParked) {
            statusSuffix = " [PARKED]";
        } else if (isEmergencyHalt) {
            statusSuffix = " [HALT]";
        } else if (isDetourActive) {
            statusSuffix = " [DETOUR]";
        } else if (hasActiveHazards) {
            statusSuffix = " [!]";
        }
        
        String label = callsign + statusSuffix;
        FontMetrics fm = g2d.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        
        // Add altitude indicator
        String altLabel = String.format(" [Alt:%d]", (int)altitude);
        String fullLabel = label + altLabel;
        int fullLabelWidth = fm.stringWidth(fullLabel);
        
        // Background color based on status
        Color bgColor = new Color(0, 0, 0, 150);
        if (isEmergencyHalt) {
            bgColor = new Color(255, 0, 0, 180); // Red for emergency
        } else if (hasActiveHazards) {
            bgColor = new Color(255, 165, 0, 180); // Orange for hazards
        }
        
        g2d.setColor(bgColor);
        g2d.fillRect((int)x - 5, (int)y - 20, fullLabelWidth + 10, 15);
        
        // Callsign text
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, (int)x, (int)y - 8);
        
        // Altitude in color-coded text
        g2d.setColor(altitude < 80 ? Color.YELLOW : altitude > 160 ? Color.ORANGE : Color.CYAN);
        g2d.drawString(altLabel, (int)x + labelWidth, (int)y - 8);
    }
    
    /**
     * Draws the destination marker (crosshair)
     */
    private void drawDestinationMarker(Graphics2D g2d, Point target, Color color) {
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 128));
        g2d.drawOval(target.x - 5, target.y - 5, 10, 10);
        g2d.drawLine(target.x - 7, target.y, target.x + 7, target.y);
        g2d.drawLine(target.x, target.y - 7, target.x, target.y + 7);
    }
    
    /**
     * Draws waypoint markers along the flight path
     */
    private void drawWaypointMarkers(Graphics2D g2d, List<Point> waypoints, 
                                    int currentWaypointIndex, Color color) {
        if (!waypoints.isEmpty()) {
            for (int i = currentWaypointIndex; i < waypoints.size(); i++) {
                Point wp = waypoints.get(i);
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 64));
                g2d.fillOval(wp.x - 3, wp.y - 3, 6, 6);
            }
        }
    }
    
    /**
     * Data class containing all state needed for rendering
     */
    public static class FlightRenderState {
        public double x;
        public double y;
        public double altitude;
        public Point destination;
        public Point activeTarget;
        public Color color;
        public String callsign;
        public boolean isParked;
        public boolean isEmergencyHalt;
        public boolean isDetourActive;
        public boolean hasActiveHazards;
        public List<Point> trail;
        public List<Point> waypoints;
        public int currentWaypointIndex;
        
        public FlightRenderState(double x, double y, double altitude, Point destination,
                                Point activeTarget, Color color, String callsign,
                                boolean isParked, boolean isEmergencyHalt, 
                                boolean isDetourActive, boolean hasActiveHazards,
                                List<Point> trail, List<Point> waypoints, 
                                int currentWaypointIndex) {
            this.x = x;
            this.y = y;
            this.altitude = altitude;
            this.destination = destination;
            this.activeTarget = activeTarget;
            this.color = color;
            this.callsign = callsign;
            this.isParked = isParked;
            this.isEmergencyHalt = isEmergencyHalt;
            this.isDetourActive = isDetourActive;
            this.hasActiveHazards = hasActiveHazards;
            this.trail = trail;
            this.waypoints = waypoints;
            this.currentWaypointIndex = currentWaypointIndex;
        }
    }
}
