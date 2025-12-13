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
    /** The radio frequency this transceiver operates on (e.g., "121.5" for emergency) */
    private final String frequency;
    /** Whether this radio is currently enabled and able to transmit */
    private boolean enabled;

    /**
     * Constructs a new Radio on the specified frequency.
     * Radio is initially enabled.
     * 
     * @param frequency the operating frequency for this radio
     */
    public Radio(String frequency) {
        this.frequency = frequency;  // Store the radio frequency
        this.enabled = true;         // Enable the radio initially
    }

    /**
     * Returns the frequency this radio operates on.
     * @return the frequency string
     */
    public String getFrequency() {
        return frequency;  // Return the stored frequency
    }

    /**
     * Checks if this radio is currently enabled.
     * @return true if enabled, false if disabled
     */
    public boolean isEnabled() {
        return enabled;  // Return the enabled status
    }

    /**
     * Sets the enabled status of this radio.
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;  // Update the enabled status
    }

    /**
     * Transmits a message on this radio frequency.
     * Message is only sent if radio is enabled.
     * 
     * @param message the message to transmit
     */
    public void transmit(String message) {
        if (enabled) {  // Check if radio is enabled
            System.out.println("[Radio " + frequency + "] " + message);  // Print the transmission
        }
    }

    /**
     * Returns a string representation of this radio.
     * @return formatted string with frequency and enabled status
     */
    @Override
    public String toString() {
        return String.format("Radio[frequency=%s, enabled=%s]", frequency, enabled);  // Format and return radio info
    }
}
