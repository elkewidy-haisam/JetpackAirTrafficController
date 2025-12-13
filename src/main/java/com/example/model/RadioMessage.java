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
    /** Identifier of the message sender (callsign or controller ID) */
    private final String sender;
    /** Identifier of the message receiver (callsign or controller ID) */
    private final String receiver;
    /** The message content text/payload */
    private final String content;
    /** The type/category of this message */
    private final MessageType type;

    /**
     * Enumeration of message types for categorization and filtering.
     */
    public enum MessageType {
        /** Instruction messages containing directives or commands */
        INSTRUCTION,
        /** Status update messages reporting current state */
        STATUS,
        /** Alert messages for warnings or urgent notifications */
        ALERT,
        /** General communication messages */
        GENERAL
    }

    /**
     * Constructs a new RadioMessage with specified parameters.
     * Message is immutable after construction.
     * 
     * @param sender the identifier of who is sending the message
     * @param receiver the identifier of who should receive the message
     * @param content the text content of the message
     * @param type the message type classification
     */
    public RadioMessage(String sender, String receiver, String content, MessageType type) {
        this.sender = sender;      // Store sender identifier
        this.receiver = receiver;  // Store receiver identifier
        this.content = content;    // Store message content
        this.type = type;          // Store message type
    }

    /**
     * Returns the sender identifier.
     * @return the sender's callsign or ID
     */
    public String getSender() {
        return sender;  // Return stored sender
    }

    /**
     * Returns the receiver identifier.
     * @return the receiver's callsign or ID
     */
    public String getReceiver() {
        return receiver;  // Return stored receiver
    }

    /**
     * Returns the message content.
     * @return the message text
     */
    public String getContent() {
        return content;  // Return stored content
    }

    /**
     * Returns the message type.
     * @return the MessageType classification
     */
    public MessageType getType() {
        return type;  // Return stored message type
    }

    /**
     * Returns a formatted string representation of this message.
     * @return formatted string with type, sender, receiver, and content
     */
    @Override
    public String toString() {
        return String.format("RadioMessage[type=%s, from=%s, to=%s, content=%s]", type, sender, receiver, content);  // Format and return message info
    }
}
