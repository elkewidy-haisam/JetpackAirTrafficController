/**
 * RadioMessageFormatter.java
 * by Haisam Elkewidy
 *
 * Formats radio messages for display and logging.
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
