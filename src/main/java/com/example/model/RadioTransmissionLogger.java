/**
 * Duplicate/alias of com.example.radio.RadioTransmissionLogger for backwards compatibility.
 * 
 * Purpose:
 * Model package version of RadioTransmissionLogger. Logs radio transmissions with timestamps.
 * Maintained for backwards compatibility. May be deprecated and removed in favor of radio package version.
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class RadioTransmissionLogger {
    /** List storing all logged radio transmissions in chronological order */
    private final List<String> transmissions;

    /**
     * Constructs a new RadioTransmissionLogger.
     * Initializes empty transmission log.
     */
    public RadioTransmissionLogger() {
        this.transmissions = new ArrayList<>();  // Initialize empty list for transmission storage
    }

    /**
     * Logs a radio transmission to the history.
     * Transmission is appended to the end of the log.
     * 
     * @param transmission the transmission message to log
     */
    public void logTransmission(String transmission) {
        transmissions.add(transmission);  // Append transmission to the log
    }

    /**
     * Returns the list of all logged transmissions.
     * 
     * @return List of transmission strings in chronological order
     */
    public List<String> getTransmissions() {
        return transmissions;  // Return the transmission log
    }
}
