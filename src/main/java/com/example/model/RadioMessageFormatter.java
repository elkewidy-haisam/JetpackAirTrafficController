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
     * Constructs a new RadioMessageFormatter instance.
     * Initializes the formatter with default settings for message presentation.
     * The formatter uses a standardized template for all message types.
     */
    public RadioMessageFormatter() {
        // Default constructor - no state required for stateless formatting operations
        // Future enhancements could include configurable format templates or locale settings
    }

    /**
     * Formats a RadioMessage into a human-readable string for display and logging.
     * Applies a standardized template that includes message type, sender, receiver, and content.
     * Handles null messages gracefully by returning a safe placeholder string.
     * 
     * @param message The RadioMessage to format
     * @return Formatted string representation, or "[Invalid Message]" if message is null
     */
    public String format(RadioMessage message) {
        // Null-safety check - prevents NullPointerException and provides clear error indication
        if (message == null) return "[Invalid Message]";
        
        // Format message using standardized template: [TYPE] From: SENDER | To: RECEIVER | CONTENT
        // This structure provides quick visual scanning in logs and radio transcript panels
        return String.format("[%s] From: %s | To: %s | %s", 
                           message.getType(),      // Message classification (e.g., CLEARANCE, ADVISORY)
                           message.getSender(),    // Originating station or callsign
                           message.getReceiver(),  // Destination callsign or broadcast indicator
                           message.getMessage());  // Actual message payload
    }
}
