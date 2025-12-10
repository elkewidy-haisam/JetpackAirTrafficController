package com.example.ui.utility;

import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.model.Building3D;
import com.example.model.CityModel3D;
import com.example.accident.AccidentAlert;

import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Renderer3D - Advanced 3D rendering for jetpack tracking with realistic city models
 * Enhanced to show nearby jetpacks, accidents, and flight states
 */
public class Renderer3D {
    private static final double FOV = 60.0; // Field of view in degrees
    private static final double NEAR_PLANE = 1.0;
    private static final double FAR_PLANE = 2000.0;
    
    /**
     * Render complete 3D scene from jetpack perspective (legacy method for backward compatibility)
     */
    public static void renderScene(Graphics2D g2d, int width, int height, 
                                  JetPackFlight flight, CityModel3D cityModel) {
        renderScene(g2d, width, height, flight, cityModel, new ArrayList<>(), new ArrayList<>(), null);
    }
    
    /**
     * Render complete 3D scene from jetpack perspective with nearby jetpacks and accidents
     */
    public static void renderScene(Graphics2D g2d, int width, int height, 
                                  JetPackFlight flight, CityModel3D cityModel,
                                  List<JetPackFlight> nearbyJetpacks,
                                  List<AccidentAlert.Accident> accidents,
                                  Map<JetPackFlight, JetPackFlightState> flightStates) {
        // Clear background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);
        
        // Get jetpack position and direction
        double jetpackX = flight.getX();
        double jetpackY = flight.getY();
        double jetpackAlt = 100; // Default altitude for viewing
        
        // Get destination for camera direction
        Point dest = flight.getDestination();
        double dx = dest.x - jetpackX;
        double dy = dest.y - jetpackY;
        double angle = Math.atan2(dy, dx);
        
        // Draw sky
        drawSky(g2d, width, height);
        
        // Draw ground/water
        drawGround(g2d, width, height, jetpackX, jetpackY, angle, cityModel);
        
        // Get and sort buildings by distance (back to front)
        List<Building3D> visibleBuildings = getVisibleBuildings(
            jetpackX, jetpackY, angle, cityModel.getBuildings());
        
        // Draw buildings
        for (Building3D building : visibleBuildings) {
            drawBuilding3D(g2d, width, height, building, jetpackX, jetpackY, 
                          jetpackAlt, angle);
        }
        
        // Draw nearby jetpacks in 3D space
        if (nearbyJetpacks != null && !nearbyJetpacks.isEmpty()) {
            drawNearbyJetpacks(g2d, width, height, jetpackX, jetpackY, angle, 
                             nearbyJetpacks, flightStates);
        }
        
        // Draw accidents in 3D space
        if (accidents != null && !accidents.isEmpty()) {
            drawAccidents(g2d, width, height, jetpackX, jetpackY, angle, accidents);
        }
        
        // Draw water surfaces if over water
        if (cityModel.isWater(jetpackX, jetpackY)) {
            drawWaterEffect(g2d, width, height, jetpackX, jetpackY);
        }
        
        // Draw jetpack model in foreground (check if parked/emergency)
        boolean isParked = false;
        boolean isEmergency = flight.isEmergencyHalt();
        if (flightStates != null && flightStates.containsKey(flight)) {
            isParked = flightStates.get(flight).isParked();
        }
        drawJetpackModel(g2d, width, height, isParked, isEmergency);
        
        // Draw destination marker in 3D space
        drawDestinationMarker(g2d, width, height, jetpackX, jetpackY, 
                             dest.x, dest.y, angle);
    }
    
    /**
     * Draw gradient sky
     */
    private static void drawSky(Graphics2D g2d, int width, int height) {
        // Horizon gradient
        Color skyTop = new Color(60, 120, 180);      // Dark blue
        Color skyHorizon = new Color(135, 206, 250); // Sky blue
        
        GradientPaint skyGradient = new GradientPaint(
            0, 0, skyTop,
            0, height * 0.4f, skyHorizon
        );
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, width, (int)(height * 0.5));
    }
    
    /**
     * Draw ground plane with perspective
     */
    private static void drawGround(Graphics2D g2d, int width, int height, 
                                  double jetpackX, double jetpackY, 
                                  double viewAngle, CityModel3D cityModel) {
        int horizonY = height / 2;
        
        // Draw ground gradient
        Color groundNear = new Color(100, 150, 100);  // Green
        Color groundFar = new Color(80, 120, 80);     // Darker green
        
        GradientPaint groundGradient = new GradientPaint(
            0, horizonY, groundFar,
            0, height, groundNear
        );
        g2d.setPaint(groundGradient);
        g2d.fillRect(0, horizonY, width, height - horizonY);
        
        // Draw perspective grid
        g2d.setColor(new Color(70, 100, 70, 150));
        g2d.setStroke(new BasicStroke(1));
        
        // Horizontal lines
        for (int i = 0; i < 20; i++) {
            double dist = 50 + i * 100;
            int screenY = projectDepthToY(dist, horizonY, height);
            if (screenY > horizonY && screenY < height) {
                g2d.drawLine(0, screenY, width, screenY);
            }
        }
        
        // Vertical lines with perspective
        for (int i = -10; i <= 10; i++) {
            drawPerspectiveLine(g2d, width, horizonY, height, i * 100);
        }
        
        // Draw water patches
        drawWaterPatches(g2d, width, height, jetpackX, jetpackY, 
                        viewAngle, cityModel, horizonY);
    }
    
    /**
     * Draw water patches where they exist
     */
    private static void drawWaterPatches(Graphics2D g2d, int width, int height,
                                        double jetpackX, double jetpackY,
                                        double viewAngle, CityModel3D cityModel,
                                        int horizonY) {
        g2d.setColor(new Color(30, 80, 150, 180)); // Blue water
        
        // Check points in front of jetpack
        for (int depth = 50; depth < 1000; depth += 50) {
            for (int lateral = -500; lateral <= 500; lateral += 50) {
                // Calculate world position
                double worldX = jetpackX + depth * Math.cos(viewAngle) - 
                               lateral * Math.sin(viewAngle);
                double worldY = jetpackY + depth * Math.sin(viewAngle) + 
                               lateral * Math.cos(viewAngle);
                
                if (cityModel.isWater(worldX, worldY)) {
                    // Project to screen
                    int screenX = width / 2 + (int)(lateral * width / (2.0 * depth + 100));
                    int screenY = projectDepthToY(depth, horizonY, height);
                    
                    if (screenY > horizonY && screenY < height) {
                        int size = Math.max(2, 50 / (1 + depth / 100));
                        g2d.fillRect(screenX - size/2, screenY - size/2, size, size);
                    }
                }
            }
        }
    }
    
    /**
     * Draw water effect overlay when flying over water
     */
    private static void drawWaterEffect(Graphics2D g2d, int width, int height,
                                       double jetpackX, double jetpackY) {
        // Blue tint overlay
        g2d.setColor(new Color(30, 100, 180, 30));
        g2d.fillRect(0, height / 2, width, height / 2);
        
        // Add sparkle effect on water
        java.util.Random rand = new java.util.Random((long)(jetpackX + jetpackY));
        g2d.setColor(new Color(200, 230, 255, 150));
        for (int i = 0; i < 20; i++) {
            int x = rand.nextInt(width);
            int y = height / 2 + rand.nextInt(height / 2);
            g2d.fillOval(x, y, 3, 3);
        }
    }
    
    /**
     * Get visible buildings in view frustum
     */
    private static List<Building3D> getVisibleBuildings(double jetpackX, double jetpackY,
                                                       double viewAngle,
                                                       List<Building3D> allBuildings) {
        List<BuildingDistance> buildingsWithDistance = new ArrayList<>();
        
        for (Building3D building : allBuildings) {
            double dx = building.getX() - jetpackX;
            double dy = building.getY() - jetpackY;
            double dist = Math.sqrt(dx * dx + dy * dy);
            
            if (dist < 1500) { // Only render nearby buildings
                // Check if in view frustum (roughly 120 degree view)
                double angleToBuilding = Math.atan2(dy, dx);
                double angleDiff = normalizeAngle(angleToBuilding - viewAngle);
                
                if (Math.abs(angleDiff) < Math.PI * 0.66) { // ~120 degree FOV
                    buildingsWithDistance.add(new BuildingDistance(building, dist));
                }
            }
        }
        
        // Sort by distance (far to near for proper rendering)
        Collections.sort(buildingsWithDistance, 
            Comparator.comparingDouble(bd -> -bd.distance));
        
        List<Building3D> result = new ArrayList<>();
        for (BuildingDistance bd : buildingsWithDistance) {
            result.add(bd.building);
        }
        
        return result;
    }
    
    /**
     * Draw a single building in 3D
     */
    private static void drawBuilding3D(Graphics2D g2d, int width, int height,
                                      Building3D building, double jetpackX, 
                                      double jetpackY, double jetpackAlt,
                                      double viewAngle) {
        // Calculate relative position
        double dx = building.getX() - jetpackX;
        double dy = building.getY() - jetpackY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < 10) return; // Too close
        
        // Rotate to view space
        double cos = Math.cos(-viewAngle);
        double sin = Math.sin(-viewAngle);
        double rotX = dx * cos - dy * sin;
        double rotY = dx * sin + dy * cos;
        
        if (rotY < NEAR_PLANE) return; // Behind camera
        
        // Perspective projection
        int horizonY = height / 2;
        double scale = 500.0 / rotY;
        
        int screenX = width / 2 + (int)(rotX * scale);
        int buildingWidth = (int)(building.getWidth() * scale);
        int buildingHeight = (int)(building.getHeight() * scale);
        int buildingY = horizonY - buildingHeight;
        
        // Don't render if off screen
        if (screenX + buildingWidth < 0 || screenX > width) return;
        
        // Draw building with distance-based shading
        float brightness = (float)Math.max(0.3, 1.0 - distance / 1500.0);
        Color buildingColor = building.getColor();
        Color shadedColor = new Color(
            (int)(buildingColor.getRed() * brightness),
            (int)(buildingColor.getGreen() * brightness),
            (int)(buildingColor.getBlue() * brightness)
        );
        
        // Main building body
        g2d.setColor(shadedColor);
        g2d.fillRect(screenX - buildingWidth/2, buildingY, buildingWidth, buildingHeight);
        
        // Building outline
        g2d.setColor(shadedColor.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(screenX - buildingWidth/2, buildingY, buildingWidth, buildingHeight);
        
        // Windows
        if (building.hasWindows() && buildingWidth > 10 && buildingHeight > 20) {
            drawWindows(g2d, screenX - buildingWidth/2, buildingY, 
                       buildingWidth, buildingHeight, building.getFloors(), brightness);
        }
    }
    
    /**
     * Draw windows on building
     */
    private static void drawWindows(Graphics2D g2d, int x, int y, 
                                   int width, int height, int floors,
                                   float brightness) {
        Color windowColor = new Color(
            (int)(200 * brightness),
            (int)(220 * brightness),
            (int)(100 * brightness),
            180
        );
        g2d.setColor(windowColor);
        
        int windowsPerFloor = Math.max(2, width / 15);
        int floorHeight = height / Math.max(1, floors);
        
        for (int floor = 0; floor < floors && floor < height / 10; floor++) {
            int floorY = y + floor * floorHeight + 2;
            for (int window = 0; window < windowsPerFloor; window++) {
                int windowX = x + 4 + window * (width / windowsPerFloor);
                int windowW = Math.max(2, width / windowsPerFloor - 4);
                int windowH = Math.max(2, floorHeight - 4);
                
                // Randomly lit windows
                if (Math.random() > 0.3) {
                    g2d.fillRect(windowX, floorY, windowW, windowH);
                }
            }
        }
    }
    
    /**
     * Draw jetpack model in foreground with state-based visualization
     */
    private static void drawJetpackModel(Graphics2D g2d, int width, int height, 
                                        boolean isParked, boolean isEmergency) {
        int centerX = width / 2;
        int centerY = (int)(height * 0.75);
        
        // Jetpack body - color changes based on state
        Color bodyColor;
        if (isParked) {
            bodyColor = new Color(150, 120, 0); // Brown-ish for parked
        } else if (isEmergency) {
            bodyColor = new Color(150, 50, 50); // Reddish for emergency
        } else {
            bodyColor = new Color(100, 100, 120); // Normal grey
        }
        g2d.setColor(bodyColor);
        int[] bodyX = {centerX - 30, centerX + 30, centerX + 25, centerX - 25};
        int[] bodyY = {centerY - 40, centerY - 40, centerY + 20, centerY + 20};
        g2d.fillPolygon(bodyX, bodyY, 4);
        
        // Thrusters
        g2d.setColor(new Color(80, 80, 90));
        g2d.fillRoundRect(centerX - 25, centerY + 10, 15, 30, 5, 5);
        g2d.fillRoundRect(centerX + 10, centerY + 10, 15, 30, 5, 5);
        
        // Flames - no flames if parked or emergency halt
        if (!isParked && !isEmergency) {
            g2d.setColor(new Color(255, 150, 0, 200));
            int flameFlicker = (int)(System.currentTimeMillis() / 50) % 10;
            g2d.fillOval(centerX - 25, centerY + 35, 15, 20 + flameFlicker);
            g2d.fillOval(centerX + 10, centerY + 35, 15, 20 + flameFlicker);
            
            g2d.setColor(new Color(255, 200, 0, 150));
            g2d.fillOval(centerX - 23, centerY + 38, 11, 15 + flameFlicker);
            g2d.fillOval(centerX + 12, centerY + 38, 11, 15 + flameFlicker);
        }
    }
    
    /**
     * Draw destination marker in 3D space
     */
    private static void drawDestinationMarker(Graphics2D g2d, int width, int height,
                                             double jetpackX, double jetpackY,
                                             double destX, double destY,
                                             double viewAngle) {
        double dx = destX - jetpackX;
        double dy = destY - jetpackY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // Rotate to view space
        double cos = Math.cos(-viewAngle);
        double sin = Math.sin(-viewAngle);
        double rotX = dx * cos - dy * sin;
        double rotY = dx * sin + dy * cos;
        
        if (rotY > 10) { // In front of camera
            int horizonY = height / 2;
            double scale = 500.0 / rotY;
            
            int screenX = width / 2 + (int)(rotX * scale);
            int screenY = horizonY - (int)(50 * scale);
            
            // Draw target reticle
            g2d.setColor(new Color(255, 50, 50, 200));
            g2d.setStroke(new BasicStroke(3));
            int size = Math.max(20, (int)(100 * scale));
            g2d.drawOval(screenX - size/2, screenY - size/2, size, size);
            g2d.drawLine(screenX - size, screenY, screenX + size, screenY);
            g2d.drawLine(screenX, screenY - size, screenX, screenY + size);
            
            // Distance label
            g2d.setFont(new Font("Monospaced", Font.BOLD, 12));
            String distLabel = String.format("%.0fm", distance);
            g2d.drawString(distLabel, screenX - 20, screenY - size/2 - 5);
        }
    }
    
    /**
     * Draw nearby jetpacks in 3D space
     */
    private static void drawNearbyJetpacks(Graphics2D g2d, int width, int height,
                                          double myX, double myY, double viewAngle,
                                          List<JetPackFlight> nearbyJetpacks,
                                          Map<JetPackFlight, JetPackFlightState> flightStates) {
        int horizonY = height / 2;
        
        for (JetPackFlight otherFlight : nearbyJetpacks) {
            double dx = otherFlight.getX() - myX;
            double dy = otherFlight.getY() - myY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            // Rotate to view space
            double cos = Math.cos(-viewAngle);
            double sin = Math.sin(-viewAngle);
            double rotX = dx * cos - dy * sin;
            double rotY = dx * sin + dy * cos;
            
            if (rotY > 10 && distance < 1000) { // In front of camera and not too far
                double scale = 500.0 / rotY;
                
                int screenX = width / 2 + (int)(rotX * scale);
                int screenY = horizonY - (int)(100 * scale); // Assume flying altitude
                
                // Check if jetpack is parked or in emergency
                boolean isParked = false;
                boolean isEmergency = otherFlight.isEmergencyHalt();
                if (flightStates != null && flightStates.containsKey(otherFlight)) {
                    isParked = flightStates.get(otherFlight).isParked();
                }
                
                // Draw jetpack dot with state-based color
                Color jetpackColor;
                if (isParked) {
                    jetpackColor = new Color(255, 165, 0); // Orange for parked
                } else if (isEmergency) {
                    jetpackColor = new Color(255, 0, 0); // Red for emergency
                } else {
                    jetpackColor = otherFlight.getColor(); // Normal color
                }
                
                // Draw with glow effect
                int size = Math.max(5, (int)(20 * scale));
                g2d.setColor(new Color(jetpackColor.getRed(), jetpackColor.getGreen(), 
                                      jetpackColor.getBlue(), 80));
                g2d.fillOval(screenX - size, screenY - size, size * 2, size * 2);
                
                g2d.setColor(jetpackColor);
                g2d.fillOval(screenX - size/2, screenY - size/2, size, size);
                
                // Draw callsign label if close enough
                if (distance < 500) {
                    g2d.setFont(new Font("Arial", Font.BOLD, 10));
                    String label = otherFlight.getJetpack().getCallsign();
                    FontMetrics fm = g2d.getFontMetrics();
                    int labelWidth = fm.stringWidth(label);
                    
                    g2d.setColor(new Color(0, 0, 0, 200));
                    g2d.fillRect(screenX - labelWidth/2 - 2, screenY - size - 15, labelWidth + 4, 12);
                    
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(label, screenX - labelWidth/2, screenY - size - 5);
                }
            }
        }
    }
    
    /**
     * Draw accident markers in 3D space
     */
    private static void drawAccidents(Graphics2D g2d, int width, int height,
                                     double myX, double myY, double viewAngle,
                                     List<AccidentAlert.Accident> accidents) {
        int horizonY = height / 2;
        
        for (AccidentAlert.Accident accident : accidents) {
            if (!accident.isActive()) continue;
            
            double dx = accident.getX() - myX;
            double dy = accident.getY() - myY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            // Only show nearby accidents (within 1000 units)
            if (distance > 1000) continue;
            
            // Rotate to view space
            double cos = Math.cos(-viewAngle);
            double sin = Math.sin(-viewAngle);
            double rotX = dx * cos - dy * sin;
            double rotY = dx * sin + dy * cos;
            
            if (rotY > 10) { // In front of camera
                double scale = 500.0 / rotY;
                
                int screenX = width / 2 + (int)(rotX * scale);
                int screenY = horizonY - (int)(20 * scale); // Ground level
                
                // Draw warning symbol
                int size = Math.max(15, (int)(40 * scale));
                
                // Pulsing effect for accidents
                int pulse = (int)(System.currentTimeMillis() / 200) % 10;
                int alpha = 150 + pulse * 10;
                
                // Red/yellow warning triangle
                g2d.setColor(new Color(255, 0, 0, alpha));
                int[] triX = {screenX, screenX - size/2, screenX + size/2};
                int[] triY = {screenY - size, screenY + size/2, screenY + size/2};
                g2d.fillPolygon(triX, triY, 3);
                
                g2d.setColor(new Color(255, 255, 0, alpha));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawPolygon(triX, triY, 3);
                
                // Warning symbol
                g2d.setFont(new Font("Arial", Font.BOLD, size/2));
                g2d.setColor(Color.YELLOW);
                g2d.drawString("!", screenX - size/6, screenY + size/6);
                
                // Accident label if close
                if (distance < 300) {
                    g2d.setFont(new Font("Arial", Font.BOLD, 10));
                    String label = "ACCIDENT: " + accident.getType();
                    FontMetrics fm = g2d.getFontMetrics();
                    int labelWidth = fm.stringWidth(label);
                    
                    g2d.setColor(new Color(255, 0, 0, 220));
                    g2d.fillRect(screenX - labelWidth/2 - 2, screenY + size/2 + 5, labelWidth + 4, 12);
                    
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(label, screenX - labelWidth/2, screenY + size/2 + 15);
                }
            }
        }
    }
    
    // Helper methods
    
    private static int projectDepthToY(double depth, int horizonY, int screenHeight) {
        double perspective = 500.0 / (depth + 100);
        return horizonY + (int)((screenHeight - horizonY) * (1.0 - perspective));
    }
    
    private static void drawPerspectiveLine(Graphics2D g2d, int width, 
                                           int horizonY, int height, double offset) {
        int topX = width / 2 + (int)(offset * 0.1);
        int bottomX = width / 2 + (int)(offset * 2);
        g2d.drawLine(topX, horizonY, bottomX, height);
    }
    
    private static double normalizeAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
    
    // Helper class for sorting buildings
    private static class BuildingDistance {
        Building3D building;
        double distance;
        
        BuildingDistance(Building3D building, double distance) {
            this.building = building;
            this.distance = distance;
        }
    }
}
