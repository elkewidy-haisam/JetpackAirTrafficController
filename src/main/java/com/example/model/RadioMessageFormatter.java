/**
 * RadioMessageFormatter component for the Air Traffic Controller system.
 * 
 * Purpose:
 * Provides radiomessageformatter functionality within the jetpack air traffic control application.
 * Supports operational requirements through specialized methods and state management.
 * 
 * Key Responsibilities:
 * - Implement core radiomessageformatter operations
 * - Maintain necessary state for radiomessageformatter functionality
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
 * Formatter class for radio message display and logging.
 * Converts RadioMessage objects into human-readable formatted strings.
 */
public class RadioMessageFormatter {
    
    /**
     * Constructs a new RadioMessageFormatter.
     * No special initialization required.
     */
    public RadioMessageFormatter() {
        // Default constructor with no initialization needed
    }

    /**
     * Formats a radio message into a standardized display string.
     * Includes message type, sender, receiver, and content in bracketed format.
     *
     * @param message The RadioMessage to format
     * @return Formatted string representation of the message
     */
    public String format(RadioMessage message) {
        // Handle null message with error indicator
        if (message == null) return "[Invalid Message]";
        // Format as: [TYPE] From: SENDER | To: RECEIVER | CONTENT
        return String.format("[%s] From: %s | To: %s | %s", message.getType(), message.getSender(), message.getReceiver(), message.getContent());
    }
}
