/**
 * Logs and maintains a chronological record of all radio transmissions for audit and replay purposes.
 * 
 * Purpose:
 * Captures every radio transmission with timestamps for debugging, compliance, and incident analysis.
 * Maintains an in-memory log of all messages broadcast through the radio system, providing methods to
 * retrieve transmission history and track whether the radio is actively transmitting.
 * 
 * Key Responsibilities:
 * - Log all outgoing radio transmissions with timestamps
 * - Maintain chronological list of transmission records
 * - Track transmission state (transmitting vs. idle)
 * - Provide access to transmission history for replay/analysis
 * - Format log entries with HH:mm:ss timestamps
 * 
 * Interactions:
 * - Used by Radio to log every broadcast message
 * - Provides transmission history to CityLogManager for persistent storage
 * - Supports RadarTapeWindow for displaying communication history
 * - Enables incident investigation by replaying radio communications
 * 
 * Patterns & Constraints:
 * - Simple append-only logger (no deletion or modification)
 * - In-memory storage only (no persistence - CityLogManager handles that)
 * - Uses LocalTime for timestamp formatting
 * - Unbounded list growth - may require periodic clearing in long sessions
 * - Thread-safety not guaranteed - external synchronization required
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
    private List<String> transmissionLog;
    private boolean isTransmitting;
    
    public RadioTransmissionLogger() {
        this.transmissionLog = new ArrayList<>();
        this.isTransmitting = false;
    }
    
    /**
     * Transmits a message (simulates radio transmission)
     */
    public void transmit(String message) {
        isTransmitting = true;
        // Simulate transmission delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        isTransmitting = false;
    }
    
    /**
     * Logs a transmission to the transmission log
     */
    public void logTransmission(String message) {
        String timestamp = LocalTime.now().toString();
        transmissionLog.add(timestamp + " - " + message);
    }
    
    /**
     * Transmits and logs a message in one operation
     */
    public void transmitAndLog(String message) {
        transmit(message);
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
