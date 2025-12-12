/**
 * RadioTransmissionLogger.java
 * by Haisam Elkewidy
 *
 * This class handles RadioTransmissionLogger functionality in the Air Traffic Controller system.
 *
 * Variables:
 *   - transmissions (List<String>)
 *
 * Methods:
 *   - RadioTransmissionLogger()
 *   - logTransmission(transmission)
 *
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
