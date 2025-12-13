/**
 * RadioCommandExecutor component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides radiocommandexecutor functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core radiocommandexecutor operations
 * - Maintain necessary state for radiocommandexecutor functionality
 * - Integrate with related system components
 * - Support queries and updates as needed
 * 
 * Interactions:
 * - Referenced by controllers and managers
 * - Integrates with data models and services
 * - Coordinates with UI components where applicable
 * 
 * Patterns & Constraints:
 * - Follows system architecture conventions
 * - Thread-safe where concurrent access expected
 * - Minimal external dependencies
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class RadioCommandExecutor {
    /**
     * Constructs a new RadioCommandExecutor instance.
     * Initializes with default settings, ready to process and execute
     * radio commands received from the control tower.
     */
    public RadioCommandExecutor() {
        // Default constructor - no initialization needed for stateless executor
        // Future enhancements could include command history tracking or validation rules
    }

    /**
     * Executes a radio command by transmitting it through the specified radio system.
     * Validates that the radio is both non-null and enabled before attempting transmission.
     * This method acts as a command processor, wrapping the command in execution metadata
     * before broadcasting to the radio channel.
     * 
     * @param command The command string to execute (e.g., "LAND", "HOLD", "PROCEED")
     * @param radio The Radio instance through which to transmit the command
     */
    public void executeCommand(String command, Radio radio) {
        // Validate radio availability - null check prevents NullPointerException
        if (radio != null && radio.isEnabled()) {
            // Wrap command with execution prefix for logging and acknowledgment tracking
            // This helps operators distinguish between raw commands and executed directives
            radio.transmit("Executing command: " + command);
        }
        // If radio is null or disabled, command is silently ignored
        // Future enhancement: could log failed execution attempts or throw exception
    }
}
