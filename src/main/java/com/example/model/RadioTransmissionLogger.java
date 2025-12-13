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
    /** Field */
    private final List<String> transmissions;

    public RadioTransmissionLogger() {
        this.transmissions = new ArrayList<>();
    }

    public void logTransmission(String transmission) {
        transmissions.add(transmission);
    }

    public List<String> getTransmissions() {
        return transmissions;
    }
}
