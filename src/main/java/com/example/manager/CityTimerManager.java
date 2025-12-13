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
    
    /** Timer for periodic weather updates (30 second intervals) */
    private Timer weatherTimer;
    /** Timer for date/time display updates (1 second intervals) */
    private Timer dateTimeTimer;
    /** Timer for animation frame updates (variable rate) */
    private Timer animationTimer;
    
    /**
     * Starts the weather timer to update weather every 30 seconds.
     * Stops any existing weather timer before creating new one.
     * 
     * @param callback The action to perform on each timer tick
     */
    public void startWeatherTimer(Runnable callback) {
        stopWeatherTimer();  // Stop existing timer if any to prevent duplicates
        
        // Create new timer with 30 second delay (30000 milliseconds)
        weatherTimer = new Timer(30000, e -> callback.run());  // Execute callback on each tick
        weatherTimer.start();  // Start the timer
    }
    
    /**
     * Starts the date/time timer to update every second.
     * Stops any existing date/time timer before creating new one.
     * 
     * @param callback The action to perform on each timer tick
     */
    public void startDateTimeTimer(Runnable callback) {
        stopDateTimeTimer();  // Stop existing timer if any to prevent duplicates
        
        // Create new timer with 1 second delay (1000 milliseconds)
        dateTimeTimer = new Timer(1000, e -> callback.run());  // Execute callback on each tick
        dateTimeTimer.start();  // Start the timer
    }
    
    /**
     * Stops the weather timer if it is running.
     * Safe to call even if timer is null or not running.
     */
    public void stopWeatherTimer() {
        if (weatherTimer != null && weatherTimer.isRunning()) {  // Check timer exists and is running
            weatherTimer.stop();  // Stop the timer
        }
    }
    
    /**
     * Stops the date/time timer if it is running.
     * Safe to call even if timer is null or not running.
     */
    public void stopDateTimeTimer() {
        if (dateTimeTimer != null && dateTimeTimer.isRunning()) {  // Check timer exists and is running
            dateTimeTimer.stop();  // Stop the timer
        }
    }
    
    /**
     * Stops the animation timer if it is running.
     * Safe to call even if timer is null or not running.
     */
    public void stopAnimationTimer() {
        if (animationTimer != null && animationTimer.isRunning()) {  // Check timer exists and is running
            animationTimer.stop();  // Stop the timer
        }
    }
    
    /**
     * Stops all managed timers (weather, date/time, animation).
     * Ensures clean shutdown when switching cities or closing application.
     */
    public void stopAllTimers() {
        stopWeatherTimer();  // Stop weather update timer
        stopDateTimeTimer();  // Stop date/time display timer
        stopAnimationTimer();  // Stop animation frame timer
    }
    
    /**
     * Sets the animation timer reference (managed externally).
     * Animation timer is typically created by CityMapAnimationController.
     * 
     * @param timer The animation timer to manage
     */
    public void setAnimationTimer(Timer timer) {
        this.animationTimer = timer;  // Store animation timer reference
    }
    
    /**
     * Gets the weather timer instance.
     * Returns null if timer hasn't been started yet.
     * 
     * @return The weather timer, or null if not initialized
     */
    public Timer getWeatherTimer() {
        return weatherTimer;  // Return weather timer reference
    }
    
    /**
     * Gets the date/time timer instance.
     * Returns null if timer hasn't been started yet.
     * 
     * @return The date/time timer, or null if not initialized
     */
    public Timer getDateTimeTimer() {
        return dateTimeTimer;  // Return date/time timer reference
    }
    
    /**
     * Gets the animation timer instance.
     * Returns null if timer hasn't been set yet.
     * 
     * @return The animation timer, or null if not initialized
     */
    public Timer getAnimationTimer() {
        return animationTimer;  // Return animation timer reference
    }
}
