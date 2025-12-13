/**
 * Duplicate/alias of com.example.radio.RadioMessageFormatter for backwards compatibility.
 * 
 * Purpose:
 * Model package version of RadioMessageFormatter. Formats radio messages in aviation standard format.
 * Maintained for backwards compatibility. May be deprecated and removed in favor of radio package version.
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class RadioMessageFormatter {
    /**
     * Constructs a new RadioMessageFormatter.
     * No special initialization required.
     */
    public RadioMessageFormatter() {
        // Default constructor - no initialization needed
    }

    /**
     * Formats a RadioMessage into a standardized string representation.
     * Includes message type, sender, receiver, and content.
     * 
     * @param message the RadioMessage to format
     * @return formatted string, or "[Invalid Message]" if message is null
     */
    public String format(RadioMessage message) {
        if (message == null) return "[Invalid Message]";  // Handle null input
        return String.format("[%s] From: %s | To: %s | %s", message.getType(), message.getSender(), message.getReceiver(), message.getContent());  // Format message with all fields
    }
}
