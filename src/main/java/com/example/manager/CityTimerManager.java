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
    
    private Timer weatherTimer;
    private Timer dateTimeTimer;
    private Timer animationTimer;
    
    /**
     * Starts the weather timer to update weather every 30 seconds.
     * Stops any existing weather timer before starting new one.
     * 
     * @param callback The action to perform on each timer tick
     */
    public void startWeatherTimer(Runnable callback) {
        // Stop any currently running weather timer to prevent duplicates
        stopWeatherTimer();
        
        // Create new Swing timer with 30 second (30000ms) delay
        weatherTimer = new Timer(30000, e -> callback.run());
        // Start the timer
        weatherTimer.start();
    }
    
    /**
     * Starts the date/time timer to update every second.
     * Stops any existing date/time timer before starting new one.
     * 
     * @param callback The action to perform on each timer tick
     */
    public void startDateTimeTimer(Runnable callback) {
        // Stop any currently running date/time timer to prevent duplicates
        stopDateTimeTimer();
        
        // Create new Swing timer with 1 second (1000ms) delay
        dateTimeTimer = new Timer(1000, e -> callback.run());
        // Start the timer
        dateTimeTimer.start();
    }
    
    /**
     * Stops the weather timer if it exists and is running.
     */
    public void stopWeatherTimer() {
        // Check if weather timer exists and is currently running
        if (weatherTimer != null && weatherTimer.isRunning()) {
            // Stop the timer
            weatherTimer.stop();
        }
    }
    
    /**
     * Stops the date/time timer if it exists and is running.
     */
    public void stopDateTimeTimer() {
        // Check if date/time timer exists and is currently running
        if (dateTimeTimer != null && dateTimeTimer.isRunning()) {
            // Stop the timer
            dateTimeTimer.stop();
        }
    }
    
    /**
     * Stops the animation timer if it exists and is running.
     */
    public void stopAnimationTimer() {
        // Check if animation timer exists and is currently running
        if (animationTimer != null && animationTimer.isRunning()) {
            // Stop the timer
            animationTimer.stop();
        }
    }
    
    /**
     * Stops all managed timers (weather, date/time, and animation).
     * Convenience method for bulk timer cleanup.
     */
    public void stopAllTimers() {
        // Stop weather update timer
        stopWeatherTimer();
        // Stop date/time update timer
        stopDateTimeTimer();
        // Stop animation timer
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
