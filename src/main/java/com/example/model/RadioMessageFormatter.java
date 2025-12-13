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
    public RadioMessageFormatter() {
        // Default constructor
    }

    public String format(RadioMessage message) {
        if (message == null) return "[Invalid Message]";
        return String.format("[%s] From: %s | To: %s | %s", message.getType(), message.getSender(), message.getReceiver(), message.getContent());
    }
}
