/**
 * Duplicate/alias of com.example.radio.RadioCommandExecutor for backwards compatibility.
 * 
 * Purpose:
 * Model package version of RadioCommandExecutor. Provides identical radio command execution via reflection.
 * Maintained for backwards compatibility with code that imports from model package instead of radio package.
 * May be deprecated and consolidated with radio package version in future refactoring.
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class RadioCommandExecutor {
    /**
     * Default constructor for RadioCommandExecutor.
     * No initialization logic required - executor is stateless.
     */
    public RadioCommandExecutor() {
        // Default constructor
        // No state to initialize
    }

    /**
     * Executes a radio command by transmitting it over the specified radio.
     * Only transmits if radio is non-null and enabled. Silently ignores
     * commands if radio is null or disabled for safety.
     * 
     * @param command the command string to execute and transmit
     * @param radio the Radio object to transmit the command through
     */
    public void executeCommand(String command, Radio radio) {
        if (radio != null && radio.isEnabled()) {  // Check radio exists and is enabled
            // Transmit command execution confirmation message over radio
            radio.transmit("Executing command: " + command);
        }
        // If radio is null or disabled, command is silently dropped for safety
    }
}
