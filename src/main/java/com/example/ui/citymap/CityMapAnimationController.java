/**
 * Controls animation timing and frame updates for city map jetpack movements.
 * 
 * Purpose:
 * Manages animation loop timing, frame rate control, and coordinates updates to all animated elements
 * in the city map display. Ensures smooth 20 FPS animation of jetpack movements.
 * 
 * @author Haisam Elkewidy
 */

package com.example.ui.citymap;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;

import com.example.flight.JetPackFlight;
import com.example.flight.JetPackFlightState;
import com.example.ui.frames.RadarTapeWindow;
import com.example.utility.geometry.GeometryUtils;
import com.example.utility.performance.PerformanceMonitor;
import com.example.weather.Weather;

/**
 * CityMapAnimationController - Manages the animation loop and collision detection
 */
public class CityMapAnimationController {
        // Helper to check if a jetpack is in the process of parking (has a target parking assigned)
        private boolean isParkingInProgress(JetPackFlightState state) {
            if (state == null) return false;
            try {
                java.lang.reflect.Field targetParkingField = state.getClass().getDeclaredField("targetParking");
                targetParkingField.setAccessible(true);
                Object targetParking = targetParkingField.get(state);
                return targetParking != null && !state.isParked();
            } catch (Exception e) {
                return false;
            }
        }
    private javax.swing.Timer animationTimer;
    private final List<JetPackFlight> jetpackFlights;
    private final Map<JetPackFlight, JetPackFlightState> flightStates;
    private final Weather currentWeather;
    private final PerformanceMonitor performanceMonitor;
    private final RadarTapeWindow radarTapeWindow;
    private final CityMapWeatherManager weatherManager;
    
    public CityMapAnimationController(List<JetPackFlight> jetpackFlights,
                                     Map<JetPackFlight, JetPackFlightState> flightStates,
                                     Weather currentWeather,
                                     PerformanceMonitor performanceMonitor,
                                     RadarTapeWindow radarTapeWindow,
                                     CityMapWeatherManager weatherManager) {
        this.jetpackFlights = jetpackFlights;
        this.flightStates = flightStates;
        this.currentWeather = currentWeather;
        this.performanceMonitor = performanceMonitor;
        this.radarTapeWindow = radarTapeWindow;
        this.weatherManager = weatherManager;
    }
    
    /**
     * Starts the animation timer
     */
    public void startAnimation(JPanel mapPanel, int mapWidth, int mapHeight) {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        
        animationTimer = new javax.swing.Timer(40, new ActionListener() {
            private Random rand = new Random();
            private int updateCounter = 0;
            private int collisionCheckCounter = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check for severe weather grounding
                if (currentWeather != null && currentWeather.getCurrentSeverity() >= 5) {
                    // Critical weather - ground all active flights
                    weatherManager.groundAllFlights("CRITICAL WEATHER - " + currentWeather.getCurrentWeather());
                } else if (currentWeather != null && currentWeather.getCurrentSeverity() == 4) {
                    // Severe weather - initiate emergency landings for active flights
                    weatherManager.initiateEmergencyLandings("SEVERE WEATHER - " + currentWeather.getCurrentWeather());
                }
                
                for (JetPackFlight flight : jetpackFlights) {
                    JetPackFlightState state = flightStates.get(flight);
                    
                    if (currentWeather != null) {
                        flight.setInclementWeather(!currentWeather.isSafeToFly());
                    }
                    
                    if (state == null || !state.isParked()) {
                        flight.updatePosition();
                    }
                    
                    // Ensure parking logic is executed for each flight
                    if (state != null) {
                        // ...removed debug output...
                        state.update();
                    }

                    updateCounter++;
                    if (updateCounter >= 18) {
                        if (state != null) {
                            // ...removed debug output...
                            state.update();
                        }
                    }
                    
                    // Only assign a new random destination if not parked and not in the process of parking
                    if (flight.hasReachedDestination() && (state == null || (!state.isParked() && !CityMapAnimationController.this.isParkingInProgress(state)))) {
                        Point newDest = new Point(50 + rand.nextInt(mapWidth - 100), 50 + rand.nextInt(mapHeight - 100));
                        flight.setNewDestination(newDest);
                    }
                }
                
                if (updateCounter >= 18) {
                    updateCounter = 0;
                }
                
                collisionCheckCounter++;
                if (collisionCheckCounter >= 25) {
                    checkCollisions();
                    collisionCheckCounter = 0;
                }
                
                // Update performance monitor
                if (performanceMonitor != null) {
                    performanceMonitor.tick();
                }
                
                mapPanel.repaint();
            }
        });
        animationTimer.start();
    }
    
    /**
     * Stops the animation timer
     */
    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
    
    /**
     * Checks for collisions between jetpack flights
     */
    private void checkCollisions() {
        final double WARNING_DISTANCE = 100.0;
        final double CRITICAL_DISTANCE = 50.0;
        
        for (int i = 0; i < jetpackFlights.size(); i++) {
            JetPackFlight flight1 = jetpackFlights.get(i);
            JetPackFlightState state1 = flightStates.get(flight1);
            
            if (state1 != null && state1.isParked()) continue;
            
            for (int j = i + 1; j < jetpackFlights.size(); j++) {
                JetPackFlight flight2 = jetpackFlights.get(j);
                JetPackFlightState state2 = flightStates.get(flight2);
                
                if (state2 != null && state2.isParked()) continue;
                
                double distance = GeometryUtils.calculateDistance(
                    flight1.getX(), flight1.getY(),
                    flight2.getX(), flight2.getY());
                
                if (distance < CRITICAL_DISTANCE) {
                    if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
                        radarTapeWindow.addMessage("⚠️ CRITICAL: " + flight1.getJetpack().getCallsign() + 
                            " and " + flight2.getJetpack().getCallsign() + 
                            " are " + String.format("%.1f", distance) + " units apart - COLLISION RISK!");
                    }
                } else if (distance < WARNING_DISTANCE) {
                    if (radarTapeWindow != null && radarTapeWindow.isVisible()) {
                        radarTapeWindow.addMessage("⚠️ Warning: " + flight1.getJetpack().getCallsign() + 
                            " and " + flight2.getJetpack().getCallsign() + 
                            " are " + String.format("%.1f", distance) + " units apart");
                    }
                }
            }
        }
    }
}
