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
     * Default constructor for RadioMessageFormatter.
     * No initialization logic required - formatter is stateless.
     */
    public RadioMessageFormatter() {
        // Default constructor
        // No state to initialize
    }

    /**
     * Formats a RadioMessage into a human-readable string for display.
     * Includes message type, sender, receiver, and content in standardized aviation format.
     * Returns error message if provided RadioMessage is null.
     * 
     * @param message the RadioMessage object to format
     * @return formatted string in format "[TYPE] From: SENDER | To: RECEIVER | CONTENT"
     */
    public String format(RadioMessage message) {
        if (message == null) return "[Invalid Message]";  // Handle null input gracefully
        // Format message with type, sender, receiver, and content fields
        return String.format("[%s] From: %s | To: %s | %s", 
                message.getType(),      // Get and include message type (e.g., "CLEARANCE", "ALERT")
                message.getSender(),    // Get and include sender callsign/ID
                message.getReceiver(),  // Get and include receiver callsign/ID
                message.getContent());  // Get and include message text content
    }
}
