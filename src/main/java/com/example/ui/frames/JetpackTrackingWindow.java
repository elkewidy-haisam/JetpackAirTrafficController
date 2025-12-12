
/*
 * JetpackTrackingWindow.java
 * Displays a separate window tracking a single jetpack in 3D, with realistic city buildings, water detection,
 * nearby jetpacks, and accident markers. Supports both legacy Graphics2D and hardware-accelerated JOGL rendering.
 *
 * Last updated: December 10, 2025
 * Author: Jetpack Air Traffic Controller Team
 */
package com.example.ui.frames;

import com.example.jetpack.JetPack;
import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.ui.citymap.CityMapAnimationController;
import com.example.model.CityModel3D;
import com.example.ui.panels.JOGL3DPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.example.ui.citymap.CityMapLoader;
import com.example.ui.utility.Renderer3D;
import com.example.accident.AccidentAlert;

/**
 * JetpackTrackingWindow - 3D tracking window for a single jetpack.
 * Shows a behind-the-jetpack view, city context, and supports both JOGL and legacy rendering.
 */
public class JetpackTrackingWindow extends JFrame {
        private Timer updateTimer;
    private final JetPack jetpack;
    private final JetPackFlight flight;
    private final String cityName;
    private final List<JetPackFlight> allFlights;
    private final Map<JetPackFlight, JetPackFlightState> allStates;
    private final CityMapAnimationController animationController;
    private JPanel renderPanel; // Can be MapTrackingPanel or JOGL3DPanel
    private boolean useJOGL = true; // Set to true to use JOGL by default
    
    public JetpackTrackingWindow(JetPack jetpack, JetPackFlight flight, String cityName,
                                 List<JetPackFlight> allFlights, 
                                 Map<JetPackFlight, JetPackFlightState> allStates,
                                 CityMapAnimationController animationController) {
        this.jetpack = jetpack;
        this.flight = flight;
        this.cityName = cityName;
        this.allFlights = allFlights;
        this.allStates = allStates;
        this.animationController = animationController;
        
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        
        // Header panel with jetpack info and rendering toggle
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create 3D rendering panel based on mode
        createRenderPanel();
        mainPanel.add(renderPanel, BorderLayout.CENTER);
        
        // Info panel at bottom
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Start update timer (only for legacy mode)
        if (!useJOGL) {
            startUpdateTimer();
        }
        
        // Handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopUpdateTimer();
                if (renderPanel instanceof JOGL3DPanel) {
                    ((JOGL3DPanel) renderPanel).stopUpdateTimer();
                }
            }
        });
    }
    
    /**
     * Create rendering panel based on JOGL or legacy mode
     */
    private void createRenderPanel() {
        try {
            BufferedImage cityMap = CityMapLoader.loadCityMap(cityName);
            if (cityMap == null) {
                throw new RuntimeException("City map could not be loaded for JOGL panel");
            }
            JOGL3DPanel joglPanel = new JOGL3DPanel(cityName, flight, allFlights, allStates, cityMap);
            com.jogamp.opengl.awt.GLJPanel glPanel = joglPanel.getGLPanel();
            com.example.ui.utility.JOGLRenderer3D renderer = joglPanel.getRenderer();
            glPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    renderer.mousePressed(e.getX(), e.getY());
                }
                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    renderer.mouseReleased(e.getX(), e.getY());
                }
            });
            glPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseDragged(java.awt.event.MouseEvent e) {
                    renderer.mouseDragged(e.getX(), e.getY());
                    glPanel.repaint();
                }
            });
            glPanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
                @Override
                public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                    renderer.mouseWheelMoved(e.getWheelRotation());
                    glPanel.repaint();
                }
            });
            renderPanel = joglPanel;
            System.out.println("Using JOGL hardware-accelerated 3D rendering");
        } catch (Exception e) {
            System.err.println("Failed to initialize JOGL: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Failed to initialize JOGL 3D window:\n" + e.getMessage(),
                "JOGL Initialization Error",
                JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("JOGL initialization failed", e);
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title with renderer type indicator
        String rendererType = useJOGL ? "JOGL OpenGL" : "Graphics2D";
        JLabel titleLabel = new JLabel("Jetpack Tracking - " + cityName + " (" + rendererType + ")");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel callsignLabel = new JLabel("Callsign: " + jetpack.getCallsign());
        callsignLabel.setFont(new Font("Arial", Font.BOLD, 14));
        callsignLabel.setForeground(Color.YELLOW);
        callsignLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Replace pipe with hyphen for a cleaner header
        JLabel serialLabel = new JLabel("Serial: " + jetpack.getSerialNumber() + " - Owner: " + jetpack.getOwnerName());
        serialLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        serialLabel.setForeground(Color.WHITE);
        serialLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel modelLabel = new JLabel("Model: " + jetpack.getModel() + " - Manufacturer: " + jetpack.getManufacturer() + " - Year: " + jetpack.getYear());
        modelLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        modelLabel.setForeground(Color.WHITE);
        modelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(callsignLabel);
        headerPanel.add(Box.createVerticalStrut(3));
        headerPanel.add(serialLabel);
        headerPanel.add(Box.createVerticalStrut(3));
        headerPanel.add(modelLabel);
        
        return headerPanel;
    }
    
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(30, 30, 40));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String rendererInfo = useJOGL ? 
            "Hardware-accelerated OpenGL rendering with JOGL. Enhanced lighting, depth, and performance." :
            "Software-based Graphics2D rendering. Fallback mode.";
        JLabel infoLabel = new JLabel("3D View: " + rendererInfo + " Shows nearby jetpacks, accidents, parking, emergency states.");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        infoLabel.setForeground(Color.LIGHT_GRAY);
        infoPanel.add(infoLabel);
        
        return infoPanel;
    }
    
    private void startUpdateTimer() {
        if (renderPanel instanceof MapTrackingPanel) {
            updateTimer = new javax.swing.Timer(50, e -> {
                renderPanel.repaint();
            });
            updateTimer.start();
        }
        // JOGL panel has its own update timer
    }
    
    private void stopUpdateTimer() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }
    
    /**
     * Inner class for rendering 3D behind-the-jetpack view with full context from main panel
     */
    private static class MapTrackingPanel extends JPanel {
        private final String cityName;
        private final JetPackFlight flight;
        private final List<JetPackFlight> allFlights;
        private final Map<JetPackFlight, JetPackFlightState> allStates;
        // Removed unused field: private final CityMapAnimationController animationController;
        private CityModel3D cityModel;
        
        public MapTrackingPanel(String cityName, JetPackFlight flight,
                               List<JetPackFlight> allFlights,
                               Map<JetPackFlight, JetPackFlightState> allStates,
                               CityMapAnimationController animationController) {
            this.cityName = cityName;
            this.flight = flight;
            this.allFlights = allFlights;
            this.allStates = allStates;
            // Removed unused assignment: this.animationController = animationController;
            setBackground(Color.BLACK);
            loadCityModel();
        }
        
        private void loadCityModel() {
            try {
                BufferedImage cityMap = CityMapLoader.loadCityMap(cityName);
                if (cityMap != null) {
                    cityModel = new CityModel3D(cityName, cityMap);
                    System.out.println("Loaded 3D city model for " + cityName + " with " + 
                                     cityModel.getBuildings().size() + " buildings");
                }
            } catch (Exception e) {
                System.err.println("Error loading city model for tracking: " + e.getMessage());
                // e.printStackTrace(); // Suppressed for cleaner output
                cityModel = null;
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            if (cityModel != null) {
                // Get nearby jetpacks for rendering
                List<JetPackFlight> nearbyJetpacks = getNearbyJetpacks();
                
                // Get active accidents (placeholder for now)
                List<AccidentAlert.Accident> accidents = new ArrayList<>();
                
                // Render complete 3D scene with all context
                Renderer3D.renderScene(g2d, getWidth(), getHeight(), flight, cityModel, 
                                      nearbyJetpacks, accidents, allStates);
            } else {
                // Fallback rendering if city model failed to load
                drawFallbackView(g2d);
            }
            
            // Draw enhanced HUD overlay
            drawEnhancedHUD(g2d);
        }
        
        /**
         * Get jetpacks within visible range (1500 units)
         */
        private List<JetPackFlight> getNearbyJetpacks() {
            List<JetPackFlight> nearby = new ArrayList<>();
            double myX = flight.getX();
            double myY = flight.getY();
            
            for (JetPackFlight otherFlight : allFlights) {
                if (otherFlight == flight) continue; // Skip self
                
                double dx = otherFlight.getX() - myX;
                double dy = otherFlight.getY() - myY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance < 1500) { // Within render range
                    nearby.add(otherFlight);
                }
            }
            
            return nearby;
        }
        
        private void drawFallbackView(Graphics2D g2d) {
            g2d.setColor(new Color(50, 50, 70));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String msg = "Loading city model for " + cityName + "...";
            FontMetrics fm = g2d.getFontMetrics();
            int msgWidth = fm.stringWidth(msg);
            g2d.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
        }
        
        private void drawEnhancedHUD(Graphics2D g2d) {
            // Check flight state
            JetPackFlightState state = allStates.get(flight);
            boolean isParked = (state != null && state.isParked());
            boolean isEmergency = flight.isEmergencyHalt();
            
            // HUD background panel - expanded for more info
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRoundRect(10, 10, 300, 180, 10, 10);
            
            // HUD text
            g2d.setFont(new Font("Monospaced", Font.BOLD, 13));
            
            int y = 30;
            int lineHeight = 20;
            
            // Coordinates (synced with main map)
            g2d.setColor(Color.GREEN);
            g2d.drawString("X: " + String.format("%.1f", flight.getX()), 20, y);
            g2d.drawString("Y: " + String.format("%.1f", flight.getY()), 20, y + lineHeight);
            y += lineHeight * 2 + 5;
            
            // Flight state with color coding
            if (isParked) {
                g2d.setColor(new Color(255, 165, 0)); // Orange for parked
                g2d.drawString("State: PARKED", 20, y);
            } else if (isEmergency) {
                g2d.setColor(new Color(255, 0, 0)); // Red for emergency
                g2d.drawString("State: EMERGENCY HALT", 20, y);
            } else {
                g2d.setColor(Color.CYAN);
                g2d.drawString("State: " + flight.getCurrentStatus(), 20, y);
            }
            y += lineHeight + 5;
            
            // Terrain type
            if (cityModel != null) {
                String terrain = cityModel.getTerrainType(flight.getX(), flight.getY());
                Color terrainColor = terrain.equals("water") ? new Color(50, 150, 255) : 
                                    terrain.equals("building") ? new Color(200, 200, 200) :
                                    new Color(100, 200, 100);
                g2d.setColor(terrainColor);
                String terrainText = terrain.equals("water") ? "Terrain: WATER 🌊" :
                                   terrain.equals("building") ? "Terrain: BUILDING 🏢" :
                                   "Terrain: LAND 🌲";
                g2d.drawString(terrainText, 20, y);
            }
            y += lineHeight + 5;
            

            // City info
            g2d.setColor(Color.WHITE);
            g2d.drawString("City: " + cityName, 20, y);
            y += lineHeight;

            // Nearby jetpacks count
            List<JetPackFlight> nearby = getNearbyJetpacks();
            if (!nearby.isEmpty()) {
                g2d.setColor(new Color(150, 200, 255));
                g2d.drawString("Nearby: " + nearby.size() + " jetpacks", 20, y);
            }
            
            // Crosshair - color changes based on state
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            
            // Change crosshair color based on state
            if (isParked) {
                g2d.setColor(new Color(255, 165, 0, 150)); // Orange when parked
            } else if (isEmergency) {
                g2d.setColor(new Color(255, 0, 0, 200)); // Red when emergency
            } else {
                g2d.setColor(new Color(0, 255, 0, 150)); // Green normal
            }
            
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(centerX - 20, centerY, centerX - 5, centerY);
            g2d.drawLine(centerX + 5, centerY, centerX + 20, centerY);
            g2d.drawLine(centerX, centerY - 20, centerX, centerY - 5);
            g2d.drawLine(centerX, centerY + 5, centerX, centerY + 20);
            g2d.drawOval(centerX - 3, centerY - 3, 6, 6);
        }
    }
}
