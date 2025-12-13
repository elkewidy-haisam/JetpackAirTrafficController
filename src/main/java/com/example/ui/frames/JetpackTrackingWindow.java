/**
 * JetpackTrackingWindow component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides jetpacktrackingwindow functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core jetpacktrackingwindow operations
 * - Maintain necessary state for jetpacktrackingwindow functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
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
    // Timer for periodic repaints in legacy Graphics2D mode (unused in JOGL mode)
    private Timer updateTimer;
    // The specific jetpack being tracked by this window
    private final JetPack jetpack;
    // Flight instance containing position and state data for tracked jetpack
    private final JetPackFlight flight;
    // Name of the city where tracking is occurring (e.g., "New York", "Boston")
    private final String cityName;
    // Complete list of all active flights in the city for context rendering
    private final List<JetPackFlight> allFlights;
    // Map of flight states for all jetpacks (parking, emergency, normal)
    private final Map<JetPackFlight, JetPackFlightState> allStates;
    // Animation controller from main panel for synchronized updates
    private final CityMapAnimationController animationController;
    // Polymorphic render panel (either JOGL3DPanel or MapTrackingPanel)
    private JPanel renderPanel;
    // Flag to select rendering mode: true for JOGL OpenGL, false for Graphics2D fallback
    private boolean useJOGL = true;
    
    /**
     * Constructs a new tracking window for a specific jetpack.
     * Initializes UI components and sets up 3D rendering pipeline.
     *
     * @param jetpack The jetpack to track
     * @param flight Flight data for the tracked jetpack
     * @param cityName Name of the city context
     * @param allFlights List of all flights for situational awareness
     * @param allStates Map of flight states for all jetpacks
     * @param animationController Controller for synchronized animation updates
     */
    public JetpackTrackingWindow(JetPack jetpack, JetPackFlight flight, String cityName,
                                 List<JetPackFlight> allFlights, 
                                 Map<JetPackFlight, JetPackFlightState> allStates,
                                 CityMapAnimationController animationController) {
        // Store reference to tracked jetpack
        this.jetpack = jetpack;
        // Store flight instance for position/state queries
        this.flight = flight;
        // Store city name for title and map loading
        this.cityName = cityName;
        // Store all flights for rendering nearby jetpacks
        this.allFlights = allFlights;
        // Store state map for checking parking/emergency status
        this.allStates = allStates;
        // Store animation controller for synchronized updates
        this.animationController = animationController;
        
        // Set window dimensions (900x700 pixels)
        setSize(900, 700);
        // Close only this window on X button, not entire application
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Center window on screen
        setLocationRelativeTo(null);
        
        // Create main container panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        // Use black background for 3D immersion
        mainPanel.setBackground(Color.BLACK);
        
        // Create header showing jetpack identification details
        JPanel headerPanel = createHeaderPanel();
        // Place header at top of window
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Initialize JOGL or Graphics2D rendering panel
        createRenderPanel();
        // Place render view in center (takes most space)
        mainPanel.add(renderPanel, BorderLayout.CENTER);
        
        // Create info panel with renderer description
        JPanel infoPanel = createInfoPanel();
        // Place info panel at bottom of window
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Start periodic repaint timer for legacy Graphics2D mode only
        if (!useJOGL) {
            startUpdateTimer();  // JOGL has its own internal timer
        }
        
        // Register window close handler for cleanup
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Stop Swing timer if running
                stopUpdateTimer();
                // Stop JOGL animator if using hardware rendering
                if (renderPanel instanceof JOGL3DPanel) {
                    ((JOGL3DPanel) renderPanel).stopUpdateTimer();
                }
            }
        });
    }
    
    /**
     * Create rendering panel based on JOGL or legacy mode.
     * Attempts JOGL initialization; throws exception on failure.
     */
    private void createRenderPanel() {
        try {
            // Load city map PNG image for terrain/building data
            BufferedImage cityMap = CityMapLoader.loadCityMap(cityName);
            // Validate that map loaded successfully
            if (cityMap == null) {
                throw new RuntimeException("City map could not be loaded for JOGL panel");
            }
            // Create JOGL 3D panel with flight tracking context
            JOGL3DPanel joglPanel = new JOGL3DPanel(cityName, flight, allFlights, allStates, cityMap);
            // Get the underlying JOGL GL panel for direct event handling
            com.jogamp.opengl.awt.GLJPanel glPanel = joglPanel.getGLPanel();
            // Get renderer for camera control
            com.example.ui.utility.JOGLRenderer3D renderer = joglPanel.getRenderer();
            
            // Add mouse listener for camera rotation via click-drag
            glPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    // Notify renderer of mouse press for drag start
                    renderer.mousePressed(e.getX(), e.getY());
                }
                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    // Notify renderer of mouse release for drag end
                    renderer.mouseReleased(e.getX(), e.getY());
                }
            });
            
            // Add mouse motion listener for camera panning via drag
            glPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseDragged(java.awt.event.MouseEvent e) {
                    // Update camera orientation based on drag motion
                    renderer.mouseDragged(e.getX(), e.getY());
                    // Trigger OpenGL redraw with new camera angles
                    glPanel.repaint();
                }
            });
            
            // Add mouse wheel listener for camera zoom in/out
            glPanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
                @Override
                public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                    // Adjust camera distance based on wheel rotation
                    renderer.mouseWheelMoved(e.getWheelRotation());
                    // Trigger OpenGL redraw with new zoom level
                    glPanel.repaint();
                }
            });
            
            // Assign JOGL panel as the render panel
            renderPanel = joglPanel;
            // Log successful JOGL initialization
            System.out.println("Using JOGL hardware-accelerated 3D rendering");
        } catch (Exception e) {
            // Log JOGL initialization failure to stderr
            System.err.println("Failed to initialize JOGL: " + e.getMessage());
            e.printStackTrace();
            // Show error dialog to user
            JOptionPane.showMessageDialog(this,
                "Failed to initialize JOGL 3D window:\n" + e.getMessage(),
                "JOGL Initialization Error",
                JOptionPane.ERROR_MESSAGE);
            // Re-throw as runtime exception to prevent window creation
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
        
        JLabel serialLabel = new JLabel("Serial: " + jetpack.getSerialNumber() + " | Owner: " + jetpack.getOwnerName());
        serialLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        serialLabel.setForeground(Color.WHITE);
        serialLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel modelLabel = new JLabel("Model: " + jetpack.getModel() + " | Manufacturer: " + jetpack.getManufacturer() + " | Year: " + jetpack.getYear());
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
            
            // Jetpack info (callsign, serial, owner, model, bird name) - match 2D panel
            com.example.jetpack.JetPack jp = flight.getJetpack();
            int idx = 0;
            if (allFlights != null) {
                for (int i = 0; i < allFlights.size(); i++) {
                    if (allFlights.get(i) == flight) { idx = i; break; }
                }
            }
            String[] birdNames = {
                "Falcon", "Eagle", "Hawk", "Osprey", "Swift", "Albatross", "Condor", "Kestrel", "Merlin", "Harrier",
                "Heron", "Raven", "Sparrow", "Vireo", "Peregrine", "Goshawk", "Avocet", "Tern", "Cormorant", "Buzzard",
                "Crane", "Lark", "Oriole", "Jay", "Wren", "Finch", "Bittern", "Snipe", "Stork", "Ibis"
            };
            String birdName = birdNames[idx % birdNames.length];
            // Match 2D panel: Callsign | Serial | Owner | Model | Bird
            String info = String.format("%-12s  |  %-15s  |  %-20s  |  %s %s",
                jp.getCallsign(), jp.getSerialNumber(), jp.getOwnerName(), jp.getModel(), birdName);
            g2d.setColor(new Color(255, 255, 200));
            g2d.drawString(info, 20, y);
            y += lineHeight;

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
