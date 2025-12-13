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

public class RadioMessageFormatter {
    /**
     * Default constructor for RadioMessageFormatter.
     * No initialization required as this is a stateless utility class.
     */
    public RadioMessageFormatter() {
        // Default constructor with no state to initialize
    }

    /**
     * Formats a RadioMessage into a human-readable string.
     * 
     * @param message RadioMessage to format
     * @return Formatted string with type, sender, receiver, and content
     */
    public String format(RadioMessage message) {
        // Check if message is null to prevent NullPointerException
        if (message == null) return "[Invalid Message]";
        // Format message as "[TYPE] From: SENDER | To: RECEIVER | CONTENT"
        return String.format("[%s] From: %s | To: %s | %s", message.getType(), message.getSender(), message.getReceiver(), message.getContent());
    }
}
