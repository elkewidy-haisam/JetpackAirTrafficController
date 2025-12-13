/**
 * Formats radio messages into standardized strings for display in operator interfaces and logs.
 * 
 * Purpose:
 * Provides consistent, readable formatting of RadioMessage objects for display in UI components
 * (RadarTapeWindow, communication panels) and log files. Ensures uniform message presentation
 * with clear identification of message type, sender, receiver, and content. Supports null-safe
 * formatting with defensive fallback for invalid messages.
 * 
 * Key Responsibilities:
 * - Format RadioMessage objects into human-readable strings
 * - Include message type classification in formatted output
 * - Display sender and receiver identifications clearly
 * - Present message content in consistent format
 * - Handle null/invalid messages with defensive fallback
 * - Support display in RadarTapeWindow and communication logs
 * - Maintain formatting consistency across system
 * 
 * Interactions:
 * - Used by RadarTapeWindow for message display formatting
 * - Referenced by RadioTransmissionLogger for log file entries
 * - Integrates with RadioMessage for field access (type, sender, receiver, content)
 * - Supports UI components displaying radio communications
 * - Coordinates with CityLogManager for formatted log writing
 * - Used in debugging and operator training scenarios
 * 
 * Patterns & Constraints:
 * - Stateless utility formatter; no internal state
 * - Thread-safe due to stateless design
 * - Null-safe: returns "[Invalid Message]" for null input
 * - Format template: "[TYPE] From: SENDER | To: RECEIVER | CONTENT"
 * - Message types: INSTRUCTION, STATUS, ALERT, GENERAL
 * - Single responsibility: formatting only (no parsing or validation)
 * - Lightweight and efficient for high-frequency message formatting
 * - Example output: "[INSTRUCTION] From: ATC-1 | To: ALPHA-01 | Descend to 150 feet"
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
