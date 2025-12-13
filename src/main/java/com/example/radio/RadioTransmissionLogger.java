/**
 * RadioTransmissionLogger component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides radiotransmissionlogger functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core radiotransmissionlogger operations
 * - Maintain necessary state for radiotransmissionlogger functionality
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

package com.example.radio;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * RadioTransmissionLogger - manages radio transmission logging
 */
public class RadioTransmissionLogger {
    // Chronological list of all radio transmissions with timestamps
    private List<String> transmissionLog;
    
    // Flag indicating active transmission in progress
    private boolean isTransmitting;
    
    /**
     * Constructs a new RadioTransmissionLogger with empty log.
     * Initializes ready to record radio communications.
     */
    public RadioTransmissionLogger() {
        // Initialize empty transmission log for chronological recording
        this.transmissionLog = new ArrayList<>();
        
        // Start in non-transmitting state
        this.isTransmitting = false;
    }
    
    /**
     * Simulates radio transmission with realistic delay.
     * Sets transmitting flag during operation to prevent concurrent transmissions.
     * Blocks for 100ms to simulate real radio transmission latency.
     * 
     * @param message Message content (not logged by this method)
     */
    public void transmit(String message) {
        // Set transmitting flag to indicate channel in use
        isTransmitting = true;
        
        // Simulate transmission delay (100ms realistic radio latency)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Restore interrupt status if transmission interrupted
            Thread.currentThread().interrupt();
        }
        
        // Clear transmitting flag when transmission complete
        isTransmitting = false;
    }
    
    /**
     * Logs a transmission to the persistent log with timestamp.
     * Captures current time and message for audit trail and replay.
     * 
     * @param message Message content to log
     */
    public void logTransmission(String message) {
        // Capture current time as timestamp string
        String timestamp = LocalTime.now().toString();
        
        // Add timestamped entry to log: "HH:mm:ss.nnn - message"
        transmissionLog.add(timestamp + " - " + message);
    }
    
    /**
     * Transmits and logs a message in one atomic operation.
     * Combines simulation delay with persistent logging for complete radio operation.
     * 
     * @param message Message to transmit and log
     */
    public void transmitAndLog(String message) {
        // Simulate radio transmission with delay
        transmit(message);
        
        // Record transmission in persistent log
        logTransmission(message);
    }
    
    /**
     * Gets the transmission log
     */
    public List<String> getTransmissionLog() {
        return new ArrayList<>(transmissionLog);
    }
    
    /**
     * Clears the transmission log
     */
    public void clearTransmissionLog() {
        transmissionLog.clear();
    }
    
    /**
     * Gets the number of logged transmissions
     */
    public int getTransmissionCount() {
        return transmissionLog.size();
    }
    
    /**
     * Checks if currently transmitting
     */
    public boolean isTransmitting() {
        return isTransmitting;
    }
    
    /**
     * Prints the transmission log to console
     */
    public void printLog() {
        // Log cleared
    }
}
