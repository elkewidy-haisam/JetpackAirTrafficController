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
     * Constructs a new RadioCommandExecutor.
     * No special initialization required.
     */
    public RadioCommandExecutor() {
        // Default constructor - no initialization needed
    }

    /**
     * Executes a command by transmitting it via the specified radio.
     * Command is only executed if radio is not null and is enabled.
     * 
     * @param command the command string to execute
     * @param radio the Radio to use for transmission
     */
    public void executeCommand(String command, Radio radio) {
        if (radio != null && radio.isEnabled()) {  // Verify radio is available and enabled
            radio.transmit("Executing command: " + command);  // Transmit the command execution message
        }
    }
}
