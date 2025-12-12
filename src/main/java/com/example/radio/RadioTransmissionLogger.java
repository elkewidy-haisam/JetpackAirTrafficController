/**
 * RadioTransmissionLogger.java
 * by Haisam Elkewidy
 *
 * RadioTransmissionLogger - manages radio transmission logging
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
