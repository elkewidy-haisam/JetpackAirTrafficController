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
    /** List storing all logged radio transmissions with timestamps */
    private List<String> transmissionLog;
    /** Flag indicating whether radio is currently in transmitting state */
    private boolean isTransmitting;
    
    /**
     * Constructs a new RadioTransmissionLogger.
     * Initializes empty transmission log and inactive transmission state.
     */
    public RadioTransmissionLogger() {
        this.transmissionLog = new ArrayList<>();  // Initialize empty transmission log
        this.isTransmitting = false;              // Set initial state to not transmitting
    }
    
    /**
     * Transmits a message (simulates radio transmission).
     * Blocks for 100ms to simulate transmission delay.
     */
    public void transmit(String message) {
        isTransmitting = true;  // Mark as actively transmitting
        // Simulate transmission delay
        try {
            Thread.sleep(100);  // Wait 100ms to simulate radio transmission time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restore interrupt status
        }
        isTransmitting = false;  // Mark transmission as complete
    }
    
    /**
     * Logs a transmission to the transmission log.
     * Adds current timestamp in HH:mm:ss format.
     * 
     * @param message the message text to log
     */
    public void logTransmission(String message) {
        String timestamp = LocalTime.now().toString();      // Get current time as string
        transmissionLog.add(timestamp + " - " + message);   // Add timestamped entry to log
    }
    
    /**
     * Transmits and logs a message in one operation.
     * Convenience method combining transmit() and logTransmission().
     * 
     * @param message the message to transmit and log
     */
    public void transmitAndLog(String message) {
        transmit(message);        // Perform simulated transmission
        logTransmission(message); // Log the transmission with timestamp
    }
    
    /**
     * Gets a copy of the transmission log.
     * Returns defensive copy to prevent external modification.
     * 
     * @return List of timestamped transmission strings
     */
    public List<String> getTransmissionLog() {
        return new ArrayList<>(transmissionLog);  // Return defensive copy of log
    }
    
    /**
     * Clears all entries from the transmission log.
     */
    public void clearTransmissionLog() {
        transmissionLog.clear();  // Remove all log entries
    }
    
    /**
     * Gets the number of logged transmissions.
     * 
     * @return count of transmission log entries
     */
    public int getTransmissionCount() {
        return transmissionLog.size();  // Return number of entries in log
    }
    
    /**
     * Checks if radio is currently transmitting.
     * 
     * @return true if actively transmitting, false otherwise
     */
    public boolean isTransmitting() {
        return isTransmitting;  // Return current transmission state
    }
    
    /**
     * Prints the transmission log to console.
     * Currently a no-op placeholder method.
     */
    public void printLog() {
        // Log cleared - no action taken
    }
}
