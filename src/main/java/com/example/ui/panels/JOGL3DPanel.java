package com.example.ui.panels;


import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.model.CityModel3D;
import com.example.ui.utility.JOGLRenderer3D;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.example.accident.AccidentAlert;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * JOGL3DPanel - Hardware-accelerated 3D panel using JOGL
 * Drop-in replacement for MapTrackingPanel with OpenGL rendering
 */
public class JOGL3DPanel extends JPanel {
        // Add mouse listeners for camera control
        public void setupMouseListeners(GLJPanel glPanel, com.example.ui.utility.JOGLRenderer3D renderer) {
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
        }
    private JOGLRenderer3D renderer;
    private JetPackFlight flight;
    private CityModel3D cityModel;
    private List<JetPackFlight> allFlights;
    private Map<JetPackFlight, JetPackFlightState> flightStates;
    private Timer updateTimer;
    
    // HUD overlay (drawn using Swing on top of OpenGL)
    private JPanel hudOverlay;
    private JLabel positionLabel;
    private JLabel statusLabel;
    private JLabel terrainLabel;
    private JLabel callsignLabel;
    private JLabel cityLabel;

    // Camera control state
    private double cameraAzimuth = 45; // horizontal angle
    private double cameraElevation = 30; // vertical angle
    private double cameraDistance = 400; // zoom
    private int lastMouseX, lastMouseY;
    private boolean dragging = false;
    
    public JOGL3DPanel(String cityName, JetPackFlight flight, 
                   List<JetPackFlight> allFlights,
                   Map<JetPackFlight, JetPackFlightState> flightStates,
                   BufferedImage cityMap) {
        super();
        this.flight = flight;
        this.allFlights = allFlights;
        this.flightStates = flightStates;
        this.cityModel = new CityModel3D(cityName, cityMap);

        // Create JOGL renderer and GLJPanel
        this.renderer = new JOGLRenderer3D();
        GLJPanel glPanel = new GLJPanel(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
        glPanel.addGLEventListener(renderer);
        setupMouseListeners(glPanel, renderer);

        setLayout(new BorderLayout());
        setOpaque(false);
        add(glPanel, BorderLayout.CENTER);

        // Create HUD overlay panel (small, left-aligned)
        createHUDOverlay(cityName);
        add(hudOverlay, BorderLayout.WEST);
        // Start update timer
        startUpdateTimer();
        // Initialize camera in renderer
        updateCameraInRenderer();
    }

    /**
     * Pass camera parameters to renderer
     */
    private void updateCameraInRenderer() {
        if (renderer != null) {
            renderer.setCamera(cameraAzimuth, cameraElevation, cameraDistance);
        }
    }
    
    /**
     * Create HUD overlay that sits on top of OpenGL canvas
     */
    private void createHUDOverlay(String cityName) {
        hudOverlay = new JPanel();
        hudOverlay.setLayout(new BoxLayout(hudOverlay, BoxLayout.Y_AXIS));
        hudOverlay.setOpaque(false);
        hudOverlay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        hudOverlay.setMaximumSize(new Dimension(180, 180));
        hudOverlay.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create HUD labels
        positionLabel = createHUDLabel("X: 0.0, Y: 0.0", Color.GREEN);
        statusLabel = createHUDLabel("Status: ACTIVE", Color.CYAN);
        terrainLabel = createHUDLabel("Terrain: LAND", Color.WHITE);
        callsignLabel = createHUDLabel("Callsign: N/A", Color.YELLOW);
        cityLabel = createHUDLabel("City: " + cityName, Color.WHITE);

        hudOverlay.add(positionLabel);
        hudOverlay.add(Box.createVerticalStrut(5));
        hudOverlay.add(statusLabel);
        hudOverlay.add(Box.createVerticalStrut(5));
        hudOverlay.add(terrainLabel);
        hudOverlay.add(Box.createVerticalStrut(5));
        hudOverlay.add(callsignLabel);
        hudOverlay.add(Box.createVerticalStrut(5));
        hudOverlay.add(cityLabel);
    }
    
    /**
     * Create styled HUD label
     */
    private JLabel createHUDLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.BOLD, 14));
        label.setForeground(color);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 180)); // Semi-transparent black
        label.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    /**
     * Start update timer to refresh scene and HUD
     */
    private void startUpdateTimer() {
        updateTimer = new Timer(50, e -> {
            updateHUD();
            updateRendererData();
            repaint(); // Trigger OpenGL redraw
        });
        updateTimer.start();
    }
    
    /**
     * Stop update timer
     */
    public void stopUpdateTimer() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }
    
    /**
     * Update HUD labels with current flight data
     */
    private void updateHUD() {
        if (flight == null) return;
        
        double x = flight.getX();
        double y = flight.getY();
        Point dest = flight.getDestination();
        
        // Update position
        positionLabel.setText(String.format("X: %.1f, Y: %.1f", x, y));
        
        // Update status
        String status = "ACTIVE";
        if (flightStates != null && flightStates.containsKey(flight)) {
            JetPackFlightState state = flightStates.get(flight);
            if (state.isParked()) {
                status = "PARKED";
                statusLabel.setForeground(Color.GRAY);
            } else {
                statusLabel.setForeground(Color.CYAN);
            }
        }
        if (flight.isEmergencyHalt()) {
            status = "EMERGENCY";
            statusLabel.setForeground(Color.RED);
        }
        statusLabel.setText("Status: " + status);
        
        // Update terrain
        String terrain = "LAND 🌲";
        Color terrainColor = Color.GREEN;
        if (cityModel.isWater(x, y)) {
            terrain = "WATER 🌊";
            terrainColor = Color.CYAN;
        } else if ("BUILDING".equals(cityModel.getTerrainType(x, y))) {
            terrain = "BUILDING 🏢";
            terrainColor = Color.GRAY;
        }
        terrainLabel.setText("Terrain: " + terrain);
        terrainLabel.setForeground(terrainColor);
        
        // Update callsign
        if (flight.getJetpack() != null) {
            callsignLabel.setText("Callsign: " + flight.getJetpack().getCallsign());
        }
    }
    
    /**
     * Update renderer with current scene data
     */
    private void updateRendererData() {
        if (renderer == null || flight == null) {
            System.err.println("[JOGL3DPanel] Renderer or flight is null!");
            return;
        }
        // ...existing code...
            // Ensure renderer receives up-to-date flight, destination, and path data
            renderer.updateData(flight, cityModel, allFlights, null, flightStates);
    }
    
    @Override
    public void removeNotify() {
        stopUpdateTimer();
        super.removeNotify();
    }
}
