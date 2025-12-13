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
    /** Identifier of the message sender (e.g., "ATC-1", "JET-042") */
    private final String sender;
    /** Identifier of the message receiver (e.g., "JET-042", "ALL") */
    private final String receiver;
    /** The message content/payload (free-form text) */
    private final String content;
    /** The type/category of this message for filtering and display */
    private final MessageType type;

    /**
     * Enumeration of possible radio message types.
     * Used to categorize and prioritize radio communications.
     */
    public enum MessageType {
        INSTRUCTION,  // Command or instruction from ATC to pilot
        STATUS,       // Status update or report from pilot to ATC
        ALERT,        // Emergency or critical alert message
        GENERAL       // General communication or information
    }

    /**
     * Constructs a new immutable RadioMessage.
     * All fields are final and cannot be changed after creation.
     * 
     * @param sender the sender identifier
     * @param receiver the receiver identifier
     * @param content the message text content
     * @param type the message type category
     */
    public RadioMessage(String sender, String receiver, String content, MessageType type) {
        this.sender = sender;      // Store sender identifier
        this.receiver = receiver;  // Store receiver identifier
        this.content = content;    // Store message content
        this.type = type;          // Store message type
    }

    /**
     * Returns the sender identifier of this message.
     * @return sender ID string
     */
    public String getSender() {
        return sender;  // Return immutable sender
    }

    /**
     * Returns the receiver identifier of this message.
     * @return receiver ID string
     */
    public String getReceiver() {
        return receiver;  // Return immutable receiver
    }

    /**
     * Returns the content/payload of this message.
     * @return message content string
     */
    public String getContent() {
        return content;  // Return immutable content
    }

    /**
     * Returns the type/category of this message.
     * @return MessageType enum value
     */
    public MessageType getType() {
        return type;  // Return immutable type
    }

    /**
     * Returns a formatted string representation of this radio message.
     * Includes type, sender, receiver, and content.
     * 
     * @return formatted message string
     */
    @Override
    public String toString() {
        // Format all message fields into readable string
        return String.format("RadioMessage[type=%s, from=%s, to=%s, content=%s]", 
                type, sender, receiver, content);
    }
}
