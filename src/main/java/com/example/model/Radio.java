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
    // Radio frequency designation (e.g., "121.5", "243.0")
    private final String frequency;
    // Whether radio is currently enabled for transmissions
    private boolean enabled;

    /**
     * Constructs a new Radio on specified frequency.
     * Radio is enabled by default.
     * 
     * @param frequency Radio frequency designation
     */
    public Radio(String frequency) {
        // Store the radio frequency identifier
        this.frequency = frequency;
        // Initialize radio as enabled for transmissions
        this.enabled = true;
    }

    /**
     * Returns the radio frequency designation.
     * 
     * @return Frequency string
     */
    public String getFrequency() {
        // Return stored frequency designation
        return frequency;
    }

    /**
     * Returns whether radio is currently enabled.
     * 
     * @return true if enabled, false if disabled
     */
    public boolean isEnabled() {
        // Return current enabled status
        return enabled;
    }

    /**
     * Sets whether radio is enabled for transmissions.
     * 
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        // Update enabled status to new value
        this.enabled = enabled;
    }

    /**
     * Transmits a message on this radio frequency.
     * Only transmits if radio is enabled.
     * 
     * @param message Message content to transmit
     */
    public void transmit(String message) {
        // Check if radio is enabled before transmitting
        if (enabled) {
            // Print message to console with frequency prefix
            System.out.println("[Radio " + frequency + "] " + message);
        }
    }

    /**
     * Returns formatted string representation of this radio.
     * 
     * @return String with frequency and enabled status
     */
    @Override
    public String toString() {
        // Format radio details as readable string
        return String.format("Radio[frequency=%s, enabled=%s]", frequency, enabled);
    }
}
