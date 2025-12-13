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
     * Constructs a new RadioTransmissionLogger with empty transmission log.
     * Initializes the transmissions list to store radio messages.
     */
    public RadioTransmissionLogger() {
        this.transmissions = new ArrayList<>();  // Initialize empty list for transmission storage
    }

    /**
     * Logs a radio transmission message to the transmission history.
     * Appends the message to the end of the transmissions list with implicit timestamp ordering.
     * 
     * @param transmission the radio transmission message to log
     */
    public void logTransmission(String transmission) {
        transmissions.add(transmission);  // Append transmission to the log list
    }

    /**
     * Returns all logged radio transmissions in chronological order.
     * Returns the actual list (not a copy), allowing external modification.
     * 
     * @return List of all logged transmission strings
     */
    public List<String> getTransmissions() {
        return transmissions;  // Return the transmission log list
    }
}
