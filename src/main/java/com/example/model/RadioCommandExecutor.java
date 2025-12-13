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
     * Default constructor for RadioCommandExecutor.
     * No initialization required as this is a stateless utility class.
     */
    public RadioCommandExecutor() {
        // Default constructor with no state to initialize
    }

    /**
     * Executes a command by transmitting it via radio.
     * Only executes if radio is non-null and enabled.
     * 
     * @param command Command string to execute
     * @param radio Radio instance to use for transmission
     */
    public void executeCommand(String command, Radio radio) {
        // Validate radio exists and is enabled before attempting transmission
        if (radio != null && radio.isEnabled()) {
            // Transmit command execution message on radio frequency
            radio.transmit("Executing command: " + command);
        }
    }
}
