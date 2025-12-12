/**
 * Radio.java
 * by Haisam Elkewidy
 *
 * This class handles radio communications between Air Traffic Control and jetpack pilots.
 *
 * Variables:
 *   - frequency (String)
 *   - enabled (boolean)
 *
 * Methods:
 *   - Radio(frequency)
 *   - transmit(message)
 *   - toString()
 *
 */

package com.example.model;

public class Radio {
    private final String frequency;
    private boolean enabled;

    public Radio(String frequency) {
        this.frequency = frequency;
        this.enabled = true;
    }

    public String getFrequency() {
        return frequency;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void transmit(String message) {
        if (enabled) {
            System.out.println("[Radio " + frequency + "] " + message);
        }
    }

    @Override
    public String toString() {
        return String.format("Radio[frequency=%s, enabled=%s]", frequency, enabled);
    }
}
