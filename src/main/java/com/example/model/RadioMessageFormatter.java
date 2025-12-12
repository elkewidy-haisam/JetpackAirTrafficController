/**
 * RadioMessageFormatter.java
 * by Haisam Elkewidy
 *
 * This class handles RadioMessageFormatter functionality in the Air Traffic Controller system.
 *
 * Methods:
 *   - RadioMessageFormatter()
 *   - format(message)
 *
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
