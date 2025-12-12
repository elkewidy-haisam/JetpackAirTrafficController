/*
 * RadioMessageFormatter.java
 * Part of Jetpack Air Traffic Controller
 *
 * Formats radio messages for display and logging.
 *
 * (c) 2025 Haisam Elkewidy. All rights reserved.
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
