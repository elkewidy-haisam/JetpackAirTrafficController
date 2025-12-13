/**
 * Centralized management for timer operations and lifecycle coordination.
 * 
 * Purpose:
 * Manages timer instances across the Air Traffic Controller system, providing
 * factory methods, registry access, and coordination logic. Supports multi-city scenarios
 * and ensures consistent timer state across operational contexts.
 * 
 * Key Responsibilities:
 * - Initialize and maintain timer collections per city or system-wide
 * - Provide query methods for timer retrieval and filtering
 * - Coordinate timer state updates across subsystems
 * - Support timer lifecycle (creation, modification, disposal)
 * 
 * Interactions:
 * - Referenced by AirTrafficControllerFrame and CityMapPanel
 * - Integrates with logging and persistence subsystems
 * - Coordinates with related manager classes
 * 
 * Patterns & Constraints:
 * - Manager pattern centralizes timer concerns
 * - Thread-safe operations for concurrent access
 * - Per-city collections for multi-city support
 * 
 * @author Haisam Elkewidy
 */

package com.example.manager;

import javax.swing.Timer;

/**
 * CityTimerManager.java
 * 
 * Manages all timers for city monitoring:
 * - Weather update timer (30 second intervals)
 * - Date/time update timer (1 second intervals)
 * - Animation timer (controlled externally)
 */
public class CityTimerManager {
    
    // Timer for periodic weather updates (30 second intervals)
    private Timer weatherTimer;
    
    // Timer for date/time display updates (1 second intervals)
    private Timer dateTimeTimer;
    
    // Timer for animation frame updates (externally managed)
    private Timer animationTimer;
    
    /**
     * Starts the weather timer to update weather conditions every 30 seconds.
     * Stops any existing weather timer before creating new one to prevent duplicates.
     * 
     * @param callback Runnable to execute on each timer tick (typically weather update logic)
     */
    public void startWeatherTimer(Runnable callback) {
        // Stop existing timer if any to prevent multiple concurrent timers
        stopWeatherTimer();
        
        // Create new timer with 30 second delay (30000ms)
        weatherTimer = new Timer(30000, e -> callback.run());
        
        // Start timer - begins firing immediately
        weatherTimer.start();
    }
    
    /**
     * Starts the date/time timer to update display every second.
     * Stops any existing date/time timer before creating new one to prevent duplicates.
     * 
     * @param callback Runnable to execute on each timer tick (typically display update logic)
     */
    public void startDateTimeTimer(Runnable callback) {
        // Stop existing timer if any to prevent multiple concurrent timers
        stopDateTimeTimer();
        
        // Create new timer with 1 second delay (1000ms)
        dateTimeTimer = new Timer(1000, e -> callback.run());
        
        // Start timer - begins firing immediately
        dateTimeTimer.start();
    }
    
    /**
     * Stops the weather timer
     */
    public void stopWeatherTimer() {
        if (weatherTimer != null && weatherTimer.isRunning()) {
            weatherTimer.stop();
        }
    }
    
    /**
     * Stops the date/time timer
     */
    public void stopDateTimeTimer() {
        if (dateTimeTimer != null && dateTimeTimer.isRunning()) {
            dateTimeTimer.stop();
        }
    }
    
    /**
     * Stops the animation timer
     */
    public void stopAnimationTimer() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }
    
    /**
     * Stops all timers
     */
    public void stopAllTimers() {
        stopWeatherTimer();
        stopDateTimeTimer();
        stopAnimationTimer();
    }
    
    /**
     * Sets the animation timer reference (managed externally)
     * 
     * @param timer The animation timer
     */
    public void setAnimationTimer(Timer timer) {
        this.animationTimer = timer;
    }
    
    /**
     * Gets the weather timer
     * 
     * @return The weather timer
     */
    public Timer getWeatherTimer() {
        return weatherTimer;
    }
    
    /**
     * Gets the date/time timer
     * 
     * @return The date/time timer
     */
    public Timer getDateTimeTimer() {
        return dateTimeTimer;
    }
    
    /**
     * Gets the animation timer
     * 
     * @return The animation timer
     */
    public Timer getAnimationTimer() {
        return animationTimer;
    }
}
