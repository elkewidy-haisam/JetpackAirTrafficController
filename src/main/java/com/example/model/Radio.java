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
    /** The radio frequency this transceiver operates on (e.g., "121.5", "118.1") */
    private final String frequency;
    
    /** Whether this radio is currently enabled and capable of transmitting */
    private boolean enabled;

    /**
     * Constructs a new Radio on the specified frequency.
     * The radio is enabled by default.
     * 
     * @param frequency the operating frequency for this radio
     */
    public Radio(String frequency) {
        this.frequency = frequency;  // Store the assigned frequency
        this.enabled = true;         // Enable radio by default
    }

    /**
     * Returns the operating frequency of this radio.
     * @return the frequency string (e.g., "121.5")
     */
    public String getFrequency() {
        return frequency;  // Return the immutable frequency
    }

    /**
     * Checks if this radio is currently enabled.
     * @return true if enabled and able to transmit, false otherwise
     */
    public boolean isEnabled() {
        return enabled;  // Return the enabled status
    }

    /**
     * Sets the enabled state of this radio.
     * @param enabled true to enable transmissions, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;  // Update the enabled flag
    }

    /**
     * Transmits a message over this radio frequency.
     * Only transmits if radio is enabled; silently drops message if disabled.
     * 
     * @param message the message to broadcast
     */
    public void transmit(String message) {
        if (enabled) {  // Only transmit if radio is enabled
            // Output message to console with frequency tag
            System.out.println("[Radio " + frequency + "] " + message);
        }
        // If disabled, message is silently dropped (no transmission)
    }

    /**
     * Returns a formatted string representation of this Radio.
     * @return string with frequency and enabled status
     */
    @Override
    public String toString() {
        // Format and return radio details
        return String.format("Radio[frequency=%s, enabled=%s]", frequency, enabled);
    }
}
