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

/**
 * Executor class for processing and transmitting radio commands.
 * Handles command validation and transmission via radio equipment.
 */
public class RadioCommandExecutor {
    
    /**
     * Constructs a new RadioCommandExecutor.
     * No special initialization required.
     */
    public RadioCommandExecutor() {
        // Default constructor with no initialization needed
    }

    /**
     * Executes a radio command by transmitting it via specified radio.
     * Validates radio availability before transmission.
     *
     * @param command The command string to execute/transmit
     * @param radio The Radio equipment to use for transmission
     */
    public void executeCommand(String command, Radio radio) {
        // Check if radio exists and is enabled before transmission
        if (radio != null && radio.isEnabled()) {
            // Transmit command with execution prefix for logging/tracking
            radio.transmit("Executing command: " + command);
        }
    }
}
