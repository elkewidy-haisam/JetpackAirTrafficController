/**
 * Represents a structured radio transmission between ATC and jetpack pilots.
 * 
 * Purpose:
 * Models a single radio communication message with sender/receiver identification, content payload,
 * and message type classification. Provides a standardized data structure for radio communications
 * throughout the system, enabling logging, replay, and display in operator interfaces. Supports
 * categorization of messages for filtering and prioritization.
 * 
 * Key Responsibilities:
 * - Store sender and receiver identifiers (callsigns, controller IDs)
 * - Encapsulate message content as free-form text
 * - Classify message type (INSTRUCTION, STATUS, ALERT, GENERAL)
 * - Provide immutable message records for audit compliance
 * - Support formatted string output for logging and display
 * 
 * Interactions:
 * - Created by Radio subsystem during transmission operations
 * - Logged via RadioTransmissionLogger for compliance records
 * - Formatted by RadioMessageFormatter for display consistency
 * - Displayed in RadarTapeWindow for operator communication history
 * - Used in RadioCommandExecutor to parse and execute instructions
 * - Referenced in session exports for communication playback
 * 
 * Patterns & Constraints:
 * - Immutable value object ensures message integrity after creation
 * - MessageType enum provides fixed set of communication categories
 * - Lightweight structure suitable for high-frequency message generation
 * - No validation logic; consumers responsible for content interpretation
 * - Thread-safe by virtue of immutability
 * 
 * @author Haisam Elkewidy
 */

package com.example.model;

public class RadioMessage {
    private final String sender;
    private final String receiver;
    private final String content;
    private final MessageType type;

    public enum MessageType {
        INSTRUCTION, STATUS, ALERT, GENERAL
    }

    public RadioMessage(String sender, String receiver, String content, MessageType type) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("RadioMessage[type=%s, from=%s, to=%s, content=%s]", type, sender, receiver, content);
    }
}
