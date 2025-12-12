/**
 * CityTimerManager.java
 * by Haisam Elkewidy
 *
 * This class handles CityTimerManager functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - weatherTimer (Timer)
 *   - dateTimeTimer (Timer)
 *   - animationTimer (Timer)
 *
 * Methods:
 *   - startWeatherTimer(callback)
 *   - startDateTimeTimer(callback)
 *   - stopWeatherTimer()
 *   - stopDateTimeTimer()
 *   - stopAnimationTimer()
 *   - stopAllTimers()
 *
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
     * Starts the weather timer to update weather every 30 seconds
     * 
     * @param callback The action to perform on each timer tick
     */
    public void startWeatherTimer(Runnable callback) {
        stopWeatherTimer(); // Stop existing timer if any
        
        weatherTimer = new Timer(30000, e -> callback.run());
        weatherTimer.start();
    }
    
    /**
     * Starts the date/time timer to update every second
     * 
     * @param callback The action to perform on each timer tick
     */
    public void startDateTimeTimer(Runnable callback) {
        stopDateTimeTimer(); // Stop existing timer if any
        
        dateTimeTimer = new Timer(1000, e -> callback.run());
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
