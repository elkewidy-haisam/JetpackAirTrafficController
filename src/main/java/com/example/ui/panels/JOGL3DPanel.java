/**
 * JOGL3DPanel.java
 * by Haisam Elkewidy
 *
 * This class handles JOGL3DPanel functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - allFlights (List<JetPackFlight>)
 *   - renderer (JOGLRenderer3D)
 *   - glPanel (GLJPanel)
 *   - flight (JetPackFlight)
 *   - cityModel (CityModel3D)
 *   - flightStates (Map<JetPackFlight, JetPackFlightState>)
 *   - updateTimer (Timer)
 *   - hudOverlay (JPanel)
 *   - positionLabel (JLabel)
 *   - statusLabel (JLabel)
 *   - ... and 7 more
 *
 * Methods:
 *   - JOGL3DPanel(cityName, flight, allFlights, Map<JetPackFlight, flightStates, cityMap)
 *   - hideJetpack(flight)
 *   - showJetpack(flight)
 *   - registerPanel(panel)
 *   - unregisterPanel(panel)
 *   - stopUpdateTimer()
 *   - removeNotify()
 *
 */

/*
 * JOGL3DPanel.java
 * Hardware-accelerated 3D panel using JOGL for OpenGL rendering. Drop-in replacement for MapTrackingPanel.
 *
 * Last updated: December 10, 2025
 * Author: Jetpack Air Traffic Controller Team
 */
package com.example.ui.panels;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.model.CityModel3D;
import com.example.ui.utility.JOGLRenderer3D;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

/**
 * JOGL3DPanel - Hardware-accelerated 3D panel using JOGL for OpenGL rendering.
 * Drop-in replacement for MapTrackingPanel.
 */
public class JOGL3DPanel extends JPanel {
        private List<JetPackFlight> allFlights;
    // Methods to filter jetpacks
    public void hideJetpack(JetPackFlight flight) {
        visibleJetpacks.remove(flight);
        repaint();
    }
    public void showJetpack(JetPackFlight flight) {
        visibleJetpacks.add(flight);
        repaint();
    }
    public void setVisibleJetpacks(java.util.Collection<JetPackFlight> flights) {
        visibleJetpacks.clear();
        visibleJetpacks.addAll(flights);
        repaint();
    }
    // Shared registry for all open JOGL3DPanel instances
    private static final java.util.Set<JOGL3DPanel> openPanels = new java.util.HashSet<>();
    public static void registerPanel(JOGL3DPanel panel) {
        openPanels.add(panel);
    }
    public static void unregisterPanel(JOGL3DPanel panel) {
        openPanels.remove(panel);
    }
    public static java.util.Set<JOGL3DPanel> getOpenPanels() {
        return new java.util.HashSet<>(openPanels);
    }

    private JOGLRenderer3D renderer;
    private GLJPanel glPanel;
    private JetPackFlight flight;
    private CityModel3D cityModel;
    private final java.util.Set<JetPackFlight> visibleJetpacks = new java.util.HashSet<>();
    private Map<JetPackFlight, JetPackFlightState> flightStates;
    private Timer updateTimer;
    
    // HUD overlay (drawn using Swing on top of OpenGL)
    private JPanel hudOverlay;
    private JLabel positionLabel;
    private JLabel statusLabel;
    private JLabel terrainLabel;
    private JLabel callsignLabel;
    private JLabel cityLabel;
    // private JLabel jetpackInfoLabel; // Removed: 2D-style info label

    // Camera control state
    private double cameraAzimuth = 45; // horizontal angle
    private double cameraElevation = 30; // vertical angle
    private double cameraDistance = 400; // zoom
    // Removed unused camera control fields lastMouseX, lastMouseY, dragging
    
    public JOGL3DPanel(String cityName, JetPackFlight flight, 
                   List<JetPackFlight> allFlights,
                   Map<JetPackFlight, JetPackFlightState> flightStates,
                   BufferedImage cityMap) {
        // Removed unused variable dest
        this.flight = flight;
        this.allFlights = allFlights;
        this.flightStates = flightStates;
        this.cityModel = new CityModel3D(cityName, cityMap);
        registerPanel(this);

        // By default, all jetpacks are visible
        visibleJetpacks.addAll(allFlights);

        // Create JOGL renderer and GLJPanel
        this.renderer = new JOGLRenderer3D();
        glPanel = new GLJPanel(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
        glPanel.addGLEventListener(renderer);
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
        hudOverlay.setMaximumSize(new Dimension(260, 220));
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

        // Detailed 2D-style info label removed; no additional HUD text here.
    }
    
    /**
     * Update renderer with current scene data
     */
    private void updateRendererData() {
        if (renderer == null || flight == null) {
            System.err.println("[JOGL3DPanel] Renderer or flight is null!");
            return;
        }
        // Ensure renderer receives up-to-date flight, destination, and path data
        renderer.updateData(flight, cityModel, new ArrayList<>(visibleJetpacks), null, flightStates);
    }

    
    /**
     * Getter for GLJPanel
     */
    public GLJPanel getGLPanel() {
        return glPanel;
    }

    /**
     * Getter for JOGLRenderer3D
     */
    public JOGLRenderer3D getRenderer() {
        return renderer;
    }

    @Override
    public void removeNotify() {
        stopUpdateTimer();
        if (glPanel != null) {
            glPanel.destroy();
        }
        unregisterPanel(this);
        super.removeNotify();
    }
}
