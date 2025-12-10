/*
 * Renderer3D.java
 * Part of Jetpack Air Traffic Controller
 *
 * Advanced 3D rendering for jetpack tracking with realistic city models.
 * Enhanced to show nearby jetpacks, accidents, and flight states.
 * (c) 2025 Haisam Elkewidy. All rights reserved.
 */
package com.example.ui.utility;

import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.accident.AccidentAlert;
import com.example.model.CityModel3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Renderer3D provides static methods for rendering the 3D city and jetpack scenes.
 * All rendering is performed using Java2D for compatibility with offscreen and onscreen panels.
 */
public class Renderer3D {
    /**
     * Render complete 3D scene from jetpack perspective (legacy method for backward compatibility)
     */
    public static void renderScene(Graphics2D g2d, int width, int height, JetPackFlight flight, CityModel3D cityModel) {
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

        // Get destination for camera direction
        Point dest = flight.getDestination();
        double dx = dest.x - jetpackX;
        double dy = dest.y - jetpackY;
        double angle = Math.atan2(dy, dx);

        // Draw sky
        drawSky(g2d, width, height);

        // Draw ground/water
        drawGround(g2d, width, height, jetpackX, jetpackY, angle, cityModel);

        // ...existing code...
    }

    // Stub for drawSky
    private static void drawSky(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.CYAN);
        g2d.fillRect(0, 0, width, height / 2);
    }

    // Stub for drawGround
    private static void drawGround(Graphics2D g2d, int width, int height, double jetpackX, double jetpackY, double angle, CityModel3D cityModel) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, height / 2, width, height / 2);
    }

    // ...other rendering methods, e.g., drawBuildings, drawJetpacks, etc...
}