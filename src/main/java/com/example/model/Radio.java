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

/**
 * Radio communication equipment model for air traffic control.
 * Represents a transceiver operating on a specific frequency.
 */
public class Radio {
    // Immutable radio frequency (e.g., "121.5" for emergency channel)
    private final String frequency;
    // Flag indicating if radio is currently enabled for transmission
    private boolean enabled;

    /**
     * Constructs a new Radio on specified frequency.
     * Radio is enabled by default upon creation.
     *
     * @param frequency Radio frequency identifier string
     */
    public Radio(String frequency) {
        // Assign immutable frequency
        this.frequency = frequency;
        // Enable radio by default
        this.enabled = true;
    }

    /**
     * Gets the radio frequency.
     *
     * @return Frequency string
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Checks if radio is currently enabled.
     *
     * @return true if radio is enabled for transmission
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled state of the radio.
     *
     * @param enabled New enabled state
     */
    public void setEnabled(boolean enabled) {
        // Update radio enabled/disabled state
        this.enabled = enabled;
    }

    /**
     * Transmits a message on this radio frequency.
     * Only transmits if radio is currently enabled.
     *
     * @param message Message content to transmit
     */
    public void transmit(String message) {
        // Check if radio is enabled before transmitting
        if (enabled) {
            // Output message to console with frequency identifier
            System.out.println("[Radio " + frequency + "] " + message);
        }
    }

    /**
     * Returns string representation of radio state.
     *
     * @return Formatted string with frequency and enabled status
     */
    @Override
    public String toString() {
        // Format as: Radio[frequency=X, enabled=Y]
        return String.format("Radio[frequency=%s, enabled=%s]", frequency, enabled);
    }
}
