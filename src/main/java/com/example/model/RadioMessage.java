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

/**
 * Immutable radio message model for ATC communications.
 * Structured data representing radio transmissions between sender and receiver.
 */
public class RadioMessage {
    // Callsign or ID of message sender (controller or pilot)
    private final String sender;
    // Callsign or ID of message receiver (controller or pilot)
    private final String receiver;
    // Text content of the radio message
    private final String content;
    // Classification type of message for filtering/prioritization
    private final MessageType type;

    /**
     * Enumeration of radio message types.
     * Categories for communication classification and handling.
     */
    public enum MessageType {
        // ATC instruction or clearance to pilot
        INSTRUCTION,
        // Status report or position update
        STATUS,
        // Alert or warning message
        ALERT,
        // General communication
        GENERAL
    }

    /**
     * Constructs a new immutable RadioMessage.
     *
     * @param sender Sender callsign/identifier
     * @param receiver Receiver callsign/identifier
     * @param content Message text content
     * @param type Message type classification
     */
    public RadioMessage(String sender, String receiver, String content, MessageType type) {
        // Store sender identifier
        this.sender = sender;
        // Store receiver identifier
        this.receiver = receiver;
        // Store message content
        this.content = content;
        // Store message type classification
        this.type = type;
    }

    /**
     * Gets the sender identifier.
     *
     * @return Sender callsign/ID
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the receiver identifier.
     *
     * @return Receiver callsign/ID
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Gets the message content.
     *
     * @return Message text
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the message type.
     *
     * @return Message type classification
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Returns formatted string representation of radio message.
     *
     * @return Formatted string with type, sender, receiver, and content
     */
    @Override
    public String toString() {
        // Format as: RadioMessage[type=X, from=Y, to=Z, content=W]
        return String.format("RadioMessage[type=%s, from=%s, to=%s, content=%s]", type, sender, receiver, content);
    }
}
