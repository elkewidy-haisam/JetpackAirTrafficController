/*
 * RadioTransmissionLogger.java
 * Part of Jetpack Air Traffic Controller
 *
 * Logs radio transmissions for jetpack flights.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
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
