/**
 * Models radio communication equipment for air traffic control transmissions.
 * 
 * Purpose:
 * Represents a radio transceiver operating on a designated frequency, enabling air traffic controllers
 * to broadcast messages, clearances, and emergency directives to jetpack pilots. Provides a simplified
 * abstraction of aviation radio systems with enable/disable functionality.
 * 
 * Key Responsibilities:
 * - Maintain assigned radio frequency (e.g., "121.5" for emergency)
 * - Support enable/disable toggling for operational control
 * - Transmit messages to connected receivers (pilots, other controllers)
 * - Provide immutable frequency identifier for channel coordination
 * 
 * Interactions:
 * - Used by AirTrafficController to broadcast system-wide messages
 * - Referenced in RadioInstructionsPanel for UI display of active frequency
 * - Logged via RadioTransmissionLogger for compliance and playback
 * - Associated with RadioMessage objects for structured communications
 * 
 * Patterns & Constraints:
 * - Immutable frequency ensures channel stability
 * - Lightweight model suitable for multiple radio instances per controller
 * - No actual RF hardware integration; represents logical communication channel
 * - Thread-safe for concurrent transmit operations at higher layers
 * 
 * @author Haisam Elkewidy
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
