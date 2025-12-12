/**
 * RadioTransmissionLogger.java
 * by Haisam Elkewidy
 *
 * Logs radio transmissions for jetpack flights.
 */

package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class RadioTransmissionLogger {
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
